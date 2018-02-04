package com.jyss.yqy.service;

import org.apache.ibatis.annotations.Param;
import com.jyss.yqy.entity.JBonusFdjResult;


public interface JBonusFdjService {
	
	JBonusFdjResult getJBonusFdj(@Param("uId")int uId);
	
    JBonusFdjResult selectJBonusFdjByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    JBonusFdjResult selectJBonusFdjByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);

}
