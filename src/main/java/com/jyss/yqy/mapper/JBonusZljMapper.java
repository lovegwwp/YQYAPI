package com.jyss.yqy.mapper;


import com.jyss.yqy.entity.JBonusGxj;
import com.jyss.yqy.entity.JRecordZl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JBonusZljMapper {

    //插入总监助理奖
    int insert(JBonusGxj jBonusGxj);

    //查询代理市场
    List<JRecordZl> selectZlByUId(@Param("uId") Integer uId);

    //查询历史总助理奖
    float selectTotal(@Param("uId") Integer uId,@Param("sId") Integer sId);

    //查询昨日助理奖
    float selectJBonusZljDay(@Param("uId") Integer uId,@Param("sId") Integer sId);

    //查询本周列表
    List<JBonusGxj> selectJBonusZljWek(@Param("uId") Integer uId, @Param("sId") Integer sId);

    //按两个日期查询列表
    List<JBonusGxj> selectJBonusZljByDay(@Param("uId") Integer uId, @Param("sId") Integer sId,
                                         @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按两个日期查询总收益
    float selectZljTotalByDay(@Param("uId") Integer uId, @Param("sId") Integer sId,
                              @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    //按月查询列表
    List<JBonusGxj> selectJBonusZljByMonth(@Param("uId") Integer uId, @Param("sId") Integer sId, @Param("month") String month);

    //按月查询总收益
    float selectZljTotalByMonth(@Param("uId") Integer uId, @Param("sId") Integer sId, @Param("month") String month);



}