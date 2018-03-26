package com.jyss.yqy.mapper;

import com.jyss.yqy.entity.JBonusScj;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JBonusScjMapper {
    
    
    //查询昨日的pv值
	List<JBonusScj> selectJBonusScjByUid(@Param("uId")int uId);
    
    //查询总收益
    float selectTotalPv(@Param("uId")int uId);
    
    //查询本周列表
    List<JBonusScj> selectJBonusScjWek(@Param("uId")int uId);
    
    //按日查询
    List<JBonusScj> selectJBonusScjByDay(@Param("uId")int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按两个日期查询总pv
    List<JBonusScj> selectScjTotalByDay(@Param("uId")int uId, @Param("beginTime")String beginTime,@Param("endTime")String endTime);
    
    //按月查询
    List<JBonusScj> selectJBonusScjByMonth(@Param("uId")int uId, @Param("month")String month);
    
    //按月查询总pv
    List<JBonusScj> selectScjTotalByMonth(@Param("uId")int uId, @Param("month")String month);
    
    //添加市场奖
    int insertBonusScj(JBonusScj jbonusScj);
    
    //查询所有人当日的收益
    List<JBonusScj> selectEveryDayEarnings();
}