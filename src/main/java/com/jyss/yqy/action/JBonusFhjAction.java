package com.jyss.yqy.action;

import com.jyss.yqy.entity.JBonusFhjResult;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.service.JBonusFhjService;
import com.jyss.yqy.service.UMobileLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/fhj")
public class JBonusFhjAction {
	
	@Autowired
	private JBonusFhjService jBonusFhjService;
	@Autowired
	private UMobileLoginService uMobileLoginService;

	
	
	/**
	 * 分红奖展示
	 */
	@RequestMapping("/showFhj")
	@ResponseBody
	public Map<String, Object> getJBonusFhj(@RequestParam("token")String token){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			String uUuid = uMobileLogin.getuUuid();
			JBonusFhjResult result = jBonusFhjService.getJBonusFhj(uUuid);
			if(StringUtils.isEmpty(result)){
				map.put("status", "false");
				map.put("code", "-1");
				map.put("message", "查询失败，请稍后再试！");
				map.put("data", "");
				return map;
			}
			map.put("status", "true");
			map.put("code", "0");
			map.put("message", "查询成功！");
			map.put("data", result);
			return map;
		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
	}
	
	

	/**
	 * 按天查询
	 */
	@RequestMapping("/showFhjByDay")
	@ResponseBody
	public Map<String, Object> selectFhjByDay(@RequestParam("token")String token,
											  @RequestParam(value = "page", required = true) int page,
											  @RequestParam(value = "limit", required = true) int limit,
											  @RequestParam("beginTime")String beginTime,
											  @RequestParam("endTime")String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			String uUuid = uMobileLogin.getuUuid();
			JBonusFhjResult result = jBonusFhjService.selectJBonusFhjByDay(uUuid, page, limit, beginTime, endTime);
			if(StringUtils.isEmpty(result)){
				map.put("status", "false");
				map.put("code", "-1");
				map.put("message", "查询失败，请稍后再试！");
				map.put("data", "");
				return map;
			}
			map.put("status", "true");
			map.put("code", "0");
			map.put("message", "查询成功！");
			map.put("data", result);
			return map;
		}
		map.put("status", "false");
		map.put("code", "-2");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
	}
	
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showFhjByMonth")
	@ResponseBody
	public Map<String, Object> selectFhjByMonth(@RequestParam("token")String token,
												@RequestParam(value = "page", required = true) int page,
												@RequestParam(value = "limit", required = true) int limit,
												@RequestParam("month")String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			String uUuid = uMobileLogin.getuUuid();
			JBonusFhjResult result = jBonusFhjService.selectJBonusFhjByMonth(uUuid, page, limit, month);
			if(StringUtils.isEmpty(result)){
				map.put("status", "false");
				map.put("code", "-1");
				map.put("message", "查询失败，请稍后再试！");
				map.put("data", "");
				return map;
			}
			map.put("status", "true");
			map.put("code", "0");
			map.put("message", "查询成功！");
			map.put("data", result);
			return map;
		}
		map.put("status", "false");
		map.put("code", "-2");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
	}



}
