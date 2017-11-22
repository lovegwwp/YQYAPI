package com.jyss.yqy.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jyss.yqy.entity.AccountUser;
import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.service.AccountUserService;
import com.jyss.yqy.shiro.ShiroToken;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.DownLoadUtils;
import com.jyss.yqy.utils.PasswordUtil;
import com.jyss.yqy.utils.Utils;

@Controller
public class AccountUserAction {
	@Autowired
	private AccountUserService auService;

	// private String yzm;

	@RequestMapping("/SuccTz")
	public String SuccTz() {
		return "index";
	}

	// /用户列表页面跳转
	@RequestMapping("/accounts")
	public String accuontsTz() {
		return "accounts";
	}

	@RequestMapping("/rePwd")
	public String rePwd() {
		return "rePwd";
	}

	@RequestMapping("/shiro-tcxt")
	public String tcxt(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			try {
				subject.logout();
			} catch (Exception ex) {
			}
		}
		return "login";
	}

	@RequestMapping("/shiro-getLogin")
	public ModelAndView getLogin(@RequestParam("logName") String logName,
			@RequestParam("logPass") String logPass,
			@RequestParam("logYzm") String logYzm, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView();
		// 验证验证码
		String codeImg = (String) request.getSession().getAttribute("codeImg");
		if (!(logYzm.toUpperCase().equals(codeImg))) {
			mav.addObject("error", "验证码错误！");
			mav.setViewName("error");
			return mav;
		}
		int isOnly = 0;
		isOnly = auService.getAuNum(logName);
		if (isOnly == 0) {
			mav.addObject("error", "账号不存在！");
			mav.setViewName("error");
			return mav;
		}
		if (isOnly != 1) {
			mav.addObject("error", "账号冲突！");
			mav.setViewName("error");
			return mav;
		}
		AccountUser dbAu = auService.getAuBy(logName);
		/*
		 * String info = loginUser(dbAu); if (!"SUCC".equals(info)) {
		 * mav.addObject("error", info); mav.setViewName("error"); return mav;
		 * }else { // 最终登陆成功 HttpSession session = request.getSession();
		 * session.setAttribute("username", logName); //
		 * session.setMaxInactiveInterval(1 * 60 * 60);// 设置时间1小时
		 * mav.addObject("username", logName); mav.setViewName("index"); return
		 * mav; }
		 */
		if (PasswordUtil.generate(logPass, dbAu.getSalt())
				.equals(dbAu.getPwd())) {
			HttpSession session = request.getSession();
			session.setAttribute("username", logName);
			// session.setMaxInactiveInterval(1 * 60 * 60);// 设置时间1小时
			mav.addObject("username", logName);
			mav.setViewName("index");
			return mav;
		} else {
			// 密码错误
			mav.addObject("error", "密码错误");
			mav.setViewName("error");
			return mav;
		}
	}

	private String loginUser(AccountUser user) {
		if (isRelogin(user))
			return "SUCC"; // 如果已经登陆，无需重新登录

		return shiroLogin(user); // 调用shiro的登陆验证
	}

	private String shiroLogin(AccountUser user) {
		// 组装token，包括客户公司名称、简称、客户编号、用户名称；密码
		// UsernamePasswordToken token = new UsernamePasswordToken(
		// user.getUsername(), user.getPassword());
		ShiroToken token = new ShiroToken(user.getUsername(), user.getPwd(),
				user.getSalt());
		token.setRememberMe(true); // 记住我 下次无需验证

		// shiro登陆验证
		try {
			SecurityUtils.getSubject().login(token);
		} catch (UnknownAccountException ex) {
			return "用户不存在！";
		} catch (IncorrectCredentialsException ex) {
			return "密码错误！";
		} catch (AuthenticationException ex) {
			return ex.getMessage(); // 自定义报错信息
		} catch (Exception ex) {
			ex.printStackTrace();
			return "内部错误，请重试！";
		}
		return "SUCC";
	}

	private boolean isRelogin(AccountUser user) {
		Subject us = SecurityUtils.getSubject();
		if (us.isAuthenticated()) {
			return true; // 参数未改变，无需重新登录，默认为已经登录成功
		}
		return false; // 需要重新登陆
	}

	@RequestMapping("/upHtPwd")
	@ResponseBody
	public ResponseEntity upHtPwd(@RequestParam("password") String password,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		int count = 0;
		String username = (String) request.getSession()
				.getAttribute("username");
		if (username.equals("") || username == null) {
			return new ResponseEntity("false", "操作失败！");
		}
		String salt = CommTool.getSalt();
		count = auService.upHtPwd(username, password, salt);
		if (count == 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");
	}

	@RequestMapping("/addAccount")
	@ResponseBody
	public ResponseEntity addDev(AccountUser au) {
		// TODO Auto-generated method stub
		int count = 0;
		int isOnly = 0;
		isOnly = auService.getAuNum(au.getUsername());
		if (isOnly >= 1) {
			return new ResponseEntity("NO", "账号冲突！");
		}
		if (au.getId() == 0) {
			// 新增
			count = auService.addAccount(au);
		} else {
			// 修改
			count = auService.upAccount(au);
		}

		if (count == 1) {
			return new ResponseEntity("OK", "操作成功！");
		}
		return new ResponseEntity("NO", "操作失败！");
	}

	@RequestMapping("/delAccount")
	@ResponseBody
	public ResponseEntity deleteDev(String strIds) {
		// TODO Auto-generated method stub
		int count = 0;
		List<Long> ids = Utils.stringToLongList(strIds, ",");
		count = auService.deleteAccounts(ids);
		if (count >= 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");
	}

	@RequestMapping("/upAccountZt")
	@ResponseBody
	public ResponseEntity updateDevZt(String strIds,
			@RequestParam("status") String status) {
		// TODO Auto-generated method stub
		int count = 0;
		List<Long> ids = Utils.stringToLongList(strIds, ",");
		count = auService.upAccountStatus(ids, status);
		if (count >= 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");
	}

	// 用户列表
	@RequestMapping("/accountsCx")
	@ResponseBody
	public List<AccountUser> accountsCx(
			@RequestParam("username") String username) {
		List<AccountUser> list = new ArrayList<AccountUser>();
		list = auService.getAuByUsername(username);
		return list;
	}

	@RequestMapping("/accountsSx")
	@ResponseBody
	public List<AccountUser> accountsSx() {
		List<AccountUser> list = new ArrayList<AccountUser>();
		list = auService.getAuByUsername("");
		return list;
	}

	@RequestMapping("/roleList")
	@ResponseBody
	public List<AccountUser> roleList() {
		List<AccountUser> list = new ArrayList<AccountUser>();
		list = auService.getRoles();
		return list;
	}

	@RequestMapping("/UserDownLoad")
	@ResponseBody
	public ResponseEntity UserDownLoad(HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		int count = 0;
		String photoUrl = "http://192.168.0.26:8080/uploadVedio/11.mp4";
		String fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
		// System.out.println("fileName---->"+fileName);
		// String filePath = "c:" + File.separator + "MST" + File.separator
		// + "uploadVedio";
		URL url = new URL(photoUrl);
		URLConnection conn = url.openConnection();
		InputStream inStream = conn.getInputStream();
		OutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DownLoadUtils.saveUrlAs(photoUrl, out, fileName, "GET");
		return new ResponseEntity("true", "操作成功！");
	}

}
