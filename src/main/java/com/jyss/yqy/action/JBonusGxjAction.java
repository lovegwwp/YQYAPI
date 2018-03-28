package com.jyss.yqy.action;

import com.jyss.yqy.entity.JBonusResult;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JBonusGxjService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/gxj")
public class JBonusGxjAction {
	
	@Autowired
	private JBonusGxjService jBonusGxjService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 共享奖展示（昨日）
	 */
	@RequestMapping("/showGxj")
	@ResponseBody
	public Map<String, Object> getJBonusFhj(@RequestParam("token")String token){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size() == 1){
				UserBean userBean = list.get(0);
				JBonusResult result = jBonusGxjService.getJBonusGxj(userBean.getId());
				if(result == null){
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
	@RequestMapping("/showGxjByDay")
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
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size() == 1){
				UserBean userBean = list.get(0);
				JBonusResult result = jBonusGxjService.selectJBonusGxjByDay(userBean.getId(), page, limit, beginTime, endTime);
				if(result == null){
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
	@RequestMapping("/showGxjByMonth")
	@ResponseBody
	public Map<String, Object> selectFhjByMonth(@RequestParam("token")String token,
												@RequestParam(value = "page", required = true) int page,
												@RequestParam(value = "limit", required = true) int limit,
												@RequestParam("month")String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList != null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size() == 1){
				UserBean userBean = list.get(0);
				JBonusResult result = jBonusGxjService.selectJBonusGxjByMonth(userBean.getId(), page, limit, month);
				if(result == null){
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
		}
		map.put("status", "false");
		map.put("code", "-2");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
	}



}
