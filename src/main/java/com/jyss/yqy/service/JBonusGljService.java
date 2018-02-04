package com.jyss.yqy.service;

import org.apache.ibatis.annotations.Param;
import com.jyss.yqy.entity.JBonusGljResult;

public interface JBonusGljService {
	
	
	JBonusGljResult getJBonusGlj(@Param("uId")int uId);
	
    JBonusGljResult selectJBonusGljByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    JBonusGljResult selectJBonusGljByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);
    
    //public Map<String,String> insertScore();

}
