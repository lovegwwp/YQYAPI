package com.jyss.yqy.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface AlipayAppService {


	public Map<String, Object> getDLROrderString( String filePath,  float money,
			 int gmID);
	
	public Map<String, Object> getYmzOrderString(
			@RequestParam("filePath") String filePath,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,
			@RequestParam("spID") int spID) ;

	public Map<String, Object> getHhrOrderString(
			@RequestParam("filePath") String filePath,@RequestParam("userElec") int userElec,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,@RequestParam("hhrmoney") float hhrmoney,
			@RequestParam("spID") int spID,	@RequestParam("type") int type,@RequestParam("payPwd") String payPwd) ;

	public Map<String, Object> getGoodOrderString(
			@RequestParam("filePath") String filePath,@RequestParam("userElec") int userElec,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,@RequestParam("hhrmoney") float hhrmoney,
			@RequestParam("spID") int spID,	@RequestParam("type") int type,@RequestParam("payPwd") String payPwd) ;
	
	public Map<String,String>  checkResponseParams(Map<String, String[]> requestParams) ;

}
