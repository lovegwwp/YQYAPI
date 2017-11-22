package com.jyss.yqy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.StringUtils;
import com.jyss.yqy.entity.ThOrders;
import com.jyss.yqy.entity.Thd;
import com.jyss.yqy.mapper.ThdMapper;
import com.jyss.yqy.service.ThdService;
import com.jyss.yqy.utils.PasswordUtil;

@Service
public class ThdServiceImpl implements ThdService {
	@Autowired
	private ThdMapper thdMapper;

	// @Autowired
	// private XtclMapper xtclMapper;

	/**
	 * 用户登陆
	 */
	@Override
	public Map<String, Object> login(String tel, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(tel)) {
			map.put("status", "false");
			map.put("message", "请输入手机号");
			return map;
		}
		List<Thd> list = thdMapper.findThdByTel(tel);
		if (list == null || list.size() == 0) {
			map.put("status", "false");
			map.put("message", "您还没有注册哦~");
			return map;
		}
		Thd thd = list.get(0);
		if (PasswordUtil.generate(password, thd.getSalt()).equals(
				thd.getPassword())) {
			map.put("status", "true");
			map.put("message", thd);
			return map;
		}
		map.put("status", "false");
		map.put("message", "用户名或密码不正确");
		return map;
	}

	/**
	 * 用户查询
	 */
	public List<Thd> findThdByTel(String tel) {
		return thdMapper.findThdByTel(tel);
	}

	/**
	 * 密码修改
	 */
	public void updatePwd(String tel, String password, String salt) {
		thdMapper.updatePwd(tel, password, salt);
	}

	@Override
	public List<ThOrders> getThdOrdersBy(String thId) {
		// TODO Auto-generated method stub
		return thdMapper.getThdOrdersBy(thId);
	}

}
