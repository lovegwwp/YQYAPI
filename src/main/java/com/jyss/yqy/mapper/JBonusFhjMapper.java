package com.jyss.yqy.mapper;


import com.jyss.yqy.entity.JBonusFhj;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBonusFhjMapper {

    //插入分红奖
    int insert(JBonusFhj jBonusFhj);

    //查询当日分红
    float selectJBonusFhjToday(@Param("uId") Integer uId);

    //查询历史总分红
    float selectTotal(@Param("uId") Integer uId);

    //查询本周列表
    List<JBonusFhj> selectJBonusFhjWek(@Param("uId") Integer uId);

    //按两个日期查询列表
    List<JBonusFhj> selectJBonusFhjByDay(@Param("uId") Integer uId,@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按两个日期查询总收益
    float selectFhjTotalByDay(@Param("uId") Integer uId,@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按月查询列表
    List<JBonusFhj> selectJBonusFhjByMonth(@Param("uId") Integer uId,@Param("month") String month);

    //按月查询总收益
    float selectFhjTotalByMonth(@Param("uId") Integer uId,@Param("month") String month);

    //查询所有人今日的总收益
    List<JBonusFhj> selectEveryDayEarnings();


}