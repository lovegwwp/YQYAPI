package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.mapper.*;
import com.jyss.yqy.service.JBonusFPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JRecordService;

@Service
@Transactional
public class JRecordServiceImpl implements JRecordService{
	
	@Autowired
	private JRecordMapper recordMapper;
	@Autowired
	private JBonusScjMapper bonusScjMapper;
	@Autowired
	private OrdersBMapper ordersBMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private JBonusFPService jBonusFPService;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;

	
	/**
	 * 计算市场奖（量奖）
	 */
	@Override
	public Map<String,String> insertJBonusScj(){

		//修改今日pv
		insertJbonusScj();

		//计算总监助理
		updateUserCash();
		
		List<JRecord> uIdList = recordMapper.selectJRecord();  //查询用户列表
		for (int i = 0; i < uIdList.size(); i++) {
        	JRecord jRecord = uIdList.get(i);
			JBonusScj bonusScj = new JBonusScj();
			List<JRecord> recordList = recordMapper.selectJRecordByPid(jRecord.getuId());  //查询子列表
			if(recordList != null && recordList.size()>1){
				bonusScj.setuId(jRecord.getuId());
				JRecord record1 = recordList.get(0);
				JRecord record2 = recordList.get(1);
				Integer depart1 = record1.getDepart();
				Integer depart2 = record2.getDepart();
				
				//查询返现比列和封顶值
				Xtcl xtcl1 = xtclMapper.getClsValue("ljfde_type", "4");        //量奖比例
				float float1 = Float.parseFloat(xtcl1.getBz_value());       			    //0.1

				Xtcl xtcl2 = xtclMapper.getClsValue("ljfde_type", "1");        //初级代理人日封顶金额
				float float2 = Float.parseFloat(xtcl2.getBz_value());        			    //5000.00
				Xtcl xtcl3 = xtclMapper.getClsValue("ljfde_type", "2");        //中级代理人日封顶金额
				float float3 = Float.parseFloat(xtcl3.getBz_value());        			    //15000.00
				Xtcl xtcl4 = xtclMapper.getClsValue("ljfde_type", "3");        //高级代理人日封顶金额
				float float4 = Float.parseFloat(xtcl4.getBz_value());        			    //25000.00


				List<UserBean> userList = userMapper.getUserScoreById(jRecord.getuId());
				UserBean userBean = userList.get(0);
				int level = userBean.getIsChuangke();
				
				if(depart1 == 1 && depart2 == 2){
					bonusScj.setaId(record1.getuId());
					bonusScj.setbId(record2.getuId());
					float total1 = getTotal(record1.getuId());
					bonusScj.setaPv(total1);
					float total2 = getTotal(record2.getuId());
					bonusScj.setbPv(total2);
					
					float money = 0.00f;
					if(level == 2){
						money = Math.min(total1, total2)*float1 <= float2 ? Math.min(total1, total2)*float1 : float2;
					}else if(level == 3){
						money = Math.min(total1, total2)*float1 <= float3 ? Math.min(total1, total2)*float1 : float3;
					}else if(level == 4){
						money = Math.min(total1, total2)*float1 <= float4 ? Math.min(total1, total2)*float1 : float4;
					}else if(level == 5){
						money = Math.min(total1, total2)*float1 ;
					}
					bonusScj.setPv(money);
					bonusScj.setStatus(1);
					int count = bonusScjMapper.insertBonusScj(bonusScj);
					//计算积分
					if(count == 1){
						if(money > 0){
							jBonusFPService.insertScoreBalance(jRecord.getuId(),money,6);
						}
					}
					//更新结余
					if(total1 >= total2){
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record1.getuId());
						recordMapper.updateJRecordByUid(null, 0.00+"", record2.getuId());
					}else {
						recordMapper.updateJRecordByUid(null, 0.00+"", record1.getuId());
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record2.getuId());
					}
				}else if(depart1 == 2 && depart2 == 1){
					bonusScj.setaId(record2.getuId());
					bonusScj.setbId(record1.getuId());
					float total1 = getTotal(record2.getuId());
					bonusScj.setaPv(total1);
					float total2 = getTotal(record1.getuId());
					bonusScj.setbPv(total2);
					
					float money = 0.00f;
					if(level == 2){
						money = Math.min(total1, total2)*float1 <= float2 ? Math.min(total1, total2)*float1 : float2;
					}else if(level == 3){
						money = Math.min(total1, total2)*float1 <= float3 ? Math.min(total1, total2)*float1 : float3;
					}else if(level == 4){
						money = Math.min(total1, total2)*float1 <= float4 ? Math.min(total1, total2)*float1 : float4;
					}else if(level == 5){
						money = Math.min(total1, total2)*float1 ;
					}
					bonusScj.setPv(money);
					bonusScj.setStatus(1);
					int count = bonusScjMapper.insertBonusScj(bonusScj);
					//计算积分
					if(count == 1){
						if(money > 0){
							jBonusFPService.insertScoreBalance(jRecord.getuId(),money,6);
						}
					}
					
					if(total1 >= total2){
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record2.getuId());
						recordMapper.updateJRecordByUid(null, 0.00+"", record1.getuId());
					}else {
						recordMapper.updateJRecordByUid(null, 0.00+"", record2.getuId());
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record1.getuId());
					}
				}
			}
		}


		Map<String,String> map = new HashMap<String,String>();
		map.put("message", "量奖和积分计算完成时间："+new Date());
		return map;
	}
	
	/**
	 * 计算总pv
	 */
	private float getTotal(int id){
		Float total = 0.00f;
		List<JRecord> list = getJRecordList(id);
        for (int i = 0; i < list.size(); i++) {
        	JRecord record = list.get(i);
        	total += record.getPv();
        }
		return total;
	}

	/**
	 * 二叉树递归的前序遍历
	 */
	public List<JRecord> getJRecordList(int uId){
		List<JRecord> result = new ArrayList<JRecord>();
		getJRecordList(uId,result);
		return result;
	}
	private void getJRecordList(int id,List<JRecord> result){
		//如果节点为空，返回
        if("null".equals(String.valueOf(id))){
            return;
        }
        //不为空，则加入节点的值
        List<JRecord> list = recordMapper.selectJRecordByUid(id);
        if(list != null && list.size()>0 ){
        	//JRecord record = list.get(0);
        	result.add(list.get(0));
        	List<JRecord> recordList = recordMapper.selectJRecordByPid(id);
        	if(recordList != null && recordList.size()>1){
        		JRecord record1 = recordList.get(0);
        		JRecord record2 = recordList.get(1);
        		Integer depart1 = record1.getDepart();
        		Integer depart2 = record2.getDepart();
        		if(depart1 == 1 && depart2 == 2){
        			//先递归左孩子
        			getJRecordList(record1.getuId(),result);
        			//再递归右孩子
        	        getJRecordList(record2.getuId(),result);
        		}else if(depart1 == 2 && depart2 == 1){
        			//先递归左孩子
        			getJRecordList(record2.getuId(),result);
        			//再递归右孩子
        	        getJRecordList(record1.getuId(),result);
        		}
        	}
        }
	}
	
	/**
	 * 计算市场奖前修改今日pv
	 */
	private void insertJbonusScj(){
		int record = recordMapper.updateJRecord();     //将每日的pv值清0
		if(record > 0){
			List<JRecord> orderPvList = ordersBMapper.getSuccessOrderTotal();
			for (JRecord jRecord : orderPvList) {
				recordMapper.updateJRecordByUid(jRecord.getPv()+"",null,jRecord.getuId());
			}
		}
	}


	/**
	 * 计算总监助理
	 */
	private void updateUserCash(){

		float float1 = 0.002f;
		//查询比列
		Xtcl xtcl1 = xtclMapper.getClsValue("zjzl_type", "1");        //总监助理比例
		float1 = Float.parseFloat(xtcl1.getBz_value());       			           //0.002

		List<UserBean> userBeans = userMapper.selectUserZL();
		if(userBeans != null && userBeans.size()>0){
			for (UserBean userBean : userBeans) {

				float total = getTotal(userBean.getAge());           //查询总监市场的总pv
				float cashScore = total * float1;

				//添加股券
				ScoreBalance score = new ScoreBalance();
				score.setEnd(2);
				score.setuUuid(userBean.getUuid());
				score.setCategory(12);
				score.setType(1);
				score.setScore(cashScore);
				score.setJyScore(userBean.getCashScore() + cashScore);
				score.setStatus(1);
				int count = scoreBalanceMapper.addCashScore(score);
				if(count == 1){
					UserBean userBean1 = new UserBean();
					userBean1.setId(userBean.getId());
					userBean1.setCashScore(userBean.getCashScore() + cashScore);
					userMapper.updateScore(userBean1);
				}
			}
		}
	}



}
