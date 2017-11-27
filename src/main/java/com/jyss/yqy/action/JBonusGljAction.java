package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljResult;
import com.jyss.yqy.service.JBonusGljService;

@Controller
@RequestMapping("/glj")
public class JBonusGljAction {
	
	@Autowired
	private JBonusGljService jBonusGljService;
	
	
	/**
	 * 管理奖展示(本周)
	 */
	
	@RequestMapping("/showGlj")
	@ResponseBody
	public Map<String, Object> getJBonusGlj(int uId){
		Map<String, Object> map = new HashMap<String,Object>();
		JBonusGljResult result = jBonusGljService.getJBonusGlj(uId);
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
	
	/**
	 * 按天查询
	 */
	@RequestMapping("/showGljByDay")
	@ResponseBody
	public Map<String, Object> selectGljByDay(int uId,String beginTime,String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		List<JBonusGlj> jBonusListByDay = jBonusGljService.selectJBonusGljByDay(uId, beginTime, endTime);
		map.put("status", "true");
		map.put("code", "0");
		map.put("message", "查询成功！");
		map.put("data", jBonusListByDay);
		return map;
		
	}
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showGljByMonth")
	@ResponseBody
	public Map<String, Object> selectGljByMonth(int uId,String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<JBonusGlj> jBonusListByMonth = jBonusGljService.selectJBonusGljByMonth(uId, month);
		map.put("status", "true");
		map.put("code", "0");
		map.put("message", "查询成功！");
		map.put("data", jBonusListByMonth);
		return map;
		
	}

}
