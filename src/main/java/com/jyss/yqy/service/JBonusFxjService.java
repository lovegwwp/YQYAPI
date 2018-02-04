package com.jyss.yqy.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusFxjResult;


public interface JBonusFxjService {
	
	JBonusFxjResult getJBonusFxj(@Param("uId")int uId);
	
	JBonusFxjResult selectJBonusFxjByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
	JBonusFxjResult selectJBonusFxjByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);
    
    Map<String, String> insertJBonusFxj();

}
