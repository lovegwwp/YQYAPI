package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.entity.User;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.filter.MySessionContext;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.HttpClientUtil;
import com.jyss.yqy.utils.PasswordUtil;

@Controller
@RequestMapping("/b")
public class UserAction {
	@Autowired
	private UserService userService;

	/**
	 * 用户登陆
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam("account") String account,
			@RequestParam("password") String password) {
		Map<String, Object> m = new HashMap<String, Object>();
		m = userService.login(account, password);
		return m;
	}

	/**
	 * 发送验证码 bz =1 注册 bz=2忘记密码
	 */
	@RequestMapping("/sendCode")
	@ResponseBody
	public Map<String, Object> sendCode(@RequestParam("tel") String tel,
			@RequestParam("bz") String bz, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> m = new HashMap<String, String>();
		Map<String, String> m2 = new HashMap<String, String>();
		if (bz.equals("1")) {
			if (tel != null && !"".equals(tel)) {
				List<UserBean> ulist = userService.getUserBy(tel, "1", "", "");
				if (ulist == null || ulist.size() == 0) {
					m2 = sendCode(tel, request);
					if (m2.get("msgDo").equals("1")) {
						map.put("status", "true");
						map.put("message", "操作成功！");
						m.put("sessionId", m2.get("sessionId"));
						map.put("data", m);
						return map;
					}
					map.put("status", "false");
					map.put("message", "操作失败！");
					m.put("sessionId", "");
					map.put("data", m);
					return map;
				}
			}

			map.put("status", "false");
			map.put("message", "该手机号已注册");
			m.put("sessionId", "");
			map.put("data", m);
			return map;
		}
		// //单纯修改密码 ---发送验证码
		m2 = sendCode(tel, request);
		if (m2.get("msgDo").equals("1")) {
			map.put("status", "true");
			map.put("message", "操作成功！");
			m.put("sessionId", m2.get("sessionId"));
			map.put("data", m);
			return map;
		}
		map.put("status", "false");
		map.put("message", "操作失败！");
		m.put("sessionId", "");
		map.put("data", m);
		return map;
	}

	public Map<String, String> sendCode(String tel, HttpServletRequest request) {
		String code = CommTool.getYzm();
		Map<String, String> m = new HashMap<String, String>();
		// 添加到session
		HttpSession session = request.getSession();
		String sessionId = "";
		sessionId = session.getId();
		session.removeAttribute("code");
		session.setAttribute("code", code);
		session.removeAttribute("tel");
		session.setAttribute("tel", tel);
		// 设置过期时间(S)
		session.setMaxInactiveInterval(10 * 60);
		String msgDo = HttpClientUtil.MsgDo(tel, "您此次操作的验证码为：" + code
				+ ",请尽快在10分钟内完成验证");
		m.put("sessionId", sessionId);
		m.put("msgDo", msgDo);
		return m;

	}

