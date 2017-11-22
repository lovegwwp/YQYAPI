package com.jyss.yqy.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.AccountUser;

public interface AccountUserMapper {

	/**
	 * 获取登录用户信息
	 * 
	 * @param username
	 * @return
	 */
	AccountUser getAuBy(@Param("username") String username);

	/**
	 * 获取角色列表信息
	 * 
	 * @param username
	 * @return
	 */
	List<AccountUser> getRoles();

	/**
	 * 获取用户列表信息
	 * 
	 * @param username
	 * @return
	 */
	List<AccountUser> getAuByUsername(@Param("username") String username);

	/**
	 * 获取登录用户权限信息
	 * 
	 * @param username
	 * @return
	 */

	Set<AccountUser> getPermissionBy(@Param("username") String username);

	/**
	 * 判断当前登录用户是否唯一
	 * 
	 * @param username
	 * @return
	 */
	int getAuNum(@Param("username") String username);

	int upHtPwd(@Param("username") String username,
			@Param("password") String password, @Param("salt") String salt);

	AccountUser getAuByUsernameAndPassword(@Param("username") String username,
			@Param("password") String password);

	/**
	 * 增加用户
	 * 
	 * @param au
	 * @return
	 * 
	 */
	int addAccount(AccountUser au);

	/**
	 * 修改用户
	 * 
	 * @param au
	 * @return
	 * 
	 */
	int upAccount(AccountUser au);

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 * 
	 */
	int deleteAccounts(@Param("ids") List<Long> ids);

	/**
	 * 禁用
	 * 
	 * @param ids
	 * @return
	 * 
	 */
	int upAccountStatus(@Param("ids") List<Long> ids,
			@Param("status") String status);

}
