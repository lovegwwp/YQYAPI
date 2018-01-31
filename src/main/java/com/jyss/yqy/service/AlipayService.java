package com.jyss.yqy.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AlipayService {

	public Map<String, Object> addDlrOrder(@Param("filePath") String filePath,
			@Param("money") int money, @Param("gmID") int gmID);

	public Map<String, Object> addDlrOrder2(@Param("filePath") String filePath,
			@Param("money") int money, @Param("gmID") int gmID);

	public Map<String, Object> addYmzOrder2(@Param("filePath") String filePath,
			@Param("gmID") int gmID, @Param("gmNum") int gmNum,
			@Param("spID") int spID);
	
	//public String  getOrderString(Map<String, String> paramsMap);
	
	//public String checkResponseParams(Map<String, String[]> requestParams) ;

}
