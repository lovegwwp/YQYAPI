package com.jyss.yqy.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface JRecordMapper {
    
    //更新pv值
	int updateJRecordByUid(@Param("pv")String pv,@Param("jyPv")String jyPv,@Param("uId")int uId);
	
	//查询所有用户id
	List<JRecord> selectJRecord();
	
	//根据父id查询
	List<JRecord> selectJRecordByPid(@Param("parentId")int parentId);
	
	//根据用户id查询
	List<JRecord> selectJRecordByUid(@Param("uId")int uId);
	
	//将pv值置0
	int updateJRecord();
	
	//查询所有层奖用户
	List<JRecord> selectUserByCj();

	//根据父id查询代理级别
	List<JRecord> selectUserByPid(@Param("parentId")int parentId);

	//查询所有共享奖用户
	List<JRecord> selectUserGxjByUid(@Param("uId")int uId);
  
}