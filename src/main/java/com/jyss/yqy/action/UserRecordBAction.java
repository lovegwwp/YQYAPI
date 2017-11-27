package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.service.UserRecordBService;

@Controller
public class UserRecordBAction {
	
	@Autowired
	private UserRecordBService userRecordBService;
	
	/**
	 * 绑定用户关系&给推荐人算管理奖
	 */
	
	@RequestMapping("/b/writeCode")
	@ResponseBody
	public  Map<String,String> addUserRecordB(String uuid,String bCode){
		Map<String, String> map = userRecordBService.insertUserRecordB(uuid, bCode);
		return map;
	}

}
