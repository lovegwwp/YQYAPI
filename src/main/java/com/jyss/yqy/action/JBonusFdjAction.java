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
import com.jyss.yqy.service.JBonusFdjService;



@Controller
@RequestMapping("/fdj")
public class JBonusFdjAction {
	
	@Autowired
	private JBonusFdjService jBonusFdjService;
	
	
	/**
	 * 辅导奖展示(本周)
	 */
	
	@RequestMapping("/showFdj")
	@ResponseBody
	public Map<String, Object> getJBonusFdj(int uId){
		Map<String, Object> map = new HashMap<String,Object>();
		JBonusFdjResult result = jBonusFdjService.getJBonusFdj(uId);
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
	@RequestMapping("/showFdjByDay")
	@ResponseBody
	public Map<String, Object> selectFdjByDay(int uId,String beginTime,String endTime){
		Map<String, Object> map = new HashMap<String,Object>();
		List<JBonusFdj> jBonusListByDay = jBonusFdjService.selectJBonusFdjByDay(uId, beginTime, endTime);
		map.put("status", "true");
		map.put("code", "0");
		map.put("message", "查询成功！");
		map.put("data", jBonusListByDay);
		return map;
		
	}
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showFdjByMonth")
	@ResponseBody
	public Map<String, Object> selectFdjByMonth(int uId,String month){
		Map<String, Object> map = new HashMap<String,Object>();
		List<JBonusFdj> jBonusListByMonth = jBonusFdjService.selectJBonusFdjByMonth(uId, month);
		map.put("status", "true");
		map.put("code", "0");
		map.put("message", "查询成功！");
		map.put("data", jBonusListByMonth);
		return map;
		
	}

}
