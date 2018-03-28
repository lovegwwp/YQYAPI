package com.jyss.yqy.service.impl;


import com.jyss.yqy.entity.JBonusCj;
import com.jyss.yqy.entity.JBonusResult;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.mapper.JBonusCjMapper;
import com.jyss.yqy.mapper.JRecordMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusCjService;
import com.jyss.yqy.service.JBonusFPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class JBonusCjServiceImpl implements JBonusCjService {

	@Autowired
	private JBonusCjMapper jBonusCjMapper;
	@Autowired
	private JRecordMapper recordMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private JBonusFPService jBonusFPService;
	@Autowired
	private UserMapper userMapper;


	/**
	 * 层奖查询（今日）
	 */
	@Override
	public JBonusResult getJBonusCj(Integer uId) {
		float earnings = jBonusCjMapper.selectEarnings(uId);
		float total = jBonusCjMapper.selectTotal(uId);
		List<JBonusCj> list = jBonusCjMapper.selectJBonusCj(uId);

		JBonusResult result = new JBonusResult();
		result.setEarnings(earnings);
		result.setTotal(total);
		result.setData(list);
		return result;
	}


	/**
	 * 计算层奖
	 */
	@Override
	public Map<String, String> insertJBonusCj() {
		for (int i = 0; i < 5; i++) {
			updateUserCjStatus();                 //重复查询5次
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", "层奖和积分计算完成时间："+new Date());
		return map;
	}

	//计算层奖
	private void updateUserCjStatus(){

		Xtcl xtcl1 = xtclMapper.getClsValue("cjbl_type", "1");      //层奖第一层比例
		float float1 = Float.parseFloat(xtcl1.getBz_value());        			 //0.7
		Xtcl xtcl2 = xtclMapper.getClsValue("cjbl_type", "2");      //层奖第二层比例
		float float2 = Float.parseFloat(xtcl2.getBz_value());        			 //0.5
		Xtcl xtcl3 = xtclMapper.getClsValue("cjbl_type", "3");      //层奖第三至第五层比例
		float float3 = Float.parseFloat(xtcl3.getBz_value());        			 //0.3

		List<JRecord> records = recordMapper.selectUserByCj();     //查询所有层奖用户
		for (JRecord record : records) {
			List<JRecord> recordList = recordMapper.selectUserByPid(record.getuId());
			if(recordList != null && recordList.size() == 2){
				JRecord record1 = recordList.get(0);
				JRecord record2 = recordList.get(1);

				Integer status = record.getStatus();     //层奖所处第几层
				Integer floor = record.getFloor();       //市场所处第几层

				JBonusCj bonusCj = new JBonusCj();
				bonusCj.setuId(record.getuId());
				bonusCj.setStatus(1);

				if(status == 1){
					int level = Math.min(record1.getDepart(), record2.getDepart());       //代理级别
					Xtcl xtcl4 = xtclMapper.getClsValue("cjhhr_type", level+"");     //消费额
					if(xtcl4 != null){
						float float4 = Float.parseFloat(xtcl4.getBz_value());
						float money = float4 * float1;
						//添加层奖
						bonusCj.setName("第一层");
						bonusCj.setAmount(money);
						int count = jBonusCjMapper.insert(bonusCj);
						if(count == 1){
							//计算积分
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), money, 5);
							//升层级
							if(flag){
								userMapper.updateUserCjStatus(record.getStatus()+1,null,record.getuId());
							}
						}
					}
				}else if(status == 2){
					int level = getMinUserChuangke(record1.getuId(), record2.getuId(), floor + 2);
					Xtcl xtcl4 = xtclMapper.getClsValue("cjhhr_type", level+"");     //消费额
					if(xtcl4 != null){
						float float4 = Float.parseFloat(xtcl4.getBz_value());
						float money = float4 * float2;
						//添加层奖
						bonusCj.setName("第二层");
						bonusCj.setAmount(money);
						int count = jBonusCjMapper.insert(bonusCj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), money, 5);
							//升层级
							if(flag){
								userMapper.updateUserCjStatus(record.getStatus()+1,null,record.getuId());
							}
						}
					}
				}else if(status == 3){
					int level = getMinUserChuangke(record1.getuId(), record2.getuId(), floor + 3);
					Xtcl xtcl4 = xtclMapper.getClsValue("cjhhr_type", level+"");     //消费额
					if(xtcl4 != null){
						float float4 = Float.parseFloat(xtcl4.getBz_value());
						float money = float4 * float3;
						//添加层奖
						bonusCj.setName("第三层");
						bonusCj.setAmount(money);
						int count = jBonusCjMapper.insert(bonusCj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), money, 5);
							//升层级
							if(flag){
								userMapper.updateUserCjStatus(record.getStatus()+1,null,record.getuId());
							}
						}
					}
				}else if(status == 4){
					int level = getMinUserChuangke(record1.getuId(), record2.getuId(), floor + 4);
					Xtcl xtcl4 = xtclMapper.getClsValue("cjhhr_type", level+"");     //消费额
					if(xtcl4 != null){
						float float4 = Float.parseFloat(xtcl4.getBz_value());
						float money = float4 * float3;
						//添加层奖
						bonusCj.setName("第四层");
						bonusCj.setAmount(money);
						int count = jBonusCjMapper.insert(bonusCj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), money, 5);
							//升层级
							if(flag){
								userMapper.updateUserCjStatus(record.getStatus()+1,null,record.getuId());
							}
						}
					}
				}else if(status == 5){
					int level = getMinUserChuangke(record1.getuId(), record2.getuId(), floor + 5);
					Xtcl xtcl4 = xtclMapper.getClsValue("cjhhr_type", level+"");     //消费额
					if(xtcl4 != null){
						float float4 = Float.parseFloat(xtcl4.getBz_value());
						float money = float4 * float3;
						//添加层奖
						bonusCj.setName("第五层");
						bonusCj.setAmount(money);
						int count = jBonusCjMapper.insert(bonusCj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), money, 5);
							//升层级
							if(flag){
								userMapper.updateUserCjStatus(record.getStatus()+1,null,record.getuId());
							}
						}
					}
				}
			}
		}
	}

	//两边最大，取小
	private int getMinUserChuangke(int uId1,int uId2,int floor){
		//左市场
		List<Integer> result1 = new ArrayList<>();
		getJRecordListByPid(uId1,floor,result1);
		//右市场
		List<Integer> result2 = new ArrayList<>();
		getJRecordListByPid(uId2,floor,result2);
		if(result1 != null && result1.size()>0 && result2 != null && result2.size()>0){
			Integer a = Collections.max(result1);
			Integer b = Collections.max(result2);
			return Math.min(a,b);
		}
		return 8;
	}

	//递归查询
	private void getJRecordListByPid(int pId, int floor, List<Integer> result){
		List<JRecord> list = recordMapper.selectUserByPid(pId);
		if(list != null && list.size()>0){
			for (JRecord jRecord : list) {
				if(jRecord.getFloor() < (floor + 1)){
					if(jRecord.getFloor() == floor){
						result.add(jRecord.getDepart());
					}
					getJRecordListByPid(jRecord.getuId(),floor,result);
				}
			}
		}
	}

}
