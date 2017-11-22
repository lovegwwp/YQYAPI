package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.User;
import com.jyss.yqy.entity.jsonEntity.UserBean;

public interface UserMapper {

	/**
	 * B端登录用户
	 * 
	 * @param status
	 * @param isAuth
	 * @param statusAuth
	 * @return
	 */
	List<UserBean> getUserBy(@Param("account") String account,
			@Param("status") String status, @Param("isAuth") String isAuth,
			@Param("statusAuth") String statusAuth);

	List<UserBean> getUserById(@Param("id") String id,
			@Param("status") String status, @Param("isAuth") String isAuth);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	int addUser(User user);

	/**
	 * 修改个人用户
	 * 
	 * @param user
	 * @return
	 */
	int upMyInfo(User user);

	/**
	 * 修改个人信息
	 * 
	 * @param pwd
	 * @param salt
	 * @param salt
	 * @return
	 */
	int upPwd(@Param("pwd") String pwd, @Param("salt") String salt,
			@Param("id") String id);

	// /////////////////login表//////////
	/**
	 * 增加token
	 * 
	 * @param uuid
	 * @param token
	 * @return
	 */
	int addLogin(@Param("uuid") String uuid, @Param("token") String token);

	/**
	 * 修改token
	 * 
	 * @param uuid
	 * @param token
	 * @return
	 */
	int upToken(@Param("uuid") String uuid, @Param("token") String token,
			@Param("time") long time);

	/**
	 * 登录login --token
	 * 
	 * @return
	 */
	List<UserBean> getToken(@Param("uuid") String uuid,
			@Param("token") String token);

}
