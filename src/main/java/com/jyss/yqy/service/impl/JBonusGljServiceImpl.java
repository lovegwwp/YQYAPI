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
			if(list != null && list.size()>0){
				jBonusGljResult.setEarnings(earnings);
				jBonusGljResult.setTotal(total);
				jBonusGljResult.setList(list);
				return jBonusGljResult;
			}
			//本周无记录
			List<JBonusGlj> list2 = new ArrayList<JBonusGlj>();
			jBonusGljResult.setEarnings(earnings);
			jBonusGljResult.setTotal(total);
			jBonusGljResult.setList(list2);
			return jBonusGljResult;
		}
		//若无下级代理人
		List<JBonusGlj> list1 = new ArrayList<JBonusGlj>();
		jBonusGljResult.setEarnings(0.0);
		jBonusGljResult.setTotal(0.0);
		jBonusGljResult.setList(list1);
		return jBonusGljResult;
	}


	@Override
	public List<JBonusGlj> selectJBonusGljByDay(int uId, String beginTime,
			String endTime) {
		List<JBonusGlj> jBonusGljByDay = jBonusGljMapper.selectJBonusGljByDay(uId, beginTime, endTime);
		return jBonusGljByDay;
	}


	@Override
	public List<JBonusGlj> selectJBonusGljByMonth(int uId, String month) {
		List<JBonusGlj> jBonusGljByMonth = jBonusGljMapper.selectJBonusGljByMonth(uId, month);
		return jBonusGljByMonth;
	}
	
	
	
	

}
