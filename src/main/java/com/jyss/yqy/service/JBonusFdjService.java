package com.jyss.yqy.service;

import org.apache.ibatis.annotations.Param;
import com.jyss.yqy.entity.JBonusFdjResult;


public interface JBonusFdjService {
	
	public JBonusFdjResult getJBonusFdj(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit);
	
    public JBonusFdjResult selectJBonusFdjByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public JBonusFdjResult selectJBonusFdjByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);

}
