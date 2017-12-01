package com.jyss.yqy.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusFxjResult;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JBonusFdjService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;



@Controller
@RequestMapping("/fxj")
public class JBonusFxjAction {
	
	@Autowired
	private JBonusFdjService jBonusFdjService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;
	
	/**
	 * 分销奖展示
	 */
	
	@RequestMapping("/showFxj")
	@ResponseBody
	public Map<String, Object> getJBonusFxj(String token){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				//JBonusFdjResult result = jBonusFdjService.getJBonusFdj(userBean.getId());
				List<JBonusFxj> list1 = new ArrayList<JBonusFxj>();
				JBonusFxj bonusFxj1 = new JBonusFxj();
				bonusFxj1.setName("一度");
				bonusFxj1.setAmount(99.88f);
				bonusFxj1.setCreated("2017-11-12");
				list1.add(bonusFxj1);
				
				JBonusFxj bonusFxj2 = new JBonusFxj();
				bonusFxj2.setName("二度");
				bonusFxj2.setAmount(99.88f);
				bonusFxj2.setCreated("2017-11-12");
				list1.add(bonusFxj2);
				
				JBonusFxj bonusFxj3 = new JBonusFxj();
				bonusFxj3.setName("三度");
				bonusFxj3.setAmount(99.88f);
				bonusFxj3.setCreated("2017-11-12");
				list1.add(bonusFxj3);
				
				JBonusFxjResult result = new JBonusFxjResult();
				result.setEarnings(678.00f);
				result.setTotal(1876.00f);
				result.setData(list1);
				
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
	@RequestMapping("/showFxjByDay")
	@ResponseBody
	public Map<String, Object> selectFxjByDay(String token,String beginTime,String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				//JBonusFdjResult result = jBonusFdjService.selectJBonusFdjByDay(userBean.getId(), beginTime, endTime);
				
				List<JBonusFxj> list1 = new ArrayList<JBonusFxj>();
				JBonusFxj bonusFxj1 = new JBonusFxj();
				bonusFxj1.setName("一度");
				bonusFxj1.setAmount(99.88f);
				bonusFxj1.setCreated("2017-11-12");
				list1.add(bonusFxj1);
				
				JBonusFxj bonusFxj2 = new JBonusFxj();
				bonusFxj2.setName("二度");
				bonusFxj2.setAmount(99.88f);
				bonusFxj2.setCreated("2017-11-12");
				list1.add(bonusFxj2);
				
				JBonusFxj bonusFxj3 = new JBonusFxj();
				bonusFxj3.setName("三度");
				bonusFxj3.setAmount(99.88f);
				bonusFxj3.setCreated("2017-11-12");
				list1.add(bonusFxj3);
				
				JBonusFxjResult result = new JBonusFxjResult();
				result.setEarnings(null);
				result.setTotal(1876.00f);
				result.setData(list1);
				
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
	@RequestMapping("/showFxjByMonth")
	@ResponseBody
	public Map<String, Object> selectFxjByMonth(String token,String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList !=null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
			if(list != null && list.size()>0){
				UserBean userBean = list.get(0);
				
				List<JBonusFxj> list1 = new ArrayList<JBonusFxj>();
				JBonusFxj bonusFxj1 = new JBonusFxj();
				bonusFxj1.setName("一度");
				bonusFxj1.setAmount(99.88f);
				bonusFxj1.setCreated("2017-11-12");
				list1.add(bonusFxj1);
				
				JBonusFxj bonusFxj2 = new JBonusFxj();
				bonusFxj2.setName("二度");
				bonusFxj2.setAmount(99.88f);
				bonusFxj2.setCreated("2017-11-12");
				list1.add(bonusFxj2);
				
				JBonusFxj bonusFxj3 = new JBonusFxj();
				bonusFxj3.setName("三度");
				bonusFxj3.setAmount(99.88f);
				bonusFxj3.setCreated("2017-11-12");
				list1.add(bonusFxj3);
				
				JBonusFxjResult result = new JBonusFxjResult();
				result.setEarnings(null);
				result.setTotal(1876.00f);
				result.setData(list1);
				
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
		}
		map.put("status", "false");
		map.put("code", "-2");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
		
		
	}

}
