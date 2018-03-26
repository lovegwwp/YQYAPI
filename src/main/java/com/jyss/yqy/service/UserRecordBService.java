package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.UUserRRecordB;

public interface UserRecordBService {

	Map<String, String> insertUserRecordB(@Param("uuid") String uuid,
			@Param("bCode") String bCode);

	//Map<String, String> insertJBonusFdj();

	/* Map<String,String> insertJBonusGlj(@Param("uuid") String uuid); */

	List<UUserRRecordB> getRecordB(@Param("uId") String uId,
			@Param("rId") String rId, @Param("status") String status);

	//////用户更改等级
	int updateTypeByUid(@Param("type") String type,@Param("rId") String rId, @Param("status") String status);

	List<UUserRRecordB> getRecordBGroupByRid();

}
