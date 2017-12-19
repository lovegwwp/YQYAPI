package com.jyss.yqy.service;

import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.jyss.yqy.entity.JBonusGljResult;

public interface JBonusGljService {
	
	
	public JBonusGljResult getJBonusGlj(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit);
	
    public JBonusGljResult selectJBonusGljByDay(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    public JBonusGljResult selectJBonusGljByMonth(@Param("uId")int uId,@Param("page")int page,@Param("limit")int limit,
			@Param("month")String month);
    
    //public Map<String,String> insertScore();

}
