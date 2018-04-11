package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.User;
import com.jyss.yqy.entity.UserAuth;
import com.jyss.yqy.entity.jsonEntity.UserBean;

public interface UserService {

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
                             @Param("statusAuth") String statusAuth, @Param("statusAuthMoreThan") String statusAuthMoreThan);

	List<UserBean> getUserById(@Param("id") String id,
                               @Param("status") String status, @Param("isAuth") String isAuth);

	/**
	 * 用户登陆
	 */
	Map<String, Object> login(@Param("account") String account,
                                     @Param("password") String password);

	/**
	 * 用户退出
	 */
	int loginOut(@Param("uuid") String uuid, @Param("token") String token);

	/**
	 * 修改个人信息
	 * 
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
	 * @return
	 */
	int upPayPwd(@Param("uuid") String uuid, @Param("payPwd") String payPwd);
	
	/**
	 * 修改返现额度
	 */
	int upTotalPv(@Param("uuid") String uuid, @Param("totalPv") String totalPv);


	/**
	 * 修改个人。。。等等。。状态
	 * 
	 * @return
	 */
	int upUserAllStatus(@Param("status") String status,
                        @Param("bCode") String bCode, @Param("bIsPay") String bIsPay,
                        @Param("isChuangke") String isChuangke,
                        @Param("isAuth") String isAuth, @Param("id") String id);

	
	int upUserAllStatusByUUid(@Param("status") String status,
                              @Param("bCode") String bCode, @Param("bIsPay") String bIsPay,
                              @Param("isChuangke") String isChuangke,
                              @Param("isAuth") String isAuth, @Param("uuid") String uuid);
	
	/**
	 * 查询个人信息
	 */
	List<UserBean> getUserInfo(@Param("account") String account,
                               @Param("uuid") String uuid, @Param("id") String id,
                               @Param("status") String status, @Param("isAuth") String isAuth,
                               @Param("statusAuth") String statusAuth);

	/**
	 * 新增用户
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

	// 添加实名用户信息
	int insertUserAuth(UserAuth userAuth);

	/**
	 * 通过用户的uuid查询用户
	 */
	List<UserBean> getUserByUuid(@Param("uuid") String uuid);

	// 更新积分
	int updateUserBackScore(@Param("cashScore") Float cashScore,
                            @Param("shoppingScore") Float shoppingScore,
                            @Param("uuuid") String uuuid);
	
	//////分红奖/////
	/**
	 * 通过id查询积分
	 */
	List<UserBean> getUserByFHJ(@Param("account") String account,
                                @Param("status") String status, @Param("isAuth") String isAuth,
                                @Param("isChuangke") String isChuangke);

	// 更新积分
	int updateScoreByFHJ(@Param("cashScore") String cashScore,
                         @Param("shoppingScore") String shoppingScore, @Param("totalPv") String totalPv, @Param("id") String id, @Param("isChuangke") String isChuangke);

	/**
	 * //查询是否重复，根据bcode和uuid
	 * @param uuid
	 * @param bCode
	 * @return
	 */
	List<UserBean> getUserIsOnlyBy(@Param("uuid") String uuid,@Param("bCode") String bCode);

	/**
	 * 修改user各种金额
	 * @param uuid 用户uuid
	 * @param id 用户id
	 * @param totalPv 分红权
	 * @param cashScore 商城消费券
	 * @param shoppingScore 股券
	 * @param electScore 电子券
	 * @param bdScore 报单券
	 * @param totalAmount 首次消费额
	 * @param borrow 借贷金额
	 * @param isChuangke 1=成为代言人 2=一级代理人 3=二级代理人 4=三级代理人 5=经理人（虚拟）6=市场总监助理
	 * @return
	 */
	int upUserMoneyByUUidOrId(@Param("uuid") String uuid,@Param("id") String id, @Param("totalPv") Float totalPv,
							  @Param("cashScore") Float cashScore
			, @Param("shoppingScore") Float shoppingScore
			, @Param("electScore") Float electScore
			, @Param("bdScore") Float bdScore
			, @Param("totalAmount") Float totalAmount
			, @Param("borrow") Float borrow
			, @Param("isChuangke") Integer isChuangke);

}
