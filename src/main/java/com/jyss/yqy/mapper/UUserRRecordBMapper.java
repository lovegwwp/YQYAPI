package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.UUserRRecordBExample;

public interface UUserRRecordBMapper {
	int countByExample(UUserRRecordBExample example);

	int deleteByExample(UUserRRecordBExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(UUserRRecordB record);

	int insertSelective(UUserRRecordB record);

	List<UUserRRecordB> selectByExample(UUserRRecordBExample example);

	UUserRRecordB selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") UUserRRecordB record,
			@Param("example") UUserRRecordBExample example);

	int updateByExample(@Param("record") UUserRRecordB record,
			@Param("example") UUserRRecordBExample example);

	int updateByPrimaryKeySelective(UUserRRecordB record);

	int updateByPrimaryKey(UUserRRecordB record);

	int updateByUid(@Param("uId") int uId, @Param("status") int status);

	List<UUserRRecordB> getRecordB(@Param("uId") String uId,
			@Param("rId") String rId, @Param("status") String status);

}