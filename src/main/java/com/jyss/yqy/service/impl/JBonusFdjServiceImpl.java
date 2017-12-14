package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjExample;
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
		/*JBonusFdjExample example = new JBonusFdjExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(uId);
		criteria.andStatusEqualTo(1);
		List<JBonusFdj> parentList = jBonusFdjMapper.selectByExample(example);*/
		
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double earnings = jBonusFdjMapper.selectEarnings(uId);
		double total = jBonusFdjMapper.selectTotal(uId);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjWek(uId);
		jBonusFdjResult.setEarnings(earnings);
		jBonusFdjResult.setTotal(total);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
		
		//若有下级代理人
		/*if(parentList != null && parentList.size()>0){
		}*/
		//若无下级代理人
		/*List<JBonusFdj> list1 = new ArrayList<JBonusFdj>();
		jBonusFdjResult.setEarnings(0.00);
		jBonusFdjResult.setTotal(0.00);
		jBonusFdjResult.setData(list1);
		return jBonusFdjResult;*/
	}


	@Override
	public JBonusFdjResult selectJBonusFdjByDay(int uId, String beginTime,String endTime) {
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double totalByDay = jBonusFdjMapper.selectFdjTotalByDay(uId, beginTime, endTime);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjByDay(uId, beginTime, endTime);
		jBonusFdjResult.setEarnings(null);
		jBonusFdjResult.setTotal(totalByDay);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
	}


	@Override
	public JBonusFdjResult selectJBonusFdjByMonth(int uId, String month) {
		JBonusFdjResult jBonusFdjResult = new JBonusFdjResult();
		double totalByMonth = jBonusFdjMapper.selectFdjTotalByMonth(uId, month);
		List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjByMonth(uId, month);
		jBonusFdjResult.setEarnings(null);
		jBonusFdjResult.setTotal(totalByMonth);
		jBonusFdjResult.setData(list);
		return jBonusFdjResult;
	}
	
	
	
	

}
