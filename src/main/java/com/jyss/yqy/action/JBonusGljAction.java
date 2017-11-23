package com.jyss.yqy.action;

import java.util.List;

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
	public JBonusGljResult getJBonusGlj(int uId){
		JBonusGljResult result = jBonusGljService.getJBonusGlj(uId);
		return result;
	}
	
	/**
	 * 按天查询
	 */
	@RequestMapping("/showGljByDay")
	@ResponseBody
	public List<JBonusGlj> selectGljByDay(int uId,String beginTime,String endTime){
		List<JBonusGlj> jBonusListByDay = jBonusGljService.selectJBonusGljByDay(uId, beginTime, endTime);
	return jBonusListByDay;
		
	}
	
	
	/**
	 * 按月查询
	 */
	@RequestMapping("/showGljByMonth")
	@ResponseBody
	public List<JBonusGlj> selectGljByMonth(int uId,String month){
		List<JBonusGlj> jBonusListByMonth = jBonusGljService.selectJBonusGljByMonth(uId, month);
		return jBonusListByMonth;
		
	}

}
