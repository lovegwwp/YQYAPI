package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljExample;
import com.jyss.yqy.entity.JBonusGljExample.Criteria;
import com.jyss.yqy.entity.JBonusGljResult;
import com.jyss.yqy.mapper.JBonusGljMapper;
import com.jyss.yqy.service.JBonusGljService;

@Service
@Transactional
public class JBonusGljServiceImpl implements JBonusGljService{
	
	@Autowired
	private JBonusGljMapper jBonusGljMapper;
	
	
	/**
	 * 查询用户管理奖
	 */
	@Override
	public JBonusGljResult getJBonusGlj(int uId){
		JBonusGljExample example = new JBonusGljExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(uId);
		criteria.andStatusEqualTo(1);
		List<JBonusGlj> parentList = jBonusGljMapper.selectByExample(example);
		
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		//若有下级代理人
		if(parentList != null && parentList.size()>0){
			
			double earnings = jBonusGljMapper.selectEarnings(uId);
			double total = jBonusGljMapper.selectTotal(uId);
			List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljWek(uId);
			
			jBonusGljResult.setEarnings(earnings);
			jBonusGljResult.setTotal(total);
			jBonusGljResult.setData(list);
			return jBonusGljResult;
		}
		//若无下级代理人
		List<JBonusGlj> list1 = new ArrayList<JBonusGlj>();
		jBonusGljResult.setEarnings(0.0);
		jBonusGljResult.setTotal(0.0);
		jBonusGljResult.setData(list1);
		return jBonusGljResult;
	}


	@Override
	public JBonusGljResult selectJBonusGljByDay(int uId, String beginTime,String endTime) {
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		double totalByDay = jBonusGljMapper.selectGljTotalByDay(uId, beginTime, endTime);
		List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljByDay(uId, beginTime, endTime);
		jBonusGljResult.setEarnings(null);
		jBonusGljResult.setTotal(totalByDay);
		jBonusGljResult.setData(list);
		return jBonusGljResult;
	}


	@Override
	public JBonusGljResult selectJBonusGljByMonth(int uId, String month) {
		JBonusGljResult jBonusGljResult = new JBonusGljResult();
		double totalByMonth = jBonusGljMapper.selectGljTotalByMonth(uId, month);
		List<JBonusGlj> list = jBonusGljMapper.selectJBonusGljByMonth(uId, month);
		jBonusGljResult.setEarnings(null);
		jBonusGljResult.setTotal(totalByMonth);
		jBonusGljResult.setData(list);
		return jBonusGljResult;
	}
	
	
	
	

}