	/**
	 * 用户注册，验证=验证码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/signOn")
	@ResponseBody
	public Map<String, Object> signOn(@RequestParam("code") String code,
			@RequestParam("account") String account,
			@RequestParam("password") String password,
			HttpServletRequest request,
			@RequestParam("sessionId") String sessionId) {
		int count = 0;
		Map<String, Object> m = new HashMap<String, Object>();
		HttpSession session = MySessionContext.getSession(sessionId);
		String SessTo = (String) session.getAttribute("tel");
		String SessToYzm = (String) session.getAttribute("code");
		if (SessTo != null && !(SessTo.equals("")) && SessToYzm != null
				&& !(SessToYzm.equals(""))) {
			if (SessTo.equals(account)) {
				if (SessToYzm.equals(code)) {
					// 验证码正确，进行注册表插入
					List<UserBean> ulist = userService.getUserBy(account, "1",
							"", "");
					System.out.println(ulist.size());
					if (ulist != null && ulist.size() >= 1) {
						m.put("status", "false");
						m.put("message", "请输入正确的手机号！");
						m.put("data", "");
						return m;
					}
					User user = new User();
					user.setAccount(SessTo);
					user.setSalt(CommTool.getSalt());
					user.setPwd(PasswordUtil.generate(password, user.getSalt()));
					count = userService.addUser(user);
					if (count == 1) {
						String token = CommTool.getUUID();
						List<UserBean> relist = userService.getUserBy(account,
								"1", "", "");
						UserBean userb = relist.get(0);
						count = userService.addLogin(userb.getUuid(), token);
						if (count == 1) {
							userb.setToken(token);
							m.put("status", "true");
							m.put("message", "注册成功！");
							m.put("data", userb);
							return m;
						}

					}
					m.put("status", "false");
					m.put("message", "注册失败！");
					m.put("data", "");
					return m;
				}
			}
		}
		m.put("status", "false");
		m.put("message", "验证码错误！");
		m.put("data", "");
		return m;

	}

	/**
	 * 验证码===修改密码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upPwdByCode")
	@ResponseBody
	public ResponseEntity upPwdByCode(@RequestParam("code") String code,
			@RequestParam("account") String account,
			@RequestParam("password") String password,
			HttpServletRequest request,
			@RequestParam("sessionId") String sessionId) {
		int count = 0;
		HttpSession session = MySessionContext.getSession(sessionId);
		String SessTo = (String) session.getAttribute("tel");
		String SessToYzm = (String) session.getAttribute("code");
		if (SessTo != null && !(SessTo.equals("")) && SessToYzm != null
				&& !(SessToYzm.equals(""))) {
			if (SessTo.equals(account)) {
				if (SessToYzm.equals(code)) {
					// 验证码正确，进行用户认证，密码修改
					String salt = CommTool.getSalt();
					String pwd = PasswordUtil.generate(password, salt);
					List<UserBean> ulist = userService.getUserBy(account, "1",
							"", "");
					if (ulist == null && ulist.size() != 1) {
						return new ResponseEntity("false", "请输入正确的手机号");
					}
					UserBean ub = ulist.get(0);
					count = userService.upPwd(pwd, salt, ub.getId() + "");
					if (count == 1) {
						return new ResponseEntity("true", "修改成功！");
					}
					return new ResponseEntity("false", "修改失败！");
				}
			}
		}
		return new ResponseEntity("false", "验证码错误！");

	}

	/**
	 * 直接===修改密码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upPwd")
	@ResponseBody
	public ResponseEntity upPwd(@RequestParam("oldPwd") String oldPwd,
			@RequestParam("account") String account,
			@RequestParam("password") String password) {
		int count = 0;

		String salt = CommTool.getSalt();
		String pwd = PasswordUtil.generate(password, salt);// 新密码加密
		List<UserBean> ulist = userService.getUserBy(account, "1", "2", "");
		if (ulist == null || ulist.size() != 1) {
			return new ResponseEntity("false", "请输入正确的手机号");
		}
		UserBean ub = ulist.get(0);
		// 旧密码加密后对比
		if (ub.getPwd().equals(PasswordUtil.generate(oldPwd, ub.getSalt()))) {
			count = userService.upPwd(pwd, salt, ub.getId() + "");
		} else {
			return new ResponseEntity("false", "原密码错误！");
		}
		if (count == 1) {
			return new ResponseEntity("true", "修改成功！");
		}
		return new ResponseEntity("false", "修改失败！");

	}

	/**
	 * 填写推广码。
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upCodeRecord")
	@ResponseBody
	public Map<String, String> upCodeRecord(@RequestParam("uuid") String uuid,
			@RequestParam("code") String code) {
		int count = 0;

		Map<String, String> m = new HashMap<String, String>();
		// m.put("status", "true");
		// m.put("message", "已经实名认证");
		// m.put("code", "0");
		m.put("status", "true");
		m.put("message", "推广码认证通过！");
		m.put("code", "0");
		// if (count == 1) {
		// return new ResponseEntity("true", "修改成功！");
		// }
		return m;

	}

}
