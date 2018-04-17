package com.jyss.yqy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyss.yqy.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.UUserRRecordBExample.Criteria;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UUserRRecordBMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.UserRecordBService;

@Service
@Transactional
public class UserRecordBServiceImpl implements UserRecordBService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UUserRRecordBMapper userRecordMapper;
	@Autowired
	private OrdersBMapper ordersBMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;


	/**
	 * 推荐关系绑定
	 * @param uuid
	 * @param bCode
	 * @return
	 */
	@Override
	public Map<String, String> insertUserRecordB(String uuid, String bCode) {
		Map<String, String> map = new HashMap<String, String>();
		List<UserBean> userList = userMapper.getUserByUuid(uuid);
		UserBean userBean = userList.get(0);           // 获取被推荐人信息
		//int isAuth = userBean.getIsAuth();
		//int uLevel = userBean.getIsChuangke();

		List<UserBean> parentList = userMapper.getUserByBCode(bCode);
		if (parentList != null && parentList.size() > 0) {
			UserBean parentUser = parentList.get(0);     // 获取推荐人信息
			int pLevel = parentUser.getIsChuangke();
			// 推荐关系表		
			if (pLevel == 2 || pLevel == 3 || pLevel == 4 || pLevel == 5) {
				UUserRRecordBExample example = new UUserRRecordBExample();
				Criteria criteria = example.createCriteria();
				criteria.andUIdEqualTo(userBean.getId());
				List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
				if (list != null && list.size() == 1) {
					UUserRRecordB userRRecordB = list.get(0);
					userRRecordB.setrId(parentUser.getId());
					userRRecordB.setType(pLevel);
					userRRecordB.setCreatedAt(new Date());
					int count = userRecordMapper.updateByPrimaryKeySelective(userRRecordB);
					if(count == 1){
						map.put("code", "0");
						map.put("status", "true");
						map.put("message", "推荐码使用成功！");
						map.put("data", "");
						return map;
					}
					map.put("code", "-4");
					map.put("status", "false");
					map.put("message", "推荐码使用失败！");
					map.put("data", "");
					return map;
				}
				UUserRRecordB userRRecordB = new UUserRRecordB();
				userRRecordB.setuId(userBean.getId());
				userRRecordB.setrId(parentUser.getId());
				userRRecordB.setStatus(1); // 设置初始值为0
				userRRecordB.setType(pLevel);
				userRRecordB.setCreatedAt(new Date());
				int idNum = userRecordMapper.insert(userRRecordB);
				if (idNum == 1){
					map.put("code", "0");
					map.put("status", "true");
					map.put("message", "推荐码使用成功！");
					map.put("data", "");
					return map;
				}
				map.put("code", "-4");
				map.put("status", "false");
				map.put("message", "推荐码使用失败！");
				map.put("data", "");
				return map;
			}
		}
		map.put("code", "-1");
		map.put("status", "false");
		map.put("message", "未实名");
		map.put("data", "");
		return map;
	}


	/**
	 * 辅导奖
	 * @return
	 */
	/*@Override
	public Map<String, String> insertJBonusFdj() {
		Map<String, String> map = new HashMap<String, String>();
		List<UserTotalAmount> taList = ordersBMapper.getSuccessOrder();
		for (UserTotalAmount userTotalAmount : taList) {
			Integer uId = userTotalAmount.getId();
			Double amount = userTotalAmount.getAmount();

			//计算辅导奖
			computeJBonusFdj(uId,amount);

		}

		// 计算现金积分和购物积分
		List<JBonusFdj> bonusFdjList = jBonusFdjMapper.selectEveryDayEarnings();
		if (bonusFdjList != null && bonusFdjList.size() > 0) {
			for (JBonusFdj jBonusFdj : bonusFdjList) {

				// 查询积分比例
				Xtcl xtcl1 = xtclMapper.getClsValue("jjbl_type", "xj"); // 现金积分比例
				double double1 = Double.parseDouble(xtcl1.getBz_value()); // 0.7
				Xtcl xtcl2 = xtclMapper.getClsValue("jjbl_type", "gw"); // 购物积分比例
				double double2 = Double.parseDouble(xtcl2.getBz_value()); // 0.2

				List<UserBean> userList = userMapper.getUserScoreById(jBonusFdj.getParentId());
				if (userList != null && userList.size() > 0) {
					Double money = jBonusFdj.getParentMoney();
					UserBean userBean = userList.get(0);
					Float totalPv = userBean.getTotalPv();
					if(totalPv > 0){                           //pv余额大于等于0计算
						if(money <= totalPv){                  //小于等于余额pv
							// 添加现金积分
							ScoreBalance score1 = new ScoreBalance();
							score1.setEnd(2);
							score1.setuUuid(userBean.getUuid());
							score1.setCategory(5);
							score1.setType(1);
							score1.setScore((float) (money * double1));
							score1.setJyScore((float) (money * double1) + userBean.getCashScore());
							// score1.setCreatedAt(new Date());
							score1.setStatus(1);
							int count1 = scoreBalanceMapper.addCashScore(score1);

							ScoreBalance score2 = new ScoreBalance();
							score2.setEnd(2);
							score2.setuUuid(userBean.getUuid());
							score2.setCategory(5);
							score2.setType(1);
							score2.setScore((float) (money * double2));
							score2.setJyScore((float) (money * double2)+ userBean.getShoppingScore());
							// score2.setCreatedAt(new Date());
							score2.setStatus(1);
							int count2 = scoreBalanceMapper.addShoppingScore(score2);

							if (count1 == 1 && count2 == 1) {
								UserBean userBean2 = new UserBean();
								userBean2.setId(jBonusFdj.getParentId());
								userBean2.setCashScore((float) (money * double1) + userBean.getCashScore());
								userBean2.setShoppingScore((float) (money * double2) + userBean.getShoppingScore());
								userBean2.setTotalPv((float) (totalPv - money));
								userMapper.updateScore(userBean2);
							}

						}else {                                        //大于余额pv
							// 添加现金积分
							ScoreBalance score1 = new ScoreBalance();
							score1.setEnd(2);
							score1.setuUuid(userBean.getUuid());
							score1.setCategory(5);
							score1.setType(1);
							score1.setScore((float) (totalPv * double1));
							score1.setJyScore((float) (totalPv * double1) + userBean.getCashScore());
							// score1.setCreatedAt(new Date());
							score1.setStatus(1);
							int count1 = scoreBalanceMapper.addCashScore(score1);

							ScoreBalance score2 = new ScoreBalance();
							score2.setEnd(2);
							score2.setuUuid(userBean.getUuid());
							score2.setCategory(5);
							score2.setType(1);
							score2.setScore((float) (totalPv * double2));
							score2.setJyScore((float) (totalPv * double2)+ userBean.getShoppingScore());
							// score2.setCreatedAt(new Date());
							score2.setStatus(1);
							int count2 = scoreBalanceMapper.addShoppingScore(score2);

							if (count1 == 1 && count2 == 1) {
								UserBean userBean2 = new UserBean();
								userBean2.setId(jBonusFdj.getParentId());
								userBean2.setCashScore((float) (totalPv * double1) + userBean.getCashScore());
								userBean2.setShoppingScore((float) (totalPv * double2) + userBean.getShoppingScore());
								userBean2.setTotalPv(totalPv - totalPv);
								userMapper.updateScore(userBean2);
							}
						}
					}
				}
			}
		}
		map.put("message", "辅导奖和积分计算完成时间：" + new Date());
		return map;
	}


	*//**
	 * 计算辅导奖
	 * @param uId
	 *//*
	private void computeJBonusFdj(int uId,double amount){
		//查询关系表
		UUserRRecordBExample example = new UUserRRecordBExample();
		Criteria criteria = example.createCriteria();
		criteria.andUIdEqualTo(uId);
		criteria.andStatusEqualTo(1);
		List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
		if(list != null && list.size()>0){
			UUserRRecordB userRecord = list.get(0);
			Integer pLevel = userRecord.getType();    //父级代理级别

			// 查询返回比例
			Xtcl xtcl1 = xtclMapper.getClsValue("fdj_type", "1");    // 初级获得金额
			double double1 = Double.parseDouble(xtcl1.getBz_value());             // 0.02
			Xtcl xtcl2 = xtclMapper.getClsValue("fdj_type", "2");    // 中级获得金额
			double double2 = Double.parseDouble(xtcl2.getBz_value());             // 0.04
			Xtcl xtcl3 = xtclMapper.getClsValue("fdj_type", "3");    // 高级获得金额
			double double3 = Double.parseDouble(xtcl3.getBz_value());             // 0.06
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			//经理人
			if(pLevel == 5){
				computeJBonusFdj(userRecord.getrId(),amount);

			//高级代理人，结束
			}else if(pLevel == 4){
				List<UserBean> userList = userMapper.getUserNameById(userRecord.getuId());
				if(userList != null && userList.size()>0){
					UserBean userBean = userList.get(0);
					JBonusFdj bonusFdj = new JBonusFdj();
					bonusFdj.setuId(userBean.getId());
					bonusFdj.setParentId(userRecord.getrId());
					bonusFdj.setAmount(amount);
					bonusFdj.setStatus(1);
					// bonusFdj.setCreated(sdf.format(new Date()));
					bonusFdj.setuName(userBean.getRealName());
					bonusFdj.setParentMoney(amount * double3);
					jBonusFdjMapper.insert(bonusFdj);
				}

			//中级代理人
			}else if(pLevel == 3){
				List<UserBean> userList = userMapper.getUserNameById(userRecord.getuId());
				if(userList != null && userList.size()>0){
					UserBean userBean = userList.get(0);
					JBonusFdj bonusFdj = new JBonusFdj();
					bonusFdj.setuId(userBean.getId());
					bonusFdj.setParentId(userRecord.getrId());
					bonusFdj.setAmount(amount);
					bonusFdj.setStatus(1);
					bonusFdj.setuName(userBean.getRealName());
					bonusFdj.setParentMoney(amount * double2);
					jBonusFdjMapper.insert(bonusFdj);
					//递归到高级代理人，结束
					UUserRRecordB userRecord1 = selectGjDlr(userRecord.getrId(), 4);
					if(userRecord1 != null){
						List<UserBean> userList1 = userMapper.getUserNameById(userRecord1.getuId());
						if(userList1 != null && userList1.size()>0){
							UserBean userBean1 = userList1.get(0);
							JBonusFdj bonusFdj1 = new JBonusFdj();
							bonusFdj1.setuId(userBean1.getId());
							bonusFdj1.setParentId(userRecord1.getrId());
							bonusFdj1.setAmount(amount);
							bonusFdj1.setStatus(1);
							bonusFdj1.setuName(userBean1.getRealName());
							bonusFdj1.setParentMoney(amount * (double3 - double2));
							jBonusFdjMapper.insert(bonusFdj1);
						}
					}
				}

			//初级代理人
			}else if(pLevel == 2){
				List<UserBean> userList = userMapper.getUserNameById(userRecord.getuId());
				if(userList != null && userList.size()>0){
					UserBean userBean = userList.get(0);
					JBonusFdj bonusFdj = new JBonusFdj();
					bonusFdj.setuId(userBean.getId());
					bonusFdj.setParentId(userRecord.getrId());
					bonusFdj.setAmount(amount);
					bonusFdj.setStatus(1);
					bonusFdj.setuName(userBean.getRealName());
					bonusFdj.setParentMoney(amount * double1);
					jBonusFdjMapper.insert(bonusFdj);

					UUserRRecordB userRecord1 = selectGjDlr(userRecord.getrId(), 3);
					if(userRecord1 != null){
						Integer type = userRecord1.getType();
						//递归到高级代理人，结束
						if(type == 4){
							List<UserBean> userList1 = userMapper.getUserNameById(userRecord1.getuId());
							if(userList1 != null && userList1.size()>0){
								UserBean userBean1 = userList1.get(0);
								JBonusFdj bonusFdj1 = new JBonusFdj();
								bonusFdj1.setuId(userBean1.getId());
								bonusFdj1.setParentId(userRecord1.getrId());
								bonusFdj1.setAmount(amount);
								bonusFdj1.setStatus(1);
								bonusFdj1.setuName(userBean1.getRealName());
								bonusFdj1.setParentMoney(amount * (double3 - double1));
								jBonusFdjMapper.insert(bonusFdj1);
							}
						//递归到中级代理人
						}else if(type == 3){
							List<UserBean> userList1 = userMapper.getUserNameById(userRecord1.getuId());
							if(userList1 != null && userList1.size()>0){
								UserBean userBean1 = userList1.get(0);
								JBonusFdj bonusFdj1 = new JBonusFdj();
								bonusFdj1.setuId(userBean1.getId());
								bonusFdj1.setParentId(userRecord1.getrId());
								bonusFdj1.setAmount(amount);
								bonusFdj1.setStatus(1);
								bonusFdj1.setuName(userBean1.getRealName());
								bonusFdj1.setParentMoney(amount * (double2 - double1));
								jBonusFdjMapper.insert(bonusFdj1);
								//递归到高级代理人，结束
								UUserRRecordB userRecord2 = selectGjDlr(userRecord.getrId(), 4);
								if(userRecord2 != null){
									List<UserBean> userList2 = userMapper.getUserNameById(userRecord2.getuId());
									if(userList2 != null && userList2.size()>0){
										double gjDlr = double3 - (double2 - double1) - double1;
										UserBean userBean2 = userList2.get(0);
										JBonusFdj bonusFdj2 = new JBonusFdj();
										bonusFdj2.setuId(userBean2.getId());
										bonusFdj2.setParentId(userRecord2.getrId());
										bonusFdj2.setAmount(amount);
										bonusFdj2.setStatus(1);
										bonusFdj2.setuName(userBean2.getRealName());
										bonusFdj2.setParentMoney(amount * gjDlr);
										jBonusFdjMapper.insert(bonusFdj2);
									}
								}
							}
						}
					}
				}
			}
		}
	}*/


	/**
	 * 递归中高级代理
	 * @param uId
	 * @param type
	 * @return
	 */
	private UUserRRecordB selectGjDlr(int uId,int type){
		UUserRRecordBExample example = new UUserRRecordBExample();
		Criteria criteria = example.createCriteria();
		criteria.andUIdEqualTo(uId);
		criteria.andStatusEqualTo(1);
		List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
		if(list != null && list.size()>0){
			UUserRRecordB userRecord = list.get(0);
			Integer type1 = userRecord.getType();
			if(type <= type1 && type1 < 5 ){
				return userRecord;
			}else{
				return selectGjDlr(userRecord.getrId(),type);
			}
		}
		return null;
	}


	@Override
	public List<UUserRRecordB> getRecordB(String uId, String rId, String status) {
		// TODO Auto-generated method stub
		return userRecordMapper.getRecordB(uId, rId, status);
	}

	@Override
	public int updateTypeByUid(@Param("type") String type, @Param("rId") String rId, @Param("status") String status) {
		return userRecordMapper.updateTypeByUid(type, rId, status);
	}

	@Override
	public List<UUserRRecordB> getRecordBGroupByRid() {
		return userRecordMapper.getRecordBGroupByRid();
	}

}
