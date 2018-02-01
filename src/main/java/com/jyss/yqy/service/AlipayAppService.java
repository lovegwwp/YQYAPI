package com.jyss.yqy.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface AlipayAppService {


	public Map<String, Object> getDLROrderString( String filePath,  int money,
			 int gmID);
	
	public Map<String, Object> getYmzOrderString(
			@RequestParam("filePath") String filePath,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,
			@RequestParam("spID") int spID) ;
	
	public Map<String,String>  checkResponseParams(Map<String, String[]> requestParams) ;

}
