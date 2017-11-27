package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.jsonEntity.UserBean;

public interface UserRecordBService {
	
	Map<String,String> insertUserRecordB(String uuid,String bCode);

}
