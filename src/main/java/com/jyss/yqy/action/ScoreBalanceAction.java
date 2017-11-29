package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.service.UMobileLoginService;

@Controller
public class ScoreBalanceAction {
	@Autowired
	private ScoreBalanceService sbService;
	@Autowired
	private UMobileLoginService uMobileLoginService;

	@RequestMapping("/b/getCashScoreBalance")
	@ResponseBody
	public Map<String, Object> getCashScoreBalance(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "身份过期！");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		PageHelper.startPage(page, limit);// 分页语句
		List<ScoreBalance> csbListBy = sbService.getCashScoreBalance(uuuid);
		PageInfo<ScoreBalance> pageInfoBy = new PageInfo<ScoreBalance>(
				csbListBy);
		map.put("status", "true");
		map.put("message", "获取列表成功！");
		map.put("code", "0");
		map.put("data", new Page<ScoreBalance>(pageInfoBy));
		return map;
	}

	@RequestMapping("/b/getShoppingScoreBalance")
	@ResponseBody
	public Map<String, Object> getShoppingScoreBalance(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "身份过期！");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		PageHelper.startPage(page, limit);// 分页语句
		List<ScoreBalance> ssbListBy = sbService.getShoppingScoreBalance(uuuid);
		PageInfo<ScoreBalance> pageInfoBy = new PageInfo<ScoreBalance>(
				ssbListBy);
		map.put("status", "true");
		map.put("message", "获取列表成功！");
		map.put("code", "0");
		map.put("data", new Page<ScoreBalance>(pageInfoBy));
		return map;
	}

}
