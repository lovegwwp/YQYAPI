package com.jyss.yqy.mapper;

import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.UMobileLoginExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UMobileLoginMapper {
    int countByExample(UMobileLoginExample example);

    int deleteByExample(UMobileLoginExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UMobileLogin record);

    int insertSelective(UMobileLogin record);

    List<UMobileLogin> selectByExample(UMobileLoginExample example);

    UMobileLogin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UMobileLogin record, @Param("example") UMobileLoginExample example);

    int updateByExample(@Param("record") UMobileLogin record, @Param("example") UMobileLoginExample example);

    int updateByPrimaryKeySelective(UMobileLogin record);

    int updateByPrimaryKey(UMobileLogin record);
    
    //根据token查询用户
    List<UMobileLogin> findUserByToken(@Param("token") String token);
}