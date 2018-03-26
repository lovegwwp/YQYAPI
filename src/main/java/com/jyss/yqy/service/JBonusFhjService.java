package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusFhjResult;
import org.apache.ibatis.annotations.Param;

public interface JBonusFhjService {

	JBonusFhjResult getJBonusFhj(@Param("uId") Integer uId);

	JBonusFhjResult selectJBonusFhjByDay(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
                                                @Param("beginTime") String beginTime, @Param("endTime") String endTime);

	JBonusFhjResult selectJBonusFhjByMonth(@Param("uId") Integer uId, @Param("page") int page, @Param("limit") int limit,
                                                  @Param("month") String month);
    

}
