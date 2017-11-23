package com.jyss.yqy.action;

import java.util.List;

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
	public JBonusFdjResult getJBonusFdj(int uId){
		JBonusFdjResult result = jBonusFdjService.getJBonusFdj(uId);
		return result;
	}
	
	/**
	 * 按天查询
	 */
	@RequestMapping("/showFdjByDay")
	@ResponseBody
	public List<JBonusFdj> selectFdjByDay(int uId,String beginTime,String endTime){
		List<JBonusFdj> jBonusListByDay = jBonusFdjService.selectJBonusFdjByDay(uId, beginTime, endTime);
	return jBonusListByDay;
		
	}
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showFdjByMonth")
	@ResponseBody
	public List<JBonusFdj> selectFdjByMonth(int uId,String month){
		List<JBonusFdj> jBonusListByMonth = jBonusFdjService.selectJBonusFdjByMonth(uId, month);
		return jBonusListByMonth;
		
	}

}
