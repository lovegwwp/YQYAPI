package com.jyss.yqy.service;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusScjResult;


public interface JBonusScjService {
	
	JBonusScjResult selectJBonusScjByUid(@Param("uId")int uId);
	
    JBonusScjResult selectJBonusScjByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    JBonusScjResult selectJBonusScjByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);

}
