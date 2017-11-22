package com.jyss.yqy.shiro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.jyss.yqy.entity.AccountUser;
import com.jyss.yqy.service.AccountUserService;

public class MyRealm extends AuthorizingRealm {

	@Autowired
	private AccountUserService auService;
	String pass;

	/**
	 * 2.授权:
	 * 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Object principal = principalCollection.getPrimaryPrincipal();// 获取登录的用户名
		String role = "user";
		AccountUser user = auService.getAuBy((String) principal);
		if (user != null) {
			role = user.getRoleSign();
		}
		// 进行授权角色
		info.addRole(role);
		// 进行授权角色
		info.setStringPermissions(getStringPermissionBy(auService
				.getPermissionBy((String) principal)));
		return info;

	}

	public Set<String> getStringPermissionBy(Set<AccountUser> set) {
		// TODO Auto-generated method stub
		if (set == null) {
			return null;
		}
		AccountUser au = new AccountUser();
		Set<String> s = new HashSet<String>();
		Iterator i = set.iterator();// 先迭代出来
		while (i.hasNext()) {
			au = (AccountUser) i.next();
			if (au != null) {
				s.add(au.getPermissionSign());
			}
		}
		return s;
	}

	/*
	 * 1.用户验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken upToken) throws AuthenticationException {
		// 1. token 中获取登录的 username! 注意不需要获取password.
		ShiroToken token = (ShiroToken) upToken;
		String principal = token.getUsername();

		String credentials = token.getPswd();
		// 3.设置盐值 ，（加密的调料，让加密出来的东西更具安全性，一般是通过数据库查询出来的。
		// 简单的说，就是把密码根据特定的东西而进行动态加密，如果别人不知道你的盐值，就解不出你的密码）
		ByteSource credentialsSalt = ByteSource.Util.bytes(token.getSalt());

		// 当前 Realm 的name
		String realmName = getName();
		// 返回值实例化
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal,
				credentials, credentialsSalt, realmName);
		return info;
	}

	private AccountUser tokenToUser(UsernamePasswordToken authcToken) {
		AccountUser user = new AccountUser();
		user.setUsername(authcToken.getUsername());
		user.setPwd(String.valueOf(authcToken.getPassword()));
		return user;
	}

	// init-method 配置.
	// 是初始化的方法
	public void setCredentialMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");// MD5算法加密
		credentialsMatcher.setHashIterations(1024);// 1024次循环加密
		setCredentialsMatcher(credentialsMatcher);
	}

	// 用来测试的算出密码password盐值加密后的结果，下面方法用于新增用户添加到数据库操作的，我这里就直接用main获得，直接数据库添加了，省时间
	public static void main(String[] args) {
		String saltSource = "02375218";
		String hashAlgorithmName = "MD5";
		String credentials = "1234";
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt,
				hashIterations);
		System.out.println(result);
	}

	public AccountUserService getAuService() {
		return auService;
	}

	public void setAuService(AccountUserService auService) {
		this.auService = auService;
	}

}