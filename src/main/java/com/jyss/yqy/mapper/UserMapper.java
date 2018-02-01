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
	 * @param user
	 * @return
	 */
	int upUserMyInfo(@Param("uuid") String uuid, @Param("nick") String nick,
			@Param("province") String province,
			@Param("provinceId") String provinceId,
			@Param("cityId") String cityId, @Param("city") String city,
			@Param("areaId") String areaId, @Param("area") String area);

	/**
	 * 修改个人支付密码
	 * 
	 * @param user
	 * @return
	 */
	int upPayPwd(@Param("uuid") String uuid, @Param("payPwd") String payPwd);
	
	/**
	 * 修改返现额度
	 */
	int upTotalPv(@Param("uuid") String uuid, @Param("totalPv") String totalPv);

	/**
	 * 查询个人信息
	 * 
	 * @param account
	 * @param uuid
	 * @param id
	 * @param status
	 * @param isAuth
	 * @param statusAuth
	 * @return
	 */

	List<UserBean> getUserInfo(@Param("account") String account,
			@Param("uuid") String uuid, @Param("id") String id,
			@Param("status") String status, @Param("isAuth") String isAuth,
			@Param("statusAuth") String statusAuth);

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

	/**
	 * 修改个人。。。等等。。状态
	 * 
	 * @param pwd
	 * @param salt
	 * @param salt
	 * @return
	 */
	int upUserAllStatus(@Param("status") String status,
			@Param("bCode") String bCode, @Param("bIsPay") String bIsPay,
			@Param("isChuangke") String isChuangke,
			@Param("isAuth") String isAuth, @Param("id") String id);

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

	/**
	 * 通过推荐码查询推荐人信息
	 */
	List<UserBean> getUserByBCode(@Param("bCode") String bCode);

	/**
	 * 通过用户的uuid查询用户
	 */
	List<UserBean> getUserByUuid(@Param("uuid") String uuid);

	/**
	 * 通过用户的id查询用户
	 */
	List<UserBean> getUserNameById(@Param("id") int id);

	/**
	 * 通过id查询积分
	 */
	List<UserBean> getUserScoreById(@Param("id") int id);

	// 更新积分
	int updateScore(UserBean userBean);

	// 更新积分
	int updateUserBackScore(@Param("cashScore") float cashScore,
			@Param("shoppingScore") float shoppingScore,
			@Param("uuuid") String uuuid);

}
