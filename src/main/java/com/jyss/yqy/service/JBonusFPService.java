package com.jyss.yqy.service;


import org.apache.ibatis.annotations.Param;

public interface JBonusFPService {

    //奖金分配接口
    void insertScoreBalance(@Param("id") int id,@Param("money") float money,@Param("category") int category);
}
