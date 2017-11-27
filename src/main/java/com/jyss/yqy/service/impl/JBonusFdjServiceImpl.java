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
		JBonusFdjExample example = new JBonusFdjExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(uId);
		criteria.andStatusEqualTo(1);
		List<JBonusFdj> parentList = jBonusFdjMapper.selectByExample(example);
		
		JBonusFdjResult JBonusFdjResult = new JBonusFdjResult();
		//若有下级代理人
		if(parentList != null && parentList.size()>0){
			
			double earnings = jBonusFdjMapper.selectEarnings(uId);
			double total = jBonusFdjMapper.selectTotal(uId);
			List<JBonusFdj> list = jBonusFdjMapper.selectJBonusFdjWek(uId);
			
			if(list != null && list.size()>0){
				JBonusFdjResult.setEarnings(earnings);
				JBonusFdjResult.setTotal(total);
				JBonusFdjResult.setData(list);
				return JBonusFdjResult;
			}
			List<JBonusFdj> list2 = new ArrayList<JBonusFdj>();
			JBonusFdjResult.setEarnings(earnings);
			JBonusFdjResult.setTotal(total);
			JBonusFdjResult.setData(list2);
			return JBonusFdjResult;
		}
		//若无下级代理人
		List<JBonusFdj> list1 = new ArrayList<JBonusFdj>();
		JBonusFdjResult.setEarnings(0.0);
		JBonusFdjResult.setTotal(0.0);
		JBonusFdjResult.setData(list1);
		return JBonusFdjResult;
	}


	@Override
	public List<JBonusFdj> selectJBonusFdjByDay(int uId, String beginTime,
			String endTime) {
		List<JBonusFdj> JBonusFdjByDay = jBonusFdjMapper.selectJBonusFdjByDay(uId, beginTime, endTime);
		return JBonusFdjByDay;
	}


	@Override
	public List<JBonusFdj> selectJBonusFdjByMonth(int uId, String month) {
		List<JBonusFdj> JBonusFdjByMonth = jBonusFdjMapper.selectJBonusFdjByMonth(uId, month);
		return JBonusFdjByMonth;
	}
	
	
	
	

}
