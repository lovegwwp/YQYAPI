package com.jyss.yqy.service;


import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusScjResult;


public interface JBonusScjService {
	
	
	public JBonusScjResult selectJBonusScjByUid(@Param("uId") int uId);
	
    public JBonusScjResult selectJBonusScjByDay(@Param("uId") int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public JBonusScjResult selectJBonusScjByMonth(@Param("uId") int uId, @Param("month")String month);

}
