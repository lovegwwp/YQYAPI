package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusFxjResult;
import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.UUserRRecordBExample;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.UUserRRecordBExample.Criteria;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusFxjMapper;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UUserRRecordBMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusFxjService;


@Service
@Transactional
public class JBonusFxjServiceImpl implements JBonusFxjService{
	
	@Autowired
	private OrdersBMapper ordersBMapper;
	@Autowired
	private UUserRRecordBMapper userRecordMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private JBonusFxjMapper bonusFxjMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;
	
	
	/**
	 * 查询用户分销奖
	 */
	@Override
	public JBonusFxjResult getJBonusFxj(int uId,int page,int limit){
		JBonusFxjResult result = new JBonusFxjResult();
		float earnings = bonusFxjMapper.selectEarnings(uId);
		float total = bonusFxjMapper.selectTotal(uId);
		PageHelper.startPage(page, limit);         //分页，只对该语句以后的第一个查询语句得到的数据进行分页
		List<JBonusFxj> list = bonusFxjMapper.selectJBonusFxjWek(uId);
		PageInfo<JBonusFxj> pageInfo = new PageInfo<JBonusFxj>(list);
		result.setEarnings(earnings);
		result.setTotal(total);
		result.setData(list);
		return result;
		
	}

	@Override
	public JBonusFxjResult selectJBonusFxjByDay(int uId,int page,int limit,String beginTime,String endTime) {
		JBonusFxjResult result = new JBonusFxjResult();
		float totalByDay = bonusFxjMapper.selectFxjTotalByDay(uId, beginTime, endTime);
		PageHelper.startPage(page, limit);
		List<JBonusFxj> list = bonusFxjMapper.selectJBonusFxjByDay(uId, beginTime, endTime);
		PageInfo<JBonusFxj> pageInfo = new PageInfo<JBonusFxj>(list);
		result.setEarnings(0f);
		result.setTotal(totalByDay);
		result.setData(list);
		return result;
	}

