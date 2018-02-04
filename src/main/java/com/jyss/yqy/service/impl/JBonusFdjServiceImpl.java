package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjExample;
import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusFdjExample.Criteria;
import com.jyss.yqy.entity.JBonusFdjResult;
import com.jyss.yqy.mapper.JBonusFdjMapper;
import com.jyss.yqy.service.JBonusFdjService;


@Service
@Transactional
public class JBonusFdjServiceImpl implements JBonusFdjService{
	
	@Autowired
	private JBonusFdjMapper jBonusFdjMapper;
	
	
	/**
	 * 查询用户辅导奖
	 */
	@Override
	public JBonusFdjResult getJBonusFdj(int uId){
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double earnings = jBonusFdjMapper.selectEarnings(uId);
		double total = jBonusFdjMapper.selectTotal(uId);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjWek(uId);
		jBonusFdjResult.setEarnings(earnings);
		jBonusFdjResult.setTotal(total);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
		
	}


	@Override
	public JBonusFdjResult selectJBonusFdjByDay(int uId,int page,int limit,String beginTime,String endTime) {
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double totalByDay = jBonusFdjMapper.selectFdjTotalByDay(uId, beginTime, endTime);
		PageHelper.startPage(page, limit);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjByDay(uId, beginTime, endTime);
		PageInfo<JBonusFdj> pageInfo = new PageInfo<JBonusFdj>(list);
		jBonusFdjResult.setEarnings(0.0);
		jBonusFdjResult.setTotal(totalByDay);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
	}


	@Override
	public JBonusFdjResult selectJBonusFdjByMonth(int uId,int page,int limit,String month) {
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double totalByMonth = jBonusFdjMapper.selectFdjTotalByMonth(uId, month);
		PageHelper.startPage(page, limit);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjByMonth(uId, month);
		PageInfo<JBonusFdj> pageInfo = new PageInfo<JBonusFdj>(list);
		jBonusFdjResult.setEarnings(0.0);
		jBonusFdjResult.setTotal(totalByMonth);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
	}
	
	
	
	

}
