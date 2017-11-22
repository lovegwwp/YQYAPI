package com.jyss.yqy.shiro;

import java.io.Serializable;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroToken extends UsernamePasswordToken implements Serializable {

	private static final long serialVersionUID = -6451794657814516274L;

	private String salt;

	public ShiroToken(String username, String pswd, String salt) {
		super(username, pswd);
		this.pswd = pswd;
		this.salt = salt;
	}

	/** 登录密码[字符串类型] 因为父类是char[] ] **/
	private String pswd;

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
