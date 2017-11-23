package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljResult;

public interface JBonusGljService {
	
	
	public JBonusGljResult getJBonusGlj(@Param("uId") int uId);
	
    public List<JBonusGlj> selectJBonusGljByDay(@Param("uId") int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public List<JBonusGlj> selectJBonusGljByMonth(@Param("uId") int uId, @Param("month")String month);

}
