package com.jyss.yqy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFhj;
import com.jyss.yqy.entity.JBonusResult;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusFhjMapper;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusFPService;
import com.jyss.yqy.service.JBonusFhjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class JBonusFhjServiceImpl implements JBonusFhjService {

	@Autowired
	private JBonusFhjMapper jBonusFhjMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private JBonusFPService jBonusFPService;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;

	

	/**
	 * 查询本周（当日）
	 */
	@Override
	public JBonusResult getJBonusFhj(Integer uId) {
		float earnings = jBonusFhjMapper.selectJBonusFhjToday(uId);
		float total = jBonusFhjMapper.selectTotal(uId);
		List<JBonusFhj> list = jBonusFhjMapper.selectJBonusFhjWek(uId);

		JBonusResult result = new JBonusResult();
		result.setEarnings(earnings);
		result.setTotal(total);
		result.setData(list);
		return result;
	}


	/**
	 * 两个日期查询
	 */
	@Override
	public JBonusResult selectJBonusFhjByDay(Integer uId, int page, int limit,
											 String beginTime, String endTime) {
		float total = jBonusFhjMapper.selectFhjTotalByDay(uId, beginTime, endTime);

		PageHelper.startPage(page, limit);
		List<JBonusFhj> list = jBonusFhjMapper.selectJBonusFhjByDay(uId, beginTime, endTime);
		PageInfo<JBonusFhj> pageInfo = new PageInfo<JBonusFhj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0.0f);
		result.setTotal(total);
		result.setData(list);
		return result;

	}


	/**
	 * 月份查询
	 */
	@Override
	public JBonusResult selectJBonusFhjByMonth(Integer uId, int page, int limit, String month) {
		float total = jBonusFhjMapper.selectFhjTotalByMonth(uId, month);

		PageHelper.startPage(page, limit);
		List<JBonusFhj> list = jBonusFhjMapper.selectJBonusFhjByMonth(uId, month);
		PageInfo<JBonusFhj> pageInfo = new PageInfo<JBonusFhj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0.0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}


	/**
	 * 计算分红奖
	 */
	@Override
	public Map<String, String> insertJBonusFhj() {

		Xtcl xtcl1 = xtclMapper.getClsValue("fhqbs_type", "5");      //分红奖比例
		float float1 = Float.parseFloat(xtcl1.getBz_value());        			  //0.003

		//查询所有分红权用户
		List<UserBean> userBeans = userMapper.selectUserByFHJ();
		for (UserBean userBean : userBeans) {
			Float totalPv = userBean.getTotalPv();
			float money = totalPv * float1;
			//添加分红奖
			JBonusFhj bonusFhj = new JBonusFhj();
			bonusFhj.setuId(userBean.getId());
			bonusFhj.setAmount(money);
			bonusFhj.setBalance(totalPv - money);
			bonusFhj.setStatus(1);
			int count = jBonusFhjMapper.insert(bonusFhj);
			if(count == 1){
				//计算积分
				jBonusFPService.insertScoreBalance(userBean.getId(),money,4);
			}
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", "分红奖和积分计算完成时间："+new Date());
		return map;
	}


	/**
	 * 扣除借贷金额
	 */
	@Override
	public Map<String, String> updateUserBorrow() {
		List<UserBean> userBeans = userMapper.selectUserBorrow();
		for (UserBean userBean : userBeans) {
			float cashScore = userBean.getCashScore();
			Float borrow = userBean.getBorrow();
			if(cashScore > 0){
				if(cashScore <= borrow){
					//添加股券
					ScoreBalance score = new ScoreBalance();
					score.setEnd(2);
					score.setuUuid(userBean.getUuid());
					score.setCategory(11);
					score.setType(2);
					score.setScore(cashScore);
					score.setJyScore(0f);
					score.setStatus(1);
					int count = scoreBalanceMapper.addCashScore(score);
					if(count == 1){
						UserBean userBean1 = new UserBean();
						userBean1.setId(userBean.getId());
						userBean1.setCashScore(0f);
						userBean1.setBorrow(borrow - cashScore);
						userMapper.updateScore(userBean1);
					}
				}else{
					//添加股券
					ScoreBalance score = new ScoreBalance();
					score.setEnd(2);
					score.setuUuid(userBean.getUuid());
					score.setCategory(11);
					score.setType(2);
					score.setScore(borrow);
					score.setJyScore(cashScore - borrow);
					score.setStatus(1);
					int count = scoreBalanceMapper.addCashScore(score);
					if(count == 1){
						UserBean userBean1 = new UserBean();
						userBean1.setId(userBean.getId());
						userBean1.setCashScore(cashScore - borrow);
						userBean1.setBorrow(0f);
						userMapper.updateScore(userBean1);
					}
				}
			}
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", "扣除借贷金额和积分计算完成时间："+new Date());
		return map;
	}


}
