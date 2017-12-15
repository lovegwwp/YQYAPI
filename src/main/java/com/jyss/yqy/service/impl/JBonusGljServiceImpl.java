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
import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljExample;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.JBonusGljExample.Criteria;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.entity.JBonusGljResult;
import com.jyss.yqy.mapper.JBonusGljMapper;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusGljService;

@Service
@Transactional
public class JBonusGljServiceImpl implements JBonusGljService{
	
	@Autowired
	private JBonusGljMapper jBonusGljMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;
	
	
	/**
	 * 查询用户管理奖
	 */
	@Override
	public JBonusGljResult getJBonusGlj(int uId,int page,int limit){
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		double earnings = jBonusGljMapper.selectEarnings(uId);
		double total = jBonusGljMapper.selectTotal(uId);
		PageHelper.startPage(page, limit);
		List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljWek(uId);
		PageInfo<JBonusGlj> pageInfo = new PageInfo<JBonusGlj>(list);
		jBonusGljResult.setEarnings(earnings);
		jBonusGljResult.setTotal(total);
		jBonusGljResult.setData(list);
		return jBonusGljResult;
	}


	@Override
	public JBonusGljResult selectJBonusGljByDay(int uId,int page,int limit,String beginTime,String endTime) {
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		double totalByDay = jBonusGljMapper.selectGljTotalByDay(uId, beginTime, endTime);
		PageHelper.startPage(page, limit);
		List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljByDay(uId, beginTime, endTime);
		PageInfo<JBonusGlj> pageInfo = new PageInfo<JBonusGlj>(list);
		jBonusGljResult.setEarnings(null);
		jBonusGljResult.setTotal(totalByDay);
		jBonusGljResult.setData(list);
		return jBonusGljResult;
	}


	@Override
	public JBonusGljResult selectJBonusGljByMonth(int uId,int page,int limit,String month) {
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		double totalByMonth = jBonusGljMapper.selectGljTotalByMonth(uId, month);
		PageHelper.startPage(page, limit);
		List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljByMonth(uId, month);
		PageInfo<JBonusGlj> pageInfo = new PageInfo<JBonusGlj>(list);
		jBonusGljResult.setEarnings(null);
		jBonusGljResult.setTotal(totalByMonth);
		jBonusGljResult.setData(list);
		return jBonusGljResult;
	}
	
	
	
	/**
	 * 计算现金积分和购物积分
	 */
	@Override
	public Map<String,String> insertScore(){
		Map<String, String> map = new HashMap<String,String>();
		
		List<JBonusGlj> bonusGljList = jBonusGljMapper.selectEveryDayEarnings();
		if(bonusGljList != null && bonusGljList.size()>0){
			for (JBonusGlj jBonusGlj : bonusGljList) {
				
				//查询积分比例
				Xtcl xtcl1 = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
				double double1 = Double.parseDouble(xtcl1.getBz_value());    //0.7
				Xtcl xtcl2 = xtclMapper.getClsValue("jjbl_type", "gw");      //购物积分比例
				double double2 = Double.parseDouble(xtcl2.getBz_value());    //0.2
				
				List<UserBean> userList = userMapper.getUserScoreById(jBonusGlj.getParentId());
				if(userList != null && userList.size()>0){
					Double money = jBonusGlj.getParentMoney();
					UserBean userBean = userList.get(0);
					//添加现金积分
					ScoreBalance score1 = new ScoreBalance();
					score1.setEnd(2);
					score1.setuUuid(userBean.getUuid());
					score1.setCategory(4);
					score1.setType(1);
					score1.setScore((float)(money * double1));
					score1.setJyScore((float)(money * double1)+ userBean.getCashScore());
					score1.setCreatedAt(new Date());
					score1.setStatus(1);
					int count1 = scoreBalanceMapper.addCashScoreBalance(score1);
					
					ScoreBalance score2 = new ScoreBalance();
					score2.setEnd(2);
					score2.setuUuid(userBean.getUuid());
					score2.setCategory(4);
					score2.setType(1);
					score2.setScore((float)(money * double2));
					score2.setJyScore((float)(money * double2) + userBean.getShoppingScore());
					score2.setCreatedAt(new Date());
					score2.setStatus(1);
					int count2 = scoreBalanceMapper.addShoppingScoreBalance(score2);
					
					if(count1 > 0 && count2 > 0){
						UserBean userBean2 = new UserBean();
						userBean2.setId(jBonusGlj.getParentId());
						userBean2.setCashScore((float)(money * double1)+ userBean.getCashScore());
						userBean2.setShoppingScore((float)(money * double2) + userBean.getShoppingScore());
						userMapper.updateScore(userBean2);
					}
				}
				
			}
			
		}
		map.put("message", "管理奖积分计算时间："+new Date());
		return map;
	}
	
	

}
