package com.jyss.yqy.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.User;
import com.jyss.yqy.entity.UserAuth;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.filter.MySessionContext;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.utils.Base64Image;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.HttpClientUtil;
import com.jyss.yqy.utils.PasswordUtil;

@Controller
@RequestMapping("/b")
public class UserAction {
	@Autowired
	private UserService userService;
	@Autowired
	private UMobileLoginService uMobileLoginService;

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
	 * 用户退出
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/loginOut")
	@ResponseBody
	public Map<String, Object> loginOut(@RequestParam("token") String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "身份过期！");
			map.put("code", "-1");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		String newToken = CommTool.getUUID();
		int count = userService.loginOut(uuuid, newToken);
		if (count == 1) {
			map.put("status", "true");
			map.put("message", "退出成功！");
			map.put("code", "0");
			return map;
		}
		map.put("status", "false");
		map.put("message", "退出失败！");
		map.put("code", "-2");
		return map;
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
				List<UserBean> ulist = userService.getUserBy(tel, "1", "", "","");
				if (ulist == null || ulist.size() == 0) {
					m2 = sendCode(tel, request);
					if (m2.get("msgDo").equals("0")) {
						map.put("status", "true");
						map.put("message", "操作成功！");
						m.put("sessionId", m2.get("sessionId"));
						//m.put("msgDo", m2.get("msgDo"));
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
		if (m2.get("msgDo").equals("0")) {
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
		String msgDo = HttpClientUtil.sendMsgDo(tel, "您此次操作的验证码为：" + code
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
		/*HttpSession session = MySessionContext.getSession(sessionId);
		String SessTo = (String) session.getAttribute("tel");
		String SessToYzm = (String) session.getAttribute("code");
		if (SessTo != null && !(SessTo.equals("")) && SessToYzm != null
				&& !(SessToYzm.equals(""))) {
			if (SessTo.equals(account)) {
				if (SessToYzm.equals(code)) {*/
					// 验证码正确，进行注册表插入
					List<UserBean> ulist = userService.getUserBy(account, "1",
							"", "","");
					System.out.println(ulist.size());
					if (ulist != null && ulist.size() >= 1) {
						m.put("status", "false");
						m.put("message", "请输入正确的手机号！");
						m.put("data", "");
						return m;
					}
					User user = new User();
					user.setAccount(account);
					user.setSalt(CommTool.getSalt());
					user.setPwd(PasswordUtil.generate(password, user.getSalt()));
					count = userService.addUser(user);
					if (count == 1) {
						String token = CommTool.getUUID();
						List<UserBean> relist = userService.getUserBy(account,
								"1", "", "","");
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
				/*}
			}
		}
		m.put("status", "false");
		m.put("message", "验证码错误！");
		m.put("data", "");
		return m;*/

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
							"", "","");
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
	 * 验证码===修改密码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upZfPwdByCode")
	@ResponseBody
	public ResponseEntity upZfPwdByCode(@RequestParam("code") String code,
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
							"", "","");
					if (ulist == null && ulist.size() != 1) {
						return new ResponseEntity("false", "请输入正确的手机号");
					}
					UserBean ub = ulist.get(0);
					count = userService.upPayPwd(ub.getUuid(),
							PasswordUtil.generatePayPwd(password));
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
		List<UserBean> ulist = userService.getUserBy(account, "1", "2", "","");
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
	 * 获取个人推广码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/getMyBCode")
	@ResponseBody
	public Map<String, Object> getMyBCode(@RequestParam("token") String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		List<UserBean> ulist = userService.getUserInfo("", uuuid, "", "1", "2",
				"1");
		if (ulist == null || ulist.size() != 1) {
			map.put("status", "false");
			map.put("message", "用户信息错误");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		// /用户个人信息
		UserBean ub = ulist.get(0);
		// ub.setToken(token);
		// ub.setAvatar(Constant.httpUrl + ub.getAvatar());
		m.put("bCode", ub.getbCode());
		map.put("status", "true");
		map.put("message", "载入信息成功！");
		map.put("code", "0");
		map.put("data", m);
		return map;

	}

	/**
	 * 获取个人信息
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/getMyUserInfo")
	@ResponseBody
	public Map<String, Object> getMyInfo(@RequestParam("token") String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		List<UserBean> ulist = userService.getUserInfo("", uuuid, "", "1", "2",
				"1");
		if (ulist == null || ulist.size() != 1) {
			map.put("status", "false");
			map.put("message", "用户信息错误");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		// /用户个人信息
		UserBean ub = ulist.get(0);
		ub.setToken(token);
		ub.setAvatar(Constant.httpUrl + ub.getAvatar());
		map.put("status", "true");
		map.put("message", "载入信息成功！");
		map.put("code", "0");
		map.put("data", ub);
		return map;

	}

	/**
	 * 修改个人信息
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upMyUserInfo")
	@ResponseBody
	public Map<String, Object> upMyInfo(@RequestParam("token") String token,
			@RequestParam("nick") String nick,
			@RequestParam("province") String province,
			@RequestParam("provinceId") String provinceId,
			@RequestParam("cityId") String cityId,
			@RequestParam("city") String city,
			@RequestParam("areaId") String areaId,
			@RequestParam("area") String area) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		int count = userService.upUserMyInfo(uuuid, nick, province, provinceId,
				cityId, city, areaId, area);
		if (count == 1) {
			map.put("status", "true");
			map.put("message", "修改成功！");
			map.put("code", "0");
			return map;
		}
		map.put("status", "false");
		map.put("message", "修改失败！");
		map.put("code", "-2");
		return map;

	}

	/**
	 * 修改密码==支付密码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/upPayPwd")
	@ResponseBody
	public Map<String, Object> upPayPwd(@RequestParam("oldPwd") String oldPwd,
			@RequestParam("token") String token,
			@RequestParam("password") String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (password.equals("") || password.length() != 6) {
			map.put("status", "false");
			map.put("message", "密码不足6位！");
			map.put("code", "-5");
			return map;
		}
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		List<UserBean> ulist = userService.getUserInfo("", uuuid, "", "1", "2",
				"1");
		if (ulist == null || ulist.size() != 1) {
			map.put("status", "false");
			map.put("message", "用户信息错误");
			map.put("code", "-2");
			return map;
		}
		// /用户个人信息
		UserBean ub = ulist.get(0);
		int count = 0;
		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")) {
			map.put("status", "false");
			map.put("message", "原密码错误！");
			map.put("code", "-3");
			return map;
		}
		if (ub.getPayPwd().equals(PasswordUtil.generatePayPwd(oldPwd))) {
			count = userService.upPayPwd(uuuid,
					PasswordUtil.generatePayPwd(password));
		} else {
			map.put("status", "false");
			map.put("message", "原密码错误！");
			map.put("code", "-3");
			return map;
		}

		if (count == 1) {
			map.put("status", "true");
			map.put("message", "修改成功！");
			map.put("code", "0");
			return map;
		}
		map.put("status", "fasle");
		map.put("message", "修改失败！");
		map.put("code", "-4");
		return map;

	}

	/**
	 * 设置密码==支付密码
	 */
	// status1删除 1=正常2=禁用 isAuth 1=已提交 2=审核通过3=审核不通过 statusAuth 0=审核中 1=通过 2=未通过
	@RequestMapping("/szPayPwd")
	@ResponseBody
	public Map<String, Object> szPayPwd(@RequestParam("token") String token,
			@RequestParam("password") String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (password.equals("") || password.length() != 6) {
			map.put("status", "false");
			map.put("message", "密码不足6位！");
			map.put("code", "-3");
			return map;
		}
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			return map;
		}
		// /获取最新token ===》uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		String pwd = PasswordUtil.generatePayPwd(password);// 新密码加密直接md5
		int count = userService.upPayPwd(uuuid, pwd);
		if (count == 1) {
			map.put("status", "true");
			map.put("message", "设置成功！");
			map.put("code", "0");
			return map;
		}
		map.put("status", "fasle");
		map.put("message", "设置失败！");
		map.put("code", "-2");
		return map;

	}

	/**
	 * 头像修改
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/upAvatar")
	@ResponseBody
	public Map<String, String> updateAvatar(
			@RequestParam("token") String token,
			@RequestParam("picture") String picture,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList != null && loginList.size() > 0) {
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin
					.getuUuid());
			if (list != null && list.size() > 0) {
				UserBean userBean = list.get(0);
				User user = new User();
				user.setId(userBean.getId());
				// Base64.decode(photo);
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				String filePath = request.getSession().getServletContext()
						.getRealPath("/");
				int index = filePath.indexOf("YQYAPI");
				filePath = filePath.substring(0, index) + "uploadPic" + "/";
				File file = new File(filePath);
				CommTool.judeDirExists(file);
				boolean isOk = false;
				filePath = filePath + uMobileLogin.getuUuid()
						+ CommTool.getSalt() + ".png";
				isOk = Base64Image.GenerateImage(picture, filePath);
				if (isOk) {
					user.setAvatar(filePath.substring(filePath
							.indexOf("uploadPic")));
				}
				userService.upMyInfo(user);
				map.put("code", "0");
				map.put("status", "true");
				map.put("message", "头像修改成功");
				map.put("data", "");
				return map;
			}

		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新登陆");
		map.put("data", "");
		return map;
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

	@InitBinder
	protected void init(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	/**
	 * 实名认证
	 */
	@RequestMapping("/authen")
	@ResponseBody
	public Map<String, String> saveUserAuth(UserAuth userAuth,
			@RequestParam("token") String token, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList != null && loginList.size() > 0) {
			UMobileLogin uMobileLogin = loginList.get(0);
			String uuid = uMobileLogin.getuUuid();
			userAuth.setuUuid(uuid);

			// Base64.decode(photo);
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			String photo1 = userAuth.getCardPicture1();
			String photo2 = userAuth.getCardPicture2();
			String photo3 = userAuth.getCardPicture3();
			Map<String, Object> mrMap = new HashMap<String, Object>();
			String filePath = request.getSession().getServletContext()
					.getRealPath("/");
			int index = filePath.indexOf("YQYAPI");
			// boolean isOk = false;
			filePath = filePath.substring(0, index) + "uploadAuthPic" + "/";
			File f = new File(filePath);
			CommTool.judeDirExists(f);
			boolean isOk1 = false;
			boolean isOk2 = false;
			boolean isOk3 = false;
			String filePath1 = filePath + uuid + CommTool.getSalt() + "1.png";
			String filePath2 = filePath + uuid + CommTool.getSalt() + "2.png";
			String filePath3 = filePath + uuid + CommTool.getSalt() + "3.png";
			isOk1 = Base64Image.GenerateImage(photo1, filePath1);
			if (isOk1) {
				userAuth.setCardPicture1(filePath1.substring(filePath1
						.indexOf("uploadAuthPic")));
			}
			isOk2 = Base64Image.GenerateImage(photo2, filePath2);
			if (isOk2) {
				userAuth.setCardPicture2(filePath2.substring(filePath2
						.indexOf("uploadAuthPic")));
			}
			isOk3 = Base64Image.GenerateImage(photo3, filePath3);
			if (isOk3) {
				userAuth.setCardPicture3(filePath3.substring(filePath3
						.indexOf("uploadAuthPic")));
			}

			int idNum = userService.insertUserAuth(userAuth);
			if (idNum==1) {
				/////实名提交后，user 表该状态
				idNum=0;
				idNum = userService.upUserAllStatusByUUid("", "", "", "", "1", uuid);
			}
			
			String val = idNum + "";
			if (val != null && !"".equals(val)) {
				map.put("code", "0");
				map.put("status", "true");
				map.put("message", "信息已提交，请等待审核~");
				map.put("data", "");
				return map;
			}
			map.put("code", "-1");
			map.put("status", "false");
			map.put("message", "提交失败，请重新提交");
			map.put("data", "");
			return map;
		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新登陆");
		map.put("data", "");
		return map;

	}





}
