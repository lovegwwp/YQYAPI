package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjResult;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JBonusFdjService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;



@Controller
@RequestMapping("/fdj")
public class JBonusFdjAction {
	
	@Autowired
	private JBonusFdjService jBonusFdjService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 辅导奖展示(本周)
	 */
	
	@RequestMapping("/showFdj")
	@ResponseBody
	public Map<String, Object> getJBonusFdj(String token){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				JBonusFdjResult result = jBonusFdjService.getJBonusFdj(userBean.getId());
				if(result != null){
					map.put("status", "true");
					map.put("code", "0");
					map.put("message", "查询成功！");
					map.put("data", result);
					return map;
				}
				map.put("status", "false");
				map.put("code", "-1");
				map.put("message", "查询失败，请稍后再试！");
				map.put("data", "");
				return map;
				
			}
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
	@RequestMapping("/showFdjByDay")
	@ResponseBody
	public Map<String, Object> selectFdjByDay(String token,String beginTime,String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				List<JBonusFdj> jBonusListByDay = jBonusFdjService.selectJBonusFdjByDay(userBean.getId(), beginTime, endTime);
				map.put("status", "true");
				map.put("code", "0");
				map.put("message", "查询成功！");
				map.put("data", jBonusListByDay);
				return map;
			}
		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
	}
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showFdjByMonth")
	@ResponseBody
	public Map<String, Object> selectFdjByMonth(String token,String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				List<JBonusFdj> jBonusListByMonth = jBonusFdjService.selectJBonusFdjByMonth(userBean.getId(), month);
				map.put("status", "true");
				map.put("code", "0");
				map.put("message", "查询成功！");
				map.put("data", jBonusListByMonth);
				return map;
			}
		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
		
	}

}
