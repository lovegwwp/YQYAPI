package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JRecord;

public interface JRecordService {
	
	//计算市场奖
	Map<String,String> insertJBonusScj();
	
	//添加市场用户
	Map<String,String> insertJRecord(@Param("uAccount")String uAccount,@Param("pAccount")String pAccount,@Param("depart")int depart);
	
	//展示市场用户
	List<JRecord> getJRecordList();
	
	//修改市场用户
	Map<String,String> updateJRecord(@Param("id")int id,@Param("account")String account);
	
	//删除市场用户
	Map<String,String> deleteJRecord(@Param("id")int id);
	

}
