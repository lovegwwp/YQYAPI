package com.jyss.yqy.mapper;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjExample;
import com.jyss.yqy.entity.JBonusGlj;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface JBonusFdjMapper {
    int countByExample(JBonusFdjExample example);

    int deleteByExample(JBonusFdjExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(JBonusFdj record);

    int insertSelective(JBonusFdj record);

    List<JBonusFdj> selectByExample(JBonusFdjExample example);

    JBonusFdj selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JBonusFdj record, @Param("example") JBonusFdjExample example);

    int updateByExample(@Param("record") JBonusFdj record, @Param("example") JBonusFdjExample example);

    int updateByPrimaryKeySelective(JBonusFdj record);

    int updateByPrimaryKey(JBonusFdj record);
    
    //查询昨日的收益
    double selectEarnings(@Param("parentId")int parentId);
    
    //查询总收益
    double selectTotal(@Param("parentId")int parentId);
    
    //查询本周列表
    List<JBonusFdj> selectJBonusFdjWek(@Param("parentId")int parentId);
    
    //按日查询
    List<JBonusFdj> selectJBonusFdjByDay(@Param("parentId")int parentId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按日查询总收益
    double selectFdjTotalByDay(@Param("parentId")int parentId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按月查询
    List<JBonusFdj> selectJBonusFdjByMonth(@Param("parentId")int parentId, @Param("month")String month);
    
    //按月查询总收益
    double selectFdjTotalByMonth(@Param("parentId")int parentId, @Param("month")String month);
    
    //查询所有人当日的收益
    List<JBonusFdj> selectEveryDayEarnings();
}