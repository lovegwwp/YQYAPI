package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusFhjResult;
import org.apache.ibatis.annotations.Param;

public interface JBonusFhjService {

	JBonusFhjResult getJBonusFhj(@Param("uUUid") String uUUid);

	JBonusFhjResult selectJBonusFhjByDay(@Param("uUUid") String uUUid, @Param("page") int page, @Param("limit") int limit,
                                                @Param("beginTime") String beginTime, @Param("endTime") String endTime);

	JBonusFhjResult selectJBonusFhjByMonth(@Param("uUUid") String uUUid, @Param("page") int page, @Param("limit") int limit,
                                                  @Param("month") String month);
    

}
