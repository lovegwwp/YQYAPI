package com.jyss.yqy.entity;

import java.util.Date;

public class Cwzf {
	private int id;
	private String account;// 充值账户
	private int zhType;// 充值账户类型 1病人 2医生 3单位',
	private int czTime;// 充值时长
	private double czMoney;// 充值金额
	private int czType;// 充值类型 1 基础付费设置 2视频套餐设置 3通话套餐设置',
	private int zfType;// 支付类型 1支付宝 2微信 3其他',
	private int status;// 禁用状态 1正常状态 ',
	private Date createdAt;// 支付时间
	private Date lastModifyTime;// 最新修改时间
	private String cjsj;// 创建时间
	private String xgsj;// 最新修改时间
	private String zfAccount;// 付钱账号
	private String zfUname;// 付钱昵称
	// ///支付宝信息
	private String macOrderId;// 账户充值订单号
	private String zfSubject;// 支付主题
	private String zfBody;// 支付信息
	private String alipayId;// 支付宝订单号

	public Cwzf() {
		super();
	}

	public String getMacOrderId() {
		return macOrderId;
	}

	public void setMacOrderId(String macOrderId) {
		this.macOrderId = macOrderId;
	}

	public String getZfSubject() {
		return zfSubject;
	}

	public void setZfSubject(String zfSubject) {
		this.zfSubject = zfSubject;
	}

	public String getZfBody() {
		return zfBody;
	}

	public void setZfBody(String zfBody) {
		this.zfBody = zfBody;
	}

	public String getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getZhType() {
		return zhType;
	}

	public void setZhType(int zhType) {
		this.zhType = zhType;
	}

	public long getCzTime() {
		return czTime;
	}

	public void setCzTime(int czTime) {
		this.czTime = czTime;
	}

	public double getCzMoney() {
		return czMoney;
	}

	public void setCzMoney(double czMoney) {
		this.czMoney = czMoney;
	}

	public int getCzType() {
		return czType;
	}

	public void setCzType(int czType) {
		this.czType = czType;
	}

	public int getZfType() {
		return zfType;
	}

	public void setZfType(int zfType) {
		this.zfType = zfType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getZfAccount() {
		return zfAccount;
	}

	public void setZfAccount(String zfAccount) {
		this.zfAccount = zfAccount;
	}

	public String getZfUname() {
		return zfUname;
	}

	public void setZfUname(String zfUname) {
		this.zfUname = zfUname;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getXgsj() {
		return xgsj;
	}

	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}

	public Cwzf(int id, String account, int zhType, int czTime, double czMoney,
			int czType, int zfType, int status, Date createdAt,
			Date lastModifyTime, String cjsj, String xgsj, String zfAccount,
			String zfUname, String macOrderId, String zfSubject, String zfBody,
			String alipayId) {
		super();
		this.id = id;
		this.account = account;
		this.zhType = zhType;
		this.czTime = czTime;
		this.czMoney = czMoney;
		this.czType = czType;
		this.zfType = zfType;
		this.status = status;
		this.createdAt = createdAt;
		this.lastModifyTime = lastModifyTime;
		this.cjsj = cjsj;
		this.xgsj = xgsj;
		this.zfAccount = zfAccount;
		this.zfUname = zfUname;
		this.macOrderId = macOrderId;
		this.zfSubject = zfSubject;
		this.zfBody = zfBody;
		this.alipayId = alipayId;
	}

}
