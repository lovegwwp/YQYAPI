package com.jyss.yqy.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JRecord;

public interface JRecordMapper {
    
    
    //更新pv值
	int updateJRecordByUid(@Param("uId")int uId);
	
	//查询所有用户
	List<JRecord> selectJRecord();
	
	//根据父id查询
	List<JRecord> selectJRecordByPid(@Param("parentId")int parentId);
	
	//根据用户id查询
	List<JRecord> selectJRecordByUid(@Param("uId")int uId);
    
  
}