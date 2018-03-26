package com.jyss.yqy.service.impl;


import com.jyss.yqy.entity.JBonusCj;
import com.jyss.yqy.entity.JBonusResult;
import com.jyss.yqy.mapper.JBonusCjMapper;
import com.jyss.yqy.service.JBonusCjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class JBonusCjServiceImpl implements JBonusCjService {

	@Autowired
	private JBonusCjMapper jBonusCjMapper;


	/**
	 * 层奖查询（今日）
	 */
	@Override
	public JBonusResult getJBonusCj(Integer uId) {
		float earnings = jBonusCjMapper.selectEarnings(uId);
		float total = jBonusCjMapper.selectTotal(uId);
		List<JBonusCj> list = jBonusCjMapper.selectJBonusCj(uId);

		JBonusResult result = new JBonusResult();
		result.setEarnings(earnings);
		result.setTotal(total);
		result.setData(list);
		return result;
	}


}
