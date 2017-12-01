package com.jyss.yqy.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JRecord;

public interface JRecordMapper {
    
    
    //更新pv值
	int updateJRecordByUid(@Param("uId")int uId);
	
	//查询所有
	List<JRecord> selectJRecord();
    
  
}