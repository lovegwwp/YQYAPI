package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.UUserRRecordBExample;
import org.springframework.stereotype.Repository;

@Repository
public interface UUserRRecordBMapper {

	int insert(UUserRRecordB record);

	List<UUserRRecordB> selectByExample(UUserRRecordBExample example);

	int updateByPrimaryKeySelective(UUserRRecordB record);

	List<UUserRRecordB> getRecordB(@Param("uId") String uId,
			@Param("rId") String rId, @Param("status") String status);

	//////用户更改等级
	int updateTypeByUid(@Param("type") String type,@Param("rId") String rId, @Param("status") String status);

	List<UUserRRecordB> getRecordBGroupByRid();

}