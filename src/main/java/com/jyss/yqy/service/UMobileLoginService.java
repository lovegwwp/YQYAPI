package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.UMobileLogin;

public interface UMobileLoginService {
	
	//根据用户id查询用户登陆
	//public List<UMobileLogin> getUMobileLoginByUid(@Param("uUuid")String uUuid);
	
	//根据token查询用户
	List<UMobileLogin> findUserByToken(@Param("token") String token);

}
