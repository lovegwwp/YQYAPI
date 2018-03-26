package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.JBonusScjResult;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JBonusScjService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;

@Controller
@RequestMapping("/scj")
public class JBonusScjAction {
	
	@Autowired
	private JBonusScjService bonusScjService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 市场奖奖展示(本周)
	 */
	
	@RequestMapping("/showScj")
	@ResponseBody
	public Map<String, Object> getJBonusScj(@RequestParam("token")String token){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				JBonusScjResult result = bonusScjService.selectJBonusScjByUid(userBean.getId());
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
	 * 按两个日期查询
	 */
	@RequestMapping("/showScjByDay")
	@ResponseBody
	public Map<String, Object> selectScjByDay(@RequestParam("token")String token,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit,
			@RequestParam("beginTime")String beginTime,@RequestParam("endTime")String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				JBonusScjResult result = bonusScjService.selectJBonusScjByDay(userBean.getId(),page,limit, beginTime, endTime);
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
	 * 按某月查询
	 */
	@RequestMapping("/showScjByMonth")
	@ResponseBody
	public Map<String, Object> selectScjByMonth(@RequestParam("token")String token,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit,
			@RequestParam("month")String month){
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				JBonusScjResult result = bonusScjService.selectJBonusScjByMonth(userBean.getId(),page,limit, month);
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

}
