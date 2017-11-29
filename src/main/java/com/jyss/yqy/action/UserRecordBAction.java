package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserRecordBService;
import com.jyss.yqy.service.UserService;

@Controller
public class UserRecordBAction {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRecordBAction.class);
	
	@Autowired
	private UserRecordBService userRecordBService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	
	/**
	 * 绑定用户关系
	 */
	
	@RequestMapping("/b/writeCode")
	@ResponseBody
	public Map<String,String> addUserRecordB(String token,String bCode){
		List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
		if(loginList != null && loginList.size()>0){
			UMobileLogin uMobileLogin = loginList.get(0);
			Map<String, String> map = userRecordBService.insertUserRecordB(uMobileLogin.getuUuid(), bCode);
			return map;
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("code", "-3");
		map.put("status", "false");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
	}
	
	
	
	
	
	/**
	 * 给推荐人算辅导奖
	 */
	
	@RequestMapping("/fdj/computeFDJ")
	@ResponseBody
	public void insertJBonusFdj(){
		Map<String, String> map = userRecordBService.insertJBonusFdj();
		logger.info(map.get("message"));
	}

}
