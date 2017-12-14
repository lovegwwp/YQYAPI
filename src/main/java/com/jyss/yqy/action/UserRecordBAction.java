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
import com.jyss.yqy.service.JBonusFxjService;
import com.jyss.yqy.service.JBonusGljService;
import com.jyss.yqy.service.JRecordService;
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
	@Autowired
	private JRecordService recordService;
	@Autowired
	private JBonusGljService bonusGljService;
	@Autowired
	private JBonusFxjService bonusFxjService;
	
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
	 * 计算辅导奖和积分
	 */
	
	@RequestMapping("/fdj/computeFDJ")
	@ResponseBody
	public void insertJBonusFdj(){
		Map<String, String> map = userRecordBService.insertJBonusFdj();
		logger.info(map.get("message"));
	}
	
	
	/**
	 * 计算市场奖和积分
	 */
	@RequestMapping("/scj/computeSCJ")
	@ResponseBody
	public void insertJBonusScj(){
		Map<String, String> map = recordService.insertJBonusScj();
		logger.info(map.get("message"));
	}
	
	
	/**
	 * 计算管理奖的积分
	 */
	@RequestMapping("/glj/computeScore")
	@ResponseBody
	public void insertScore(){
		Map<String, String> map = bonusGljService.insertScore();
		logger.info(map.get("message"));
	}
	
	
	/**
	 * 计算分销奖和积分
	 */
	@RequestMapping("/fxj/computeFXJ")
	@ResponseBody
	public void insertJBonusFxj(){
		Map<String, String> map = bonusFxjService.insertJBonusFxj();
		logger.info(map.get("message"));
	}

}
