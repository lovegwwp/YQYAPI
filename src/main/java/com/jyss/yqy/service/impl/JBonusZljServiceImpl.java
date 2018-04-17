package com.jyss.yqy.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.*;
import com.jyss.yqy.mapper.*;
import com.jyss.yqy.service.JBonusZljService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class JBonusZljServiceImpl implements JBonusZljService {

	@Autowired
	private JBonusZljMapper jBonusZljMapper;


	/**
	 * 查询所有收益
	 */
	@Override
	public JBonusResult getTotalAndZl(Integer uId) {
		float total = jBonusZljMapper.selectTotal(uId, null);
		List<JRecordZl> list = jBonusZljMapper.selectZlByUId(uId);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}


	/**
	 * 查询本周（昨日）
	 */
	@Override
	public JBonusResult getJBonusZlj(Integer uId, Integer sId) {

		float earnings = jBonusZljMapper.selectJBonusZljDay(uId,sId);
		float total = jBonusZljMapper.selectTotal(uId,sId);
		List<JBonusGxj> list = jBonusZljMapper.selectJBonusZljWek(uId,sId);

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
	public JBonusResult selectJBonusZljByDay(Integer uId, Integer sId, int page, int limit, String beginTime, String endTime) {
		float total = jBonusZljMapper.selectZljTotalByDay(uId, sId, beginTime, endTime);

		PageHelper.startPage(page, limit);
		List<JBonusGxj> list = jBonusZljMapper.selectJBonusZljByDay(uId, sId, beginTime, endTime);
		PageInfo<JBonusGxj> pageInfo = new PageInfo<JBonusGxj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}



	/**
	 * 月份查询
	 */
	@Override
	public JBonusResult selectJBonusZljByMonth(Integer uId, Integer sId, int page, int limit, String month) {
		float total = jBonusZljMapper.selectZljTotalByMonth(uId, sId, month);

		PageHelper.startPage(page, limit);
		List<JBonusGxj> list = jBonusZljMapper.selectJBonusZljByMonth(uId, sId, month);
		PageInfo<JBonusGxj> pageInfo = new PageInfo<JBonusGxj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}




}
