package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.service.JRecordService;

@Controller
@RequestMapping("/jrc")
public class JRecordAction {
	@Autowired
	private JRecordService recordService;
	
	/**
	 * 添加市场用户
	 */
	@RequestMapping(value="/addJRecord", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> addJRecord(@RequestParam("uAccount") String uAccount,@RequestParam("pAccount") String pAccount,
			@RequestParam("depart")Integer depart){
		if(StringUtils.isEmpty(depart)){
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", "请选择分配的市场！");
			return map;
		}
		Map<String, String> map = recordService.insertJRecord(uAccount,pAccount,depart);
		return map;
		
	}
	
	
	/**
	 * 展示市场用户
	 */
	@RequestMapping("/user/list")
	@ResponseBody
	public List<JRecord> getUserList(){
		List<JRecord> list = recordService.getJRecordList();
		return list;
	}
	
	
	/**
	 * 修改市场用户
	 */
	@RequestMapping(value="/updateJrc",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updateJRecord(@RequestParam("id")int id,@RequestParam("account")String account){
		Map<String, String> map = recordService.updateJRecord(id, account);
		return map;
	}
	
	
	/**
	 * 删除市场用户
	 */
	@RequestMapping("/deleteJrc")
	@ResponseBody
	public Map<String,String> deleteJRecord(@RequestParam("id")int id){
		Map<String, String> map = recordService.deleteJRecord(id);
		return map;
	}

	

}
