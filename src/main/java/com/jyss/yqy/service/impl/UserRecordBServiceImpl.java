package com.jyss.yqy.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.UUserRRecordBExample;
import com.jyss.yqy.entity.UUserRRecordBExample.Criteria;
import com.jyss.yqy.entity.UserTotalAmount;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusFdjMapper;
import com.jyss.yqy.mapper.JBonusGljMapper;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.UUserRRecordBMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.service.UserRecordBService;

@Service
@Transactional
public class UserRecordBServiceImpl implements UserRecordBService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UUserRRecordBMapper userRecordMapper;
	@Autowired
	private JBonusGljMapper bonusGljMapper;
	@Autowired
	private OrdersBMapper ordersBMapper;
	@Autowired
	private JBonusFdjMapper jBonusFdjMapper;

	@Override
	public Map<String, String> insertUserRecordB(String uuid, String bCode) {
		Map<String, String> map = new HashMap<String, String>();
		List<UserBean> userList = userMapper.getUserByUuid(uuid);
		UserBean userBean = userList.get(0); // 获取被推荐人信息
		int isAuth = userBean.getIsAuth();
		int uLevel = userBean.getIsChuangke();
		List<UserBean> parentList = userMapper.getUserByBCode(bCode);
		if (parentList != null && parentList.size() > 0) {
			UserBean parentUser = parentList.get(0); // 获取推荐人信息
			int pLevel = parentUser.getIsChuangke();
			// 推荐关系表
			if (isAuth == 0) {
				map.put("code", "-1");
				map.put("status", "false");
				map.put("message", "未实名");
				map.put("data", "");
				return map;

			}
			// if(isAuth == 2 && (uLevel==2 || uLevel==3 || uLevel==4) &&
			// (pLevel==2 || pLevel==3 || pLevel==4)){
			if (pLevel == 2 || pLevel == 3 || pLevel == 4) {
				UUserRRecordB userRRecordB = new UUserRRecordB();
				userRRecordB.setuId(userBean.getId());
				userRRecordB.setrId(parentUser.getId());
				userRRecordB.setStatus(0);                    //设置初始值为0
				userRRecordB.setType(pLevel);
				userRRecordB.setCreatedAt(new Date());
				int idNum = userRecordMapper.insert(userRRecordB);
				String val = idNum + "";
				if (val != null && !"".equals(val)) {
					map.put("code", "0");
					map.put("status", "true");
					map.put("message", "推荐码使用成功！");
					map.put("data", "");
					return map;
				}
			}
			/*map.put("code", "-2");
			map.put("status", "false");
			map.put("message", "您的上级还不是代理人！");
			map.put("data", "");
			return map;*/
		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "此推荐码不可用！");
		map.put("data", "");
		return map;
	}

	
	@Override
	public Map<String, String> insertJBonusFdj() {
		Map<String, String> map = new HashMap<String, String>();
		List<UserTotalAmount> taList = ordersBMapper.getSuccessOrder();
		for (UserTotalAmount userTotalAmount : taList) {
			Integer uId = userTotalAmount.getId();
			Double amount = userTotalAmount.getAmount();
			List<UserBean> userList = userMapper.getUserNameById(uId);
			if (userList != null && userList.size() > 0) {
				UserBean userBean = userList.get(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				UUserRRecordBExample example = new UUserRRecordBExample();
				Criteria criteria = example.createCriteria();
				criteria.andUIdEqualTo(uId);
				criteria.andStatusEqualTo(1);
				List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					UUserRRecordB userRecord = list.get(0);
					List<UserBean> nameById = userMapper.getUserNameById(userRecord.getrId());
					if (nameById != null && nameById.size() > 0) {
						UserBean userBean1 = nameById.get(0);
						int pLevel = userBean1.getIsChuangke();
						JBonusFdj bonusFdj = new JBonusFdj();
						bonusFdj.setuId(uId);
						bonusFdj.setParentId(userRecord.getrId());
						bonusFdj.setAmount(amount);
						bonusFdj.setStatus(1);
						bonusFdj.setCreated(sdf.format(new Date()));
						bonusFdj.setuName(userBean.getRealName());
						if (pLevel == 4) {
							bonusFdj.setParentMoney(amount * 0.06);
							jBonusFdjMapper.insert(bonusFdj);
						} else if (pLevel == 3) {
							bonusFdj.setParentMoney(amount * 0.04);
							jBonusFdjMapper.insert(bonusFdj);

							UUserRRecordBExample example1 = new UUserRRecordBExample();
							Criteria criteria1 = example1.createCriteria();
							criteria1.andUIdEqualTo(userRecord.getrId());
							criteria1.andStatusEqualTo(1);
							List<UUserRRecordB> list1 = userRecordMapper.selectByExample(example1);
							if (list1 != null && list1.size() > 0) {
								UUserRRecordB userRecord1 = list1.get(0);
								/*List<UserBean> nameById1 = userMapper
										.getUserNameById(userRecord1.getrId());
								if (nameById1 != null && nameById1.size() > 0) {
									UserBean userBean2 = nameById1.get(0);*/
									JBonusFdj bonusFdj1 = new JBonusFdj();
									bonusFdj1.setuId(userRecord.getrId());
									bonusFdj1.setParentId(userRecord1.getrId());
									bonusFdj1.setAmount(amount);
									bonusFdj1.setStatus(1);
									bonusFdj1.setCreated(sdf.format(new Date()));
									bonusFdj1.setuName(userBean1.getRealName());
									bonusFdj1.setParentMoney(amount * 0.02);
									jBonusFdjMapper.insert(bonusFdj1);
							//	}
							}

						} else if (pLevel == 2) {
							bonusFdj.setParentMoney(amount * 0.02);
							jBonusFdjMapper.insert(bonusFdj);

							UUserRRecordBExample example1 = new UUserRRecordBExample();
							Criteria criteria1 = example1.createCriteria();
							criteria1.andUIdEqualTo(userRecord.getrId());
							criteria1.andStatusEqualTo(1);
							List<UUserRRecordB> list1 = userRecordMapper
									.selectByExample(example1);
							if (list1 != null && list1.size() > 0) {
								UUserRRecordB userRecord1 = list1.get(0);
								List<UserBean> nameById1 = userMapper.getUserNameById(userRecord1.getrId());
								if (nameById1 != null && nameById1.size() > 0) {
									UserBean userBean2 = nameById1.get(0);
									int pLevel1 = userBean2.getIsChuangke();
									if (pLevel1 == 3 || pLevel1 == 4) {
										JBonusFdj bonusFdj1 = new JBonusFdj();
										bonusFdj1.setuId(userRecord.getrId());
										bonusFdj1.setParentId(userRecord1.getrId());
										bonusFdj1.setAmount(amount);
										bonusFdj1.setStatus(1);
										bonusFdj1.setCreated(sdf.format(new Date()));
										bonusFdj1.setuName(userBean1.getRealName());
										bonusFdj1.setParentMoney(amount * 0.04);
										jBonusFdjMapper.insert(bonusFdj1);
									} else if (pLevel1 == 2) {
										JBonusFdj bonusFdj1 = new JBonusFdj();
										bonusFdj1.setuId(userRecord.getrId());
										bonusFdj1.setParentId(userRecord1.getrId());
										bonusFdj1.setAmount(amount);
										bonusFdj1.setStatus(1);
										bonusFdj1.setCreated(sdf.format(new Date()));
										bonusFdj1.setuName(userBean1.getRealName());
										bonusFdj1.setParentMoney(amount * 0.02);
										jBonusFdjMapper.insert(bonusFdj1);

										UUserRRecordBExample example2 = new UUserRRecordBExample();
										Criteria criteria2 = example2.createCriteria();
										criteria2.andUIdEqualTo(userRecord1.getrId());
										criteria2.andStatusEqualTo(1);
										List<UUserRRecordB> list2 = userRecordMapper.selectByExample(example2);
										if (list2 != null && list2.size() > 0) {
											UUserRRecordB userRecord2 = list2.get(0);
											/*List<UserBean> nameById2 = userMapper.getUserNameById(userRecord2.getrId());
											if (nameById2 != null && nameById2.size() > 0) {
												UserBean userBean3 = nameById2.get(0);*/
												JBonusFdj bonusFdj2 = new JBonusFdj();
												bonusFdj2.setuId(userRecord1.getrId());
												bonusFdj2.setParentId(userRecord2.getrId());
												bonusFdj2.setAmount(amount);
												bonusFdj2.setStatus(1);
												bonusFdj2.setCreated(sdf.format(new Date()));
												bonusFdj2.setuName(userBean2.getRealName());
												bonusFdj2.setParentMoney(amount * 0.02);
												jBonusFdjMapper.insert(bonusFdj2);
										//	}

										}

									}

								}
							}

						}
					}
				}

			}
		}
		map.put("message", "辅导奖计算成功！"+new Date());
		return map;
	}
	
	
	/**
	 * 管理奖
	 */
	@Override
	public Map<String,String>  insertJBonusGlj(String uuid){      
		Map<String, String> map = new HashMap<String, String>();
		List<UserBean> userList = userMapper.getUserByUuid(uuid);
		UserBean userBean = userList.get(0); // 获取被推荐人信息
		int isAuth = userBean.getIsAuth();
		int uLevel = userBean.getIsChuangke();
		if(isAuth == 2 && (uLevel == 2 || uLevel == 3 || uLevel == 4)){
			userRecordMapper.updateByUid(userBean.getId(), 1);
			
			/*try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				map.put("message","请稍后再试！");
			}*/
			
			UUserRRecordBExample example = new UUserRRecordBExample();
			Criteria criteria = example.createCriteria();
			criteria.andUIdEqualTo(userBean.getId());
			criteria.andStatusEqualTo(1);
			List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
			if(list != null && list.size()>0){
				UUserRRecordB userRecord = list.get(0);
				
				List<UserBean> nameById = userMapper.getUserNameById(userRecord.getrId());
				if(nameById != null && nameById.size()>0){
					UserBean userBean1 = nameById.get(0);
					int pLevel = userBean1.getIsChuangke();
					
					JBonusGlj bonusGlj = new JBonusGlj();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bonusGlj.setuId(userBean.getId());
					bonusGlj.setuName(userBean.getRealName());
					bonusGlj.setParentId(userRecord.getrId());
					bonusGlj.setStatus(1);
					bonusGlj.setCreated(sdf.format(new Date()));
					if (pLevel == 4) {
						bonusGlj.setParentMoney(120.00);
						bonusGljMapper.insert(bonusGlj);

					} else if(pLevel == 3) {
						bonusGlj.setParentMoney(80.00);
						bonusGljMapper.insert(bonusGlj);
						
						UUserRRecordBExample example1 = new UUserRRecordBExample();
						Criteria criteria1 = example1.createCriteria();
						criteria1.andUIdEqualTo(userRecord.getrId());
						criteria1.andStatusEqualTo(1);
						List<UUserRRecordB> list1 = userRecordMapper.selectByExample(example1);
						if(list1 != null && list1.size()>0){
							UUserRRecordB userRecord1 = list1.get(0);
							/*List<UserBean> nameById1 = userMapper.getUserNameById(userRecord1.getrId());
							if(nameById1 != null && nameById1.size()>0){
								UserBean userBean2 = nameById1.get(0);*/
								
								JBonusGlj bonusGlj1 = new JBonusGlj();
								bonusGlj1.setuId(userRecord1.getuId());
								bonusGlj1.setuName(userBean1.getRealName());
								bonusGlj1.setParentId(userRecord1.getrId());
								bonusGlj1.setParentMoney(40.00);
								bonusGlj1.setStatus(1);
								bonusGlj1.setCreated(sdf.format(new Date()));
								bonusGljMapper.insert(bonusGlj1);
							//}
						}
					}else if(pLevel == 2){
						bonusGlj.setParentMoney(40.00);
						bonusGljMapper.insert(bonusGlj);
						
						UUserRRecordBExample example1 = new UUserRRecordBExample();
						Criteria criteria1 = example1.createCriteria();
						criteria1.andUIdEqualTo(userRecord.getrId());
						criteria1.andStatusEqualTo(1);
						List<UUserRRecordB> list1 = userRecordMapper.selectByExample(example1);
						if(list1 != null && list1.size()>0){
							UUserRRecordB userRecord1 = list1.get(0);
							List<UserBean> nameById1 = userMapper.getUserNameById(userRecord1.getrId());
							if(nameById1 != null && nameById1.size()>0){
								UserBean userBean2 = nameById1.get(0);
								int level1 = userBean2.getIsChuangke();
								if(level1 == 3 || level1 == 4){
									JBonusGlj bonusGlj1 = new JBonusGlj();
									bonusGlj1.setuId(userRecord1.getuId());
									bonusGlj1.setuName(userBean1.getRealName());
									bonusGlj1.setParentId(userRecord1.getrId());
									bonusGlj1.setParentMoney(80.00);
									bonusGlj1.setStatus(1);
									bonusGlj1.setCreated(sdf.format(new Date()));
									bonusGljMapper.insert(bonusGlj1);
								}else if(level1 == 2){
									JBonusGlj bonusGlj1 = new JBonusGlj();
									bonusGlj1.setuId(userRecord1.getuId());
									bonusGlj1.setuName(userBean1.getRealName());
									bonusGlj1.setParentId(userRecord1.getrId());
									bonusGlj1.setParentMoney(40.00);
									bonusGlj1.setStatus(1);
									bonusGlj1.setCreated(sdf.format(new Date()));
									bonusGljMapper.insert(bonusGlj1);
									
									UUserRRecordBExample example2 = new UUserRRecordBExample();
									Criteria criteria2 = example2.createCriteria();
									criteria2.andUIdEqualTo(userRecord1.getrId());
									criteria2.andStatusEqualTo(1);
									List<UUserRRecordB> list2 = userRecordMapper.selectByExample(example2);
									if (list2 != null && list2.size() > 0){
										UUserRRecordB userRecord2 = list2.get(0);
										JBonusGlj bonusGlj2 = new JBonusGlj();
										bonusGlj2.setuId(userRecord2.getuId());
										bonusGlj2.setuName(userBean2.getRealName());
										bonusGlj2.setParentId(userRecord2.getrId());
										bonusGlj2.setParentMoney(40.00);
										bonusGlj2.setStatus(1);
										bonusGlj2.setCreated(sdf.format(new Date()));
										bonusGljMapper.insert(bonusGlj2);
									}
								}
							}
							
						}
					}
				}
				
			}
			map.put("messgae", "请重新提交！");
		}
		map.put("messgae", "审核成功！");
		return map;
		
		
	}
	

}
