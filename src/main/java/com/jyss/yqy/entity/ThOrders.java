package com.jyss.yqy.entity;

import java.util.Date;

public class ThOrders {
	private int id;
	private String orderSn;// 订单编号
	private String thr;// 取件人
	private String tel;// 取件人联系方式
	private String thSp;// 取件商品名称
	private String thNum;// 取件数量
	private String thDw;// 计量单位
	private Date createdAt;// 创建时间
	private String status;// 订单状态
	private String cjsj;// 创建时间
	private String thId;// 提货点id
	private String name;// 提货点联系人姓名
	private String thName;// 提货点名称
	private float price;// 价格

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThName() {
		return thName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setThName(String thName) {
		this.thName = thName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getThr() {
		return thr;
	}

	public void setThr(String thr) {
		this.thr = thr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getThSp() {
		return thSp;
	}

	public void setThSp(String thSp) {
		this.thSp = thSp;
	}

	public String getThNum() {
		return thNum;
	}

	public void setThNum(String thNum) {
		this.thNum = thNum;
	}

	public String getThDw() {
		return thDw;
	}

	public void setThDw(String thDw) {
		this.thDw = thDw;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getThId() {
		return thId;
	}

	public void setThId(String thId) {
		this.thId = thId;
	}

}
