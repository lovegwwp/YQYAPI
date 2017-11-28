package com.jyss.yqy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.UMobileLoginExample;
import com.jyss.yqy.entity.UMobileLoginExample.Criteria;
import com.jyss.yqy.mapper.UMobileLoginMapper;
import com.jyss.yqy.service.UMobileLoginService;


@Service
@Transactional
public class UMobileLoginServiceImpl implements UMobileLoginService{
	
	@Autowired
	private UMobileLoginMapper uMobileLoginMapper;

	/*@Override
	public List<UMobileLogin> getUMobileLoginByUid(String uUuid) {
		UMobileLoginExample example = new UMobileLoginExample();
		Criteria criteria = example.createCriteria();
		criteria.andUUuidEqualTo(uUuid);
		List<UMobileLogin> list = uMobileLoginMapper.selectByExample(example);
		return list;
	}*/

	@Override
	public List<UMobileLogin> findUserByToken(String token) {
		List<UMobileLogin> loginList = uMobileLoginMapper.findUserByToken(token);
		return loginList;
	}
	
	
	

}
