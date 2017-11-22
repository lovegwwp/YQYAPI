package com.jyss.yqy.entity;

import java.util.Date;

public class Zfsz {
	private int id;
	private int modelTime;
	private int modelMoney;
	private int type; // 支付类型 1 基础付费设置 2视频套餐设置 3通话套餐设置'
	private int status;
	private Date createdAt;// 创建时间
	private Date lastModifyTime;// 最新修改时间
	private String cjsj;// 创建时间
	private String xgsj;// 最新修改时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getModelTime() {
		return modelTime;
	}

	public void setModelTime(int modelTime) {
		this.modelTime = modelTime;
	}

	public int getModelMoney() {
		return modelMoney;
	}

	public void setModelMoney(int modelMoney) {
		this.modelMoney = modelMoney;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

}
