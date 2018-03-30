package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.mapper.UMobileLoginMapper;
import com.jyss.yqy.service.UMobileLoginService;

@Service
@Transactional
public class UMobileLoginServiceImpl implements UMobileLoginService {

	@Autowired
	private UMobileLoginMapper uMobileLoginMapper;

	/*
	 * @Override public List<UMobileLogin> getUMobileLoginByUid(String uUuid) {
	 * UMobileLoginExample example = new UMobileLoginExample(); Criteria
	 * criteria = example.createCriteria(); criteria.andUUuidEqualTo(uUuid);
	 * List<UMobileLogin> list = uMobileLoginMapper.selectByExample(example);
	 * return list; }
	 */

	// /方法改造 =拿到token-找到uuid ，去查找最新一条数据token,和当前传入token做比较，不同的话 token过期，list置空
	@Override
	public List<UMobileLogin> findUserByToken(String token) {
		List<UMobileLogin> loginList = uMobileLoginMapper.findUserByToken(token);
		if (loginList.size() == 1) {
			if (!loginList.get(0).getToken().equals(token)) {
				loginList = new ArrayList<UMobileLogin>();
			}
		} else {
			loginList = new ArrayList<UMobileLogin>();
		}
		return loginList;
	}

}
