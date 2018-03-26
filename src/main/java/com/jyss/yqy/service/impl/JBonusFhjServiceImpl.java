package com.jyss.yqy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFhj;
import com.jyss.yqy.entity.JBonusResult;
import com.jyss.yqy.mapper.JBonusFhjMapper;
import com.jyss.yqy.service.JBonusFhjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class JBonusFhjServiceImpl implements JBonusFhjService {
	

	@Autowired
	private JBonusFhjMapper jBonusFhjMapper;
	
	

	/**
	 * 查询本周
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
}
