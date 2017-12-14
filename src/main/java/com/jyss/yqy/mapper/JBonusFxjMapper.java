package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusFxj;

public interface JBonusFxjMapper {
	
	//添加分销奖
    int insert(JBonusFxj jBonusFxj);

    //查询昨日的收益
    float selectEarnings(@Param("parentId")int parentId);
    
    //查询总收益
    float selectTotal(@Param("parentId")int parentId);
    
    //查询本周列表
    List<JBonusFxj> selectJBonusFxjWek(@Param("parentId")int parentId);
    
    //按日查询
    List<JBonusFxj> selectJBonusFxjByDay(@Param("parentId")int parentId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按日查询总收益
    float selectFxjTotalByDay(@Param("parentId")int parentId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按月查询
    List<JBonusFxj> selectJBonusFxjByMonth(@Param("parentId")int parentId, @Param("month")String month);
    
    //按月查询总收益
    float selectFxjTotalByMonth(@Param("parentId")int parentId, @Param("month")String month);
    
    //查询所有人当日的收益
    List<JBonusFxj> selectEveryDayEarnings();
}