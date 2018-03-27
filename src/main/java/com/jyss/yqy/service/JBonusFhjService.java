package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusResult;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface JBonusFhjService {

	JBonusResult getJBonusFhj(@Param("uId") Integer uId);

	JBonusResult selectJBonusFhjByDay(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
									  @Param("beginTime") String beginTime, @Param("endTime") String endTime);

	JBonusResult selectJBonusFhjByMonth(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
										@Param("month") String month);
    
	//计算分红奖
	Map<String,String> insertJBonusFhj();

}
