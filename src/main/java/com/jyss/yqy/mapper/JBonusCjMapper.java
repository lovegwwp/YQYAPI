package com.jyss.yqy.mapper;


import com.jyss.yqy.entity.JBonusCj;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBonusCjMapper {

    //插入层奖
    int insert(JBonusCj jBonusCj);

    //查询今日金额
    float selectEarnings(@Param("uId")Integer uId);

    //查询历史总金额
    float selectTotal(@Param("uId")Integer uId);

    //查询列表
    List<JBonusCj> selectJBonusCj(@Param("uId")Integer uId);

}