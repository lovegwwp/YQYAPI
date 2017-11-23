package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.JBonusGljExample;

public interface JBonusGljMapper {
    int countByExample(JBonusGljExample example);

    int deleteByExample(JBonusGljExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(JBonusGlj record);

    int insertSelective(JBonusGlj record);

    List<JBonusGlj> selectByExample(JBonusGljExample example);

    JBonusGlj selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JBonusGlj record, @Param("example") JBonusGljExample example);

    int updateByExample(@Param("record") JBonusGlj record, @Param("example") JBonusGljExample example);

    int updateByPrimaryKeySelective(JBonusGlj record);

    int updateByPrimaryKey(JBonusGlj record);
    
    //查询今天的收益
    double selectEarnings(@Param("parentId")int parentId);
    
    //查询总收益
    double selectTotal(@Param("parentId")int parentId);
    
    //查询本周列表
    List<JBonusGlj> selectJBonusGljWek(@Param("parentId")int parentId);
    
    //按日查询
    List<JBonusGlj> selectJBonusGljByDay(@Param("parentId")int parentId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按月查询
    List<JBonusGlj> selectJBonusGljByMonth(@Param("parentId")int parentId, @Param("month")String month);
}