package com.jyss.yqy.mapper;


import com.jyss.yqy.entity.JBonusGxj;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBonusGxjMapper {

    //插入共享奖
    int insert(JBonusGxj jBonusGxj);

    //查询昨日共享奖
    float selectJBonusGxjDay(@Param("uId") Integer uId);

    //查询历史总共享奖
    float selectTotal(@Param("uId") Integer uId);

    //查询本周列表
    List<JBonusGxj> selectJBonusGxjWek(@Param("uId") Integer uId);

    //按两个日期查询列表
    List<JBonusGxj> selectJBonusGxjByDay(@Param("uId") Integer uId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按两个日期查询总收益
    float selectGxjTotalByDay(@Param("uId") Integer uId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按月查询列表
    List<JBonusGxj> selectJBonusGxjByMonth(@Param("uId") Integer uId, @Param("month") String month);

    //按月查询总收益
    float selectGxjTotalByMonth(@Param("uId") Integer uId, @Param("month") String month);



}