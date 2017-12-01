package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JBonusScjResult;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.service.JBonusScjService;



@Service
@Transactional
public class JBonusScjServiceImpl implements JBonusScjService{
	
	@Autowired
	private JBonusScjMapper bonusScjMapper;
	@Autowired
	private UserMapper userMapper;
	
	
	/**
	 * 查询用户市场奖
	 */
	@Override
	public JBonusScjResult selectJBonusScjByUid(int uId) {
		
		JBonusScjResult result = new JBonusScjResult();
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByUid(uId);
		JBonusScj bonusScj = bonusScjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		//设置市场奖总金额
		float totalPv = bonusScjMapper.selectTotalPv(uId);
		List<UserBean> userList = userMapper.getUserNameById(uId);
		UserBean userBean = userList.get(0);
		int level = userBean.getIsChuangke();
		if(level == 2){
			result.setTotal((totalPv*0.12f) <= 3000.00f ? (float)(Math.round((totalPv*0.12f)*100))/100 : 3000.00f);
		}else if(level == 3){
			result.setTotal((totalPv*0.16f) <= 6000.00f ? (float)(Math.round((totalPv*0.16f)*100))/100 : 6000.00f);
		}else if(level == 4){
			result.setTotal((totalPv*0.22f) <= 9000.00f ? (float)(Math.round((totalPv*0.22f)*100))/100 : 9000.00f);
		}
		//设置本周明细
		List<JBonusScj> scjList = bonusScjMapper.selectJBonusScjWek(uId);
		result.setData(scjList);
		return result;
		
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByDay(int uId, String beginTime,String endTime) {
		JBonusScjResult result = new JBonusScjResult();
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByDay(uId, beginTime, endTime);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByDay(uId, beginTime, endTime);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByMonth(int uId, String month) {
		JBonusScjResult result = new JBonusScjResult();
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByMonth(uId, month);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByMonth(uId, month);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}
	

}
