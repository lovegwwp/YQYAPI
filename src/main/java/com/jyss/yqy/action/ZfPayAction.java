package com.jyss.yqy.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.AlipayService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.WxpayService;
import com.jyss.yqy.utils.CommTool;

@Controller
public class ZfPayAction {

	@Autowired
	private AlipayService aliService;
	@Autowired
	private WxpayService wxService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;

	// type///// 支付方式：1=支付宝，2=微信，3=现金支付
	@RequestMapping(value = "/b/dlrOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> dlrOrder(@RequestParam int money,
			@RequestParam String type, @RequestParam String token,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String filePath = request.getSession().getServletContext()
				.getRealPath("/");
		int index = filePath.indexOf("YQYAPI");
		filePath = filePath.substring(0, index) + "orderCodePng/";
		File f = new File(filePath);
		CommTool.judeDirExists(f);
		// //验证token 获取购买人ID===gmID
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
		String uuid = uMobileLogin.getuUuid();
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList == null || ubList.size() == 0) {
			map.put("status", "false");
			map.put("message", "用户信息错误！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}

		UserBean ub = ubList.get(0);
		int gmID = ub.getId();
		// ///判断支付方式=== 走不同支付
		if (type.equals("1")) {
			map = aliService.addDlrOrder2(filePath, money, gmID);
		} else if (type.equals("2")) {
			map = wxService.dlrWxpay(filePath, money, gmID);
		} else {
			map.put("status", "false");
			map.put("message", "商品信息错误");
			map.put("code", "-3");
			map.put("data", "");
			return map;
		}
		return map;

	}

	@RequestMapping(value = "/b/ymzOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ymzOrder(@RequestParam String type,
			@RequestParam String token, @RequestParam int spId,
			@RequestParam int num, HttpServletRequest request) {
		Map<String, Object> mmap = new HashMap<String, Object>();
		String filePath = request.getSession().getServletContext()
				.getRealPath("/");
		int index = filePath.indexOf("YQYAPI");
		filePath = filePath.substring(0, index) + "orderCodePng/";
		File f = new File(filePath);
		CommTool.judeDirExists(f);
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			mmap.put("status", "false");
			mmap.put("message", "请重新登录");
			mmap.put("code", "-1");
			mmap.put("data", "");
			return mmap;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuid = uMobileLogin.getuUuid();
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList == null || ubList.size() == 0) {
			mmap.put("status", "false");
			mmap.put("message", "用户信息错误！");
			mmap.put("code", "-2");
			mmap.put("data", "");
			return mmap;
		}

		UserBean ub = ubList.get(0);
		int gmID = ub.getId();
		// ///判断支付方式=== 走不同支付
		if (type.equals("1")) {
			mmap = aliService.addYmzOrder2(filePath, gmID, num, spId);
		} else if (type.equals("2")) {

		} else if (type.equals("3")) {

		} else {
			mmap.put("status", "false");
			mmap.put("message", "商品信息错误");
			mmap.put("code", "-3");
			mmap.put("data", "");
			return mmap;

		}

		return mmap;

	}

}
