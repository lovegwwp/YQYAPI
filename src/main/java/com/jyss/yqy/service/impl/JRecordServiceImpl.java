package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.JRecordMapper;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
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
	private ScoreBalanceMapper scoreBalanceMapper;

	
	/**
	 * 计算市场奖
	 */
	@Override
	public Map<String,String> insertJBonusScj(){
		
		insertJbonusScj();       //修改今日pv
		
		Map<String,String> map = new HashMap<String,String>();
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
				Xtcl xtcl1 = xtclMapper.getClsValue("scj_type", "1");        //初级代理人市场奖比例
				float float1 = Float.parseFloat(xtcl1.getBz_value());       			  //0.12
				Xtcl xtcl2 = xtclMapper.getClsValue("scj_type", "2");        //中级代理人市场奖比例
				float float2 = Float.parseFloat(xtcl2.getBz_value());        			  //0.16
				Xtcl xtcl3 = xtclMapper.getClsValue("scj_type", "3");        //高级代理人市场奖比例
				float float3 = Float.parseFloat(xtcl3.getBz_value());       			  //0.22
				Xtcl xtcl4 = xtclMapper.getClsValue("scj_type", "4");        //经理人市场奖比例
				float float4 = Float.parseFloat(xtcl4.getBz_value());        			  //0.10
				Xtcl xtcl5 = xtclMapper.getClsValue("scj_type", "5");        //初级代理人日封顶金额
				float float5 = Float.parseFloat(xtcl5.getBz_value());        			  //3000.00
				Xtcl xtcl6 = xtclMapper.getClsValue("scj_type", "6");        //中级代理人日封顶金额
				float float6 = Float.parseFloat(xtcl6.getBz_value());        			  //6000.00
				Xtcl xtcl7 = xtclMapper.getClsValue("scj_type", "7");        //高级代理人日封顶金额
				float float7 = Float.parseFloat(xtcl7.getBz_value());        			  //9000.00
				Xtcl xtcl8 = xtclMapper.getClsValue("scj_type", "8");        //经理人日封顶金额
				float float8 = Float.parseFloat(xtcl8.getBz_value());        			  //1000.00
				Xtcl xtcl9 = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
				float float9 = Float.parseFloat(xtcl9.getBz_value());        			  //0.7

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
						money = Math.min(total1, total2)*float1 <= (float5/float9) ? Math.min(total1, total2)*float1 : (float5/float9);
					}else if(level == 3){
						money = Math.min(total1, total2)*float2 <= (float6/float9) ? Math.min(total1, total2)*float2 : (float6/float9);
					}else if(level == 4){
						money = Math.min(total1, total2)*float3 <= (float7/float9) ? Math.min(total1, total2)*float3 : (float7/float9);
					}else if(level == 5){
						money = Math.min(total1, total2)*float4 <= (float8/float9) ? Math.min(total1, total2)*float4 : (float8/float9);
					}
					bonusScj.setPv(money);
					bonusScj.setStatus(1);
					bonusScjMapper.insertBonusScj(bonusScj);
					
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
						money = Math.min(total1, total2)*float1 <= (float5/float9) ? Math.min(total1, total2)*float1 : (float5/float9);
					}else if(level == 3){
						money = Math.min(total1, total2)*float2 <= (float6/float9) ? Math.min(total1, total2)*float2 : (float6/float9);
					}else if(level == 4){
						money = Math.min(total1, total2)*float3 <= (float7/float9) ? Math.min(total1, total2)*float3 : (float7/float9);
					}else if(level == 5){
						money = Math.min(total1, total2)*float4 <= (float8/float9) ? Math.min(total1, total2)*float4 : (float8/float9);
					}
					bonusScj.setPv(money);
					bonusScj.setStatus(1);
					bonusScjMapper.insertBonusScj(bonusScj);
					
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
		
		//计算现金积分和购物积分
		List<JBonusScj> bonusScjList = bonusScjMapper.selectEveryDayEarnings();
		if(bonusScjList != null && bonusScjList.size()>0){
			for (JBonusScj jBonusScj : bonusScjList) {
				List<UserBean> userList = userMapper.getUserScoreById(jBonusScj.getuId());
				UserBean userBean = userList.get(0);
				
				Xtcl xtcl7 = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
				float float7 = Float.parseFloat(xtcl7.getBz_value());        			  //0.7
				Xtcl xtcl8 = xtclMapper.getClsValue("jjbl_type", "gw");      //购物积分比例
				float float8 = Float.parseFloat(xtcl8.getBz_value());        			  //0.2
				
				Float money = jBonusScj.getPv();
				Float totalPv = userBean.getTotalPv();
				if(totalPv > 0){
					if(money <= totalPv){

						//添加现金积分
						ScoreBalance score1 = new ScoreBalance();
						score1.setEnd(2);
						score1.setuUuid(userBean.getUuid());
						score1.setCategory(7);
						score1.setType(1);
						score1.setScore(money * float7);
						score1.setJyScore(money * float7+ userBean.getCashScore());
						//score1.setCreatedAt(new Date());
						score1.setStatus(1);
						int count1 = scoreBalanceMapper.addCashScore(score1);

						ScoreBalance score2 = new ScoreBalance();
						score2.setEnd(2);
						score2.setuUuid(userBean.getUuid());
						score2.setCategory(7);
						score2.setType(1);
						score2.setScore(money * float8);
						score2.setJyScore(money * float8 + userBean.getShoppingScore());
						//score2.setCreatedAt(new Date());
						score2.setStatus(1);
						int count2 = scoreBalanceMapper.addShoppingScore(score2);

						if(count1 == 1 && count2 == 1){
							UserBean userBean2 = new UserBean();
							userBean2.setId(jBonusScj.getuId());
							userBean2.setCashScore(money * float7+ userBean.getCashScore());
							userBean2.setShoppingScore(money * float8 + userBean.getShoppingScore());
							userBean2.setTotalPv(totalPv - money);
							userMapper.updateScore(userBean2);
						}
					}else {
						//添加现金积分
						ScoreBalance score1 = new ScoreBalance();
						score1.setEnd(2);
						score1.setuUuid(userBean.getUuid());
						score1.setCategory(7);
						score1.setType(1);
						score1.setScore(totalPv * float7);
						score1.setJyScore(totalPv * float7+ userBean.getCashScore());
						//score1.setCreatedAt(new Date());
						score1.setStatus(1);
						int count1 = scoreBalanceMapper.addCashScore(score1);

						ScoreBalance score2 = new ScoreBalance();
						score2.setEnd(2);
						score2.setuUuid(userBean.getUuid());
						score2.setCategory(7);
						score2.setType(1);
						score2.setScore(totalPv * float8);
						score2.setJyScore(totalPv * float8 + userBean.getShoppingScore());
						//score2.setCreatedAt(new Date());
						score2.setStatus(1);
						int count2 = scoreBalanceMapper.addShoppingScore(score2);

						if(count1 == 1 && count2 == 1){
							UserBean userBean2 = new UserBean();
							userBean2.setId(jBonusScj.getuId());
							userBean2.setCashScore(totalPv * float7+ userBean.getCashScore());
							userBean2.setShoppingScore(totalPv * float8 + userBean.getShoppingScore());
							userBean2.setTotalPv(totalPv - totalPv);
							userMapper.updateScore(userBean2);
						}
					}
				}
			}
		}
		map.put("message", "市场奖和积分计算完成时间："+new Date());
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
			List<JRecord> orderPvList = ordersBMapper.getSuccessOrderPv();
			for (JRecord jRecord : orderPvList) {
				recordMapper.updateJRecordByUid(jRecord.getPv()+"",null,jRecord.getuId());
			}
		}
	}

	

}