	@Override
	public JBonusFxjResult selectJBonusFxjByMonth(int uId,int page,int limit,String month) {
		JBonusFxjResult result = new JBonusFxjResult();
		float totalByMonth = bonusFxjMapper.selectFxjTotalByMonth(uId, month);
		PageHelper.startPage(page, limit);
		List<JBonusFxj> list = bonusFxjMapper.selectJBonusFxjByMonth(uId, month);
		PageInfo<JBonusFxj> pageInfo = new PageInfo<JBonusFxj>(list);
		result.setEarnings(0f);
		result.setTotal(totalByMonth);
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 计算分销奖和积分
	 */
	@Override
	public Map<String, String> insertJBonusFxj() {
		Map<String, String> map = new HashMap<String, String>();
		List<JRecord> orderPvList = ordersBMapper.getSuccessOrderPv();
		if(orderPvList != null && orderPvList.size()>0){
			
			//查询返回比例
			Xtcl xtcl1 = xtclMapper.getClsValue("fxj_type", "1");        //分销奖第一代
			float float1 = Float.parseFloat(xtcl1.getBz_value());        //0.06
			Xtcl xtcl2 = xtclMapper.getClsValue("fxj_type", "2");        //分销奖第二代
			float float2 = Float.parseFloat(xtcl2.getBz_value());        //0.03
			Xtcl xtcl3 = xtclMapper.getClsValue("fxj_type", "3");        //分销奖第三代
			float float3 = Float.parseFloat(xtcl3.getBz_value());        //0.02
			Xtcl xtcl4 = xtclMapper.getClsValue("fxj_type", "4");        //分销奖第四代至第八代
			float float4 = Float.parseFloat(xtcl4.getBz_value());        //0.01
			
			for (JRecord jRecord : orderPvList) {
				Float pv = jRecord.getPv();
				//第一代
				UUserRRecordBExample example = new UUserRRecordBExample();
				Criteria criteria = example.createCriteria();
				criteria.andUIdEqualTo(jRecord.getuId());
				criteria.andStatusEqualTo(1);
				List<UUserRRecordB> list1 = userRecordMapper.selectByExample(example);
				if(list1 != null && list1.size()>0){
					UUserRRecordB recordB1 = list1.get(0);
					List<UserBean> nameById1 = userMapper.getUserNameById(recordB1.getuId());
					UserBean userBean1 = nameById1.get(0);
					JBonusFxj bonusFxj1 = new JBonusFxj();
					bonusFxj1.setuId(jRecord.getuId());
					bonusFxj1.setName(userBean1.getRealName());
					bonusFxj1.setParentId(recordB1.getrId());
					bonusFxj1.setAmount(pv * float1);
					bonusFxj1.setStatus(1);
					int count1 = bonusFxjMapper.insert(bonusFxj1);
					if(count1 == 1){
						//第二代
						UUserRRecordBExample example1 = new UUserRRecordBExample();
						Criteria criteria1 = example1.createCriteria();
						criteria1.andUIdEqualTo(recordB1.getrId());
						criteria1.andStatusEqualTo(1);
						List<UUserRRecordB> list2 = userRecordMapper.selectByExample(example1);
						if(list2 != null && list2.size()>0){
							UUserRRecordB recordB2 = list2.get(0);
							List<UserBean> nameById2 = userMapper.getUserNameById(recordB2.getuId());
							UserBean userBean2 = nameById2.get(0);
							JBonusFxj bonusFxj2 = new JBonusFxj();
							bonusFxj2.setuId(recordB2.getuId());
							bonusFxj2.setName(userBean2.getRealName());
							bonusFxj2.setParentId(recordB2.getrId());
							bonusFxj2.setAmount(pv * float2);
							bonusFxj2.setStatus(1);
							int count2 = bonusFxjMapper.insert(bonusFxj2);
							if(count2 == 1){
								//第三代
								UUserRRecordBExample example2 = new UUserRRecordBExample();
								Criteria criteria2 = example2.createCriteria();
								criteria2.andUIdEqualTo(recordB2.getrId());
								criteria2.andStatusEqualTo(1);
								List<UUserRRecordB> list3 = userRecordMapper.selectByExample(example2);
								if(list3 != null && list3.size()>0){
									UUserRRecordB recordB3 = list3.get(0);
									List<UserBean> nameById3 = userMapper.getUserNameById(recordB3.getuId());
									UserBean userBean3 = nameById3.get(0);
									JBonusFxj bonusFxj3 = new JBonusFxj();
									bonusFxj3.setuId(recordB3.getuId());
									bonusFxj3.setName(userBean3.getRealName());
									bonusFxj3.setParentId(recordB3.getrId());
									bonusFxj3.setAmount(pv * float3);
									bonusFxj3.setStatus(1);
									int count3 = bonusFxjMapper.insert(bonusFxj3);
									if(count3 == 1){
										//第四代
										UUserRRecordBExample example3 = new UUserRRecordBExample();
										Criteria criteria3 = example3.createCriteria();
										criteria3.andUIdEqualTo(recordB3.getrId());
										criteria3.andStatusEqualTo(1);
										List<UUserRRecordB> list4 = userRecordMapper.selectByExample(example3);
										if(list4 != null && list4.size()>0){
											UUserRRecordB recordB4 = list4.get(0);
											List<UserBean> nameById4 = userMapper.getUserNameById(recordB4.getuId());
											UserBean userBean4 = nameById4.get(0);
											JBonusFxj bonusFxj4 = new JBonusFxj();
											bonusFxj4.setuId(recordB4.getuId());
											bonusFxj4.setName(userBean4.getRealName());
											bonusFxj4.setParentId(recordB4.getrId());
											bonusFxj4.setAmount(pv * float4);
											bonusFxj4.setStatus(1);
											int count4 = bonusFxjMapper.insert(bonusFxj4);
											if(count4 == 1){
												//第五代
												UUserRRecordBExample example4 = new UUserRRecordBExample();
												Criteria criteria4 = example4.createCriteria();
												criteria4.andUIdEqualTo(recordB4.getrId());
												criteria4.andStatusEqualTo(1);
												List<UUserRRecordB> list5 = userRecordMapper.selectByExample(example4);
												if(list5 != null && list5.size()>0){
													UUserRRecordB recordB5 = list5.get(0);
													List<UserBean> nameById5 = userMapper.getUserNameById(recordB5.getuId());
													UserBean userBean5 = nameById5.get(0);
													JBonusFxj bonusFxj5 = new JBonusFxj();
													bonusFxj5.setuId(recordB5.getuId());
													bonusFxj5.setName(userBean5.getRealName());
													bonusFxj5.setParentId(recordB5.getrId());
													bonusFxj5.setAmount(pv * float4);
													bonusFxj5.setStatus(1);
													int count5 = bonusFxjMapper.insert(bonusFxj5);
													if(count5 == 1){
														//第六代
														UUserRRecordBExample example5 = new UUserRRecordBExample();
														Criteria criteria5 = example5.createCriteria();
														criteria5.andUIdEqualTo(recordB5.getrId());
														criteria5.andStatusEqualTo(1);
														List<UUserRRecordB> list6 = userRecordMapper.selectByExample(example5);
														if(list6 != null && list6.size()>0){
															UUserRRecordB recordB6 = list6.get(0);
															List<UserBean> nameById6 = userMapper.getUserNameById(recordB6.getuId());
															UserBean userBean6 = nameById6.get(0);
															JBonusFxj bonusFxj6 = new JBonusFxj();
															bonusFxj6.setuId(recordB6.getuId());
															bonusFxj6.setName(userBean6.getRealName());
															bonusFxj6.setParentId(recordB6.getrId());
															bonusFxj6.setAmount(pv * float4);
															bonusFxj6.setStatus(1);
															int count6 = bonusFxjMapper.insert(bonusFxj6);
															if(count6 == 1){
																//第七代
																UUserRRecordBExample example6 = new UUserRRecordBExample();
																Criteria criteria6 = example6.createCriteria();
																criteria6.andUIdEqualTo(recordB6.getrId());
																criteria6.andStatusEqualTo(1);
																List<UUserRRecordB> list7 = userRecordMapper.selectByExample(example6);
																if(list7 != null && list7.size()>0){
																	UUserRRecordB recordB7 = list7.get(0);
																	List<UserBean> nameById7 = userMapper.getUserNameById(recordB7.getuId());
																	UserBean userBean7 = nameById7.get(0);
																	JBonusFxj bonusFxj7 = new JBonusFxj();
																	bonusFxj7.setuId(recordB7.getuId());
																	bonusFxj7.setName(userBean7.getRealName());
																	bonusFxj7.setParentId(recordB7.getrId());
																	bonusFxj7.setAmount(pv * float4);
																	bonusFxj7.setStatus(1);
																	int count7 = bonusFxjMapper.insert(bonusFxj7);
																	if(count7 == 1){
																		//第八代
																		UUserRRecordBExample example7 = new UUserRRecordBExample();
																		Criteria criteria7 = example7.createCriteria();
																		criteria7.andUIdEqualTo(recordB7.getrId());
																		criteria7.andStatusEqualTo(1);
																		List<UUserRRecordB> list8 = userRecordMapper.selectByExample(example7);
																		if(list8 != null && list8.size()>0){
																			UUserRRecordB recordB8 = list8.get(0);
																			List<UserBean> nameById8 = userMapper.getUserNameById(recordB8.getuId());
																			UserBean userBean8 = nameById8.get(0);
																			JBonusFxj bonusFxj8 = new JBonusFxj();
																			bonusFxj8.setuId(recordB8.getuId());
																			bonusFxj8.setName(userBean8.getRealName());
																			bonusFxj8.setParentId(recordB8.getrId());
																			bonusFxj8.setAmount(pv * float4);
																			bonusFxj8.setStatus(1);
																			bonusFxjMapper.insert(bonusFxj8);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//计算现金积分和购物积分
		List<JBonusFxj> bonusFxjList = bonusFxjMapper.selectEveryDayEarnings();
		if(bonusFxjList != null && bonusFxjList.size()>0){
			for (JBonusFxj jBonusFxj : bonusFxjList) {
				
				//查询积分比例
				Xtcl xtcl5 = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
				float float5 = Float.parseFloat(xtcl5.getBz_value());        //0.7
				Xtcl xtcl6 = xtclMapper.getClsValue("jjbl_type", "gw");      //购物积分比例
				float float6 = Float.parseFloat(xtcl6.getBz_value());        //0.2
				
				List<UserBean> userList = userMapper.getUserScoreById(jBonusFxj.getParentId());
				if(userList != null && userList.size()>0){
					Float money = jBonusFxj.getAmount();
					UserBean userBean = userList.get(0);
					Float totalPv = userBean.getTotalPv();
					if(totalPv > 0){
						if(money <= totalPv){

							//添加现金积分
							ScoreBalance score1 = new ScoreBalance();
							score1.setEnd(2);
							score1.setuUuid(userBean.getUuid());
							score1.setCategory(6);
							score1.setType(1);
							score1.setScore(money * float5);
							score1.setJyScore(money * float5 + userBean.getCashScore());
							//score1.setCreatedAt(new Date());
							score1.setStatus(1);
							int count1 = scoreBalanceMapper.addCashScore(score1);

							ScoreBalance score2 = new ScoreBalance();
							score2.setEnd(2);
							score2.setuUuid(userBean.getUuid());
							score2.setCategory(6);
							score2.setType(1);
							score2.setScore(money * float6);
							score2.setJyScore(money * float6 + userBean.getShoppingScore());
							//score2.setCreatedAt(new Date());
							score2.setStatus(1);
							int count2 = scoreBalanceMapper.addShoppingScore(score2);

							if(count1 == 1 && count2 == 1){
								UserBean userBean2 = new UserBean();
								userBean2.setId(jBonusFxj.getParentId());
								userBean2.setCashScore(money * float5 + userBean.getCashScore());
								userBean2.setShoppingScore(money * float6 + userBean.getShoppingScore());
								userBean2.setTotalPv(totalPv - money);
								userMapper.updateScore(userBean2);
							}
						}else{
							//添加现金积分
							ScoreBalance score1 = new ScoreBalance();
							score1.setEnd(2);
							score1.setuUuid(userBean.getUuid());
							score1.setCategory(6);
							score1.setType(1);
							score1.setScore(totalPv * float5);
							score1.setJyScore(totalPv * float5 + userBean.getCashScore());
							//score1.setCreatedAt(new Date());
							score1.setStatus(1);
							int count1 = scoreBalanceMapper.addCashScore(score1);

							ScoreBalance score2 = new ScoreBalance();
							score2.setEnd(2);
							score2.setuUuid(userBean.getUuid());
							score2.setCategory(6);
							score2.setType(1);
							score2.setScore(totalPv * float6);
							score2.setJyScore(totalPv * float6 + userBean.getShoppingScore());
							//score2.setCreatedAt(new Date());
							score2.setStatus(1);
							int count2 = scoreBalanceMapper.addShoppingScore(score2);

							if(count1 == 1 && count2 == 1){
								UserBean userBean2 = new UserBean();
								userBean2.setId(jBonusFxj.getParentId());
								userBean2.setCashScore(totalPv * float5 + userBean.getCashScore());
								userBean2.setShoppingScore(totalPv * float6 + userBean.getShoppingScore());
								userBean2.setTotalPv(totalPv - totalPv);
								userMapper.updateScore(userBean2);
							}
						}

					}
				}
				
			}
			
		}
		map.put("message", "分销奖和积分计算时间："+new Date());
		return map;
		
	}
	
	

}
