package com.jyss.yqy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.internal.util.StringUtils;
import com.jyss.yqy.entity.User;
import com.jyss.yqy.entity.UserAuth;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.UserAuthMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.PasswordUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserAuthMapper userAuthMapper;

	/**
	 * 用户登陆 status=-1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过
	 * 2=未通过
	 */
	@Override
	public Map<String, Object> login(String account, String password) {
		Map<String, Object> m = new HashMap<String, Object>();
		if (StringUtils.isEmpty(account)) {
			m.put("status", "false");
			m.put("message", "手机号错误！");
			m.put("code", "-1");
			m.put("data", "");
			return m;
		}
		List<UserBean> list = userMapper.getUserBy(account, "1", "", "","");
		if (list == null || list.size() != 1) {
			m.put("status", "false");
			m.put("message", "当前无此用户！");
			m.put("code", "-3");
			m.put("data", "");
			return m;
		}
		// ///密码
		UserBean ub = list.get(0);
		String token = CommTool.getUUID();
		ub.setToken(token);
		if (!PasswordUtil.generate(password, ub.getSalt()).equals(ub.getPwd())) {
			m.put("status", "false");
			m.put("message", "密码错误！");
			m.put("code", "-2");
			m.put("data", "");
			return m;
		}
		int count = userMapper.addLogin(ub.getUuid(), token);///登录token
		if (count == 1) {
			// //已有用户，判断身份，是普通会员0还是代言人1，1=成为代言人 2=一级代理人 3=二级代理人 4=三级代理人 5=经理人（虚拟账号）6=市场总监助理
			// 普通会员
			if (ub.getIsChuangke() == 0) {
				m.put("status", "true");
				m.put("message", "普通会员登录，去升级为合伙人！");
				m.put("code", "3");
				m.put("data", ub);
				return m;
				// /代言人
			} else if (ub.getIsChuangke() == 1) {
				// isAuth 1=已提交 2=审核通过3=审核不通过
				if (ub.getIsAuth() == 1) {
					m.put("status", "false");
					m.put("message", "当前用户正在审核！");
					m.put("code", "1");
					m.put("data", "");
					return m;
				} else if (ub.getIsAuth() == 3) {
					m.put("status", "false");
					m.put("message", "用户审核不通过！");
					m.put("code", "2");
					m.put("data", "");
					return m;
				} else if (ub.getIsAuth() == 2) {
					m.put("status", "true");
					m.put("message", "代言人登录，去升级为合伙人！");
					m.put("code", "4");
					m.put("data", ub);
					return m;
				}
				// /代理人
			} else if (ub.getIsChuangke() >= 2) {
				if (ub.getIsAuth() == 1) {
					m.put("status", "false");
					m.put("message", "当前用户正在审核！");
					m.put("code", "1");
					m.put("data", "");
					return m;
				} else if (ub.getIsAuth() == 3) {
					m.put("status", "false");
					m.put("message", "用户审核不通过！");
					m.put("code", "2");
					m.put("data", "");
					return m;
				}
				m.put("status", "true");
				m.put("message", "登陆成功！");
				m.put("code", "0");
				m.put("data", ub);
				return m;
			}
		}
		m.put("status", "false");
		m.put("message", "手机号错误！");
		m.put("code", "-1");
		m.put("data", "");
		return m;

	}

	@Override
	public List<UserBean> getUserBy(String account, String status,
			String isAuth, String statusAuth,String statusAuthMoreThan) {
		// TODO Auto-generated method stub
		return userMapper.getUserBy(account, status, isAuth, statusAuth,statusAuthMoreThan);
	}

	@Override
	public List<UserBean> getUserById(String id, String status, String isAuth) {
		// TODO Auto-generated method stub
		return userMapper.getUserById(id, status, isAuth);
	}

	@Override
	public int addUser(User user) {
		// TODO Auto-generated method stub
		user.setUuid(CommTool.getMyUUID());
		// status1删除 1=正常2=禁用
		// isAuth 1=已提交 2=审核通过3=审核不通过
		// --is_chuangke是普通会员0还是代言人1，1=成为代言人 2=一级代理人 3=二级代理人 4=三级代理人 5=经理人（虚拟账号）6=市场总监助理
		user.setIsChuangke(0);
		user.setStatus("1");
		user.setIsAuth(1);
		user.setIsChuangke(0);

		return userMapper.addUser(user);
	}

	@Override
	public int upMyInfo(User user) {
		// TODO Auto-generated method stub
		return userMapper.upMyInfo(user);
	}

	@Override
	public int upPwd(String pwd, String salt, String id) {
		// TODO Auto-generated method stub
		return userMapper.upPwd(pwd, salt, id);
	}

	@Override
	public int addLogin(String uuid, String token) {
		// TODO Auto-generated method stub
		// token = CommTool.getUUID();
		return userMapper.addLogin(uuid, token);
	}

	@Override
	public int upToken(String uuid, String token, long time) {
		// TODO Auto-generated method stub
		token = CommTool.getUUID();
		time = new Date().getTime();
		return userMapper.upToken(uuid, token, time);
	}

	@Override
	public List<UserBean> getToken(String uuid, String token) {
		// TODO Auto-generated method stub
		return userMapper.getToken(uuid, token);
	}

	@Override
	public int insertUserAuth(UserAuth userAuth) {
		userAuth.setStatus(0);
		userAuth.setCreatedAt(new Date());

		int idNum = userAuthMapper.insertUserAuth(userAuth);
		return idNum;
	}

	@Override
	public List<UserBean> getUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		return userMapper.getUserByUuid(uuid);
	}

	@Override
	public int upUserMyInfo(String uuid, String nick, String province,
			String provinceId, String cityId, String city, String areaId,
			String area) {
		// TODO Auto-generated method stub
		return userMapper.upUserMyInfo(uuid, nick, province, provinceId,
				cityId, city, areaId, area);
	}

	@Override
	public int upPayPwd(String uuid, String payPwd) {
		// TODO Auto-generated method stub
		return userMapper.upPayPwd(uuid, payPwd);
	}

	@Override
	public List<UserBean> getUserInfo(String account, String uuid, String id,
			String status, String isAuth, String statusAuth) {
		// TODO Auto-generated method stub
		return userMapper.getUserInfo(account, uuid, id, status, isAuth,
				statusAuth);
	}

	@Override
	public int loginOut(String uuid, String token) {
		// TODO Auto-generated method stub
		return userMapper.addLogin(uuid, token);
	}

	@Override
	public int upUserAllStatus(String status, String bCode, String bIsPay,
			String isChuangke, String isAuth, String id) {
		// TODO Auto-generated method stub
		return userMapper.upUserAllStatus(status, bCode, bIsPay, isChuangke,
				isAuth, id);
	}

	@Override
	public int updateUserBackScore(Float cashScore, Float shoppingScore,
			String uuuid) {
		// TODO Auto-generated method stub
		return userMapper.updateUserBackScore(cashScore, shoppingScore, uuuid);
	}

	@Override
	public int upTotalPv(String uuid, String totalPv) {
		// TODO Auto-generated method stub
		return userMapper.upTotalPv(uuid, totalPv);
	}

	@Override
	public List<UserBean> getUserByFHJ(String account, String status,
			String isAuth, String isChuangke) {
		// TODO Auto-generated method stub
		return userMapper.getUserByFHJ(account, status, isAuth, isChuangke);
	}

	@Override
	public int updateScoreByFHJ(String cashScore, String shoppingScore,
			String totalPv, String id, String isChuangke) {
		// TODO Auto-generated method stub
		return userMapper.updateScoreByFHJ(cashScore, shoppingScore, totalPv, id, isChuangke);
	}

	@Override
	public int upUserAllStatusByUUid(String status, String bCode,
			String bIsPay, String isChuangke, String isAuth, String uuid) {
		// TODO Auto-generated method stub
		return userMapper.upUserAllStatusByUUid(status, bCode, bIsPay, isChuangke, isAuth, uuid);
	}

}
