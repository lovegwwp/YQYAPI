package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JBonusScjResult;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusScjService;



@Service
@Transactional
public class JBonusScjServiceImpl implements JBonusScjService{
	
	@Autowired
	private JBonusScjMapper bonusScjMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private XtclMapper xtclMapper;
	
	
	/**
	 * 查询用户市场奖
	 */
	@Override
	public JBonusScjResult selectJBonusScjByUid(int uId,int page,int limit) {
		JBonusScjResult result = new JBonusScjResult();
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByUid(uId);
		JBonusScj bonusScj = bonusScjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		//设置市场奖总金额
		float totalPv = bonusScjMapper.selectTotalPv(uId);
		result.setTotal(totalPv);
		//设置本周明细
		PageHelper.startPage(page, limit);
		List<JBonusScj> scjList = bonusScjMapper.selectJBonusScjWek(uId);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(scjList);
		result.setData(scjList);
		return result;
		
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByDay(int uId,int page,int limit,String beginTime,String endTime) {
		JBonusScjResult result = new JBonusScjResult();
		PageHelper.startPage(page, limit);
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByDay(uId, beginTime, endTime);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(bonusScjList);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByDay(uId, beginTime, endTime);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByMonth(int uId,int page,int limit,String month) {
		JBonusScjResult result = new JBonusScjResult();
		PageHelper.startPage(page, limit);
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByMonth(uId, month);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(bonusScjList);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByMonth(uId, month);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}
	

}
