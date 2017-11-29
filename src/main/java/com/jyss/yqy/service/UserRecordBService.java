package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.jsonEntity.UserBean;

public interface UserRecordBService {
	
	Map<String,String> insertUserRecordB(@Param("uuid") String uuid,@Param("bCode") String bCode);
	
	Map<String,String> insertJBonusFdj();
	
	Map<String,String> insertJBonusGlj(@Param("uuid") String uuid);

}
