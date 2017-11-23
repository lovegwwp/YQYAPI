package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjResult;



public interface JBonusFdjService {
	
	
	public JBonusFdjResult getJBonusFdj(@Param("uId") int uId);
	
    public List<JBonusFdj> selectJBonusFdjByDay(@Param("uId") int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public List<JBonusFdj> selectJBonusFdjByMonth(@Param("uId") int uId, @Param("month")String month);

}
