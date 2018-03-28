package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusResult;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface JBonusGxjService {

	JBonusResult getJBonusGxj(@Param("uId") Integer uId);

	JBonusResult selectJBonusGxjByDay(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
									  @Param("beginTime") String beginTime, @Param("endTime") String endTime);

	JBonusResult selectJBonusGxjByMonth(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
                                        @Param("month") String month);
    

	//计算共享奖
	Map<String,String> insertJBonusGxj();

}
