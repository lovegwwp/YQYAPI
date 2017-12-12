package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljResult;

public interface JBonusGljService {
	
	
	public JBonusGljResult getJBonusGlj(@Param("uId") int uId);
	
    public JBonusGljResult selectJBonusGljByDay(@Param("uId") int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public JBonusGljResult selectJBonusGljByMonth(@Param("uId") int uId, @Param("month")String month);
    
    public Map<String,String> insertScore();

}
