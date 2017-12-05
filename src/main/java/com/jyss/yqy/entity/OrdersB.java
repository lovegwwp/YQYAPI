package com.jyss.yqy.entity;

import java.util.Date;

public class OrdersB {
	private int id;
	private String orderSn;// 订单编号
	private String gmId;// 购买人id
	private String gmr;// 购买人
	private String tel;// 购买人联系方式
	private String gmSp;// 取件商品名称
	private String pics;// 图片
	private String gmNum;// 购买数量
	private String gmDw;// 计量单位
	private String status;// 订单状态-1未付款状态,1待提货状态，2已提货，订单完成
	private String type;// 1B端商品，2非B端商品
	private float price;// 价格
	private float total;// 付钱总价
	private float pv;// 价格对应PV值
	private int zfType;// 支付方式：1=支付宝，2=微信，3=现金支付
	private String zfId;// 支付平台对应订单号
	private String code;// 订单提货码
	private Date createdAt;// 创建时间
	private Date lastModifyTime;// 最新修改时间
	private String dljb;// 1=初级代理，2=高级代理，3=高级代理，0=不标识状态
	private String cjsj;// /格式化创建时间

	public String getDljb() {
		return dljb;
	}

	public void setDljb(String dljb) {
		this.dljb = dljb;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getPv() {
		return pv;
	}

	public void setPv(float pv) {
		this.pv = pv;
	}

	public int getZfType() {
		return zfType;
	}

	public void setZfType(int zfType) {
		this.zfType = zfType;
	}

	public String getZfId() {
		return zfId;
	}

	public void setZfId(String zfId) {
		this.zfId = zfId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getId() {
		return id;
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

	public String getGmId() {
		return gmId;
	}

	public void setGmId(String gmId) {
		this.gmId = gmId;
	}

	public String getGmr() {
		return gmr;
	}

	public void setGmr(String gmr) {
		this.gmr = gmr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGmSp() {
		return gmSp;
	}

	public void setGmSp(String gmSp) {
		this.gmSp = gmSp;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getGmNum() {
		return gmNum;
	}

	public void setGmNum(String gmNum) {
		this.gmNum = gmNum;
	}

	public String getGmDw() {
		return gmDw;
	}

	public void setGmDw(String gmDw) {
		this.gmDw = gmDw;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
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

	public OrdersB() {
		super();
	}

	public OrdersB(String orderSn, String gmId, String gmr, String tel,
			String gmSp, String pics, String gmNum, String gmDw, String status,
			String type, float price, float total, int pv, String dljb,
			String code, String zfId, int zfType) {
		super();
		this.orderSn = orderSn;
		this.gmId = gmId;
		this.gmr = gmr;
		this.tel = tel;
		this.gmSp = gmSp;
		this.pics = pics;
		this.code = code;
		this.total = total;
		this.pv = pv;
		this.gmNum = gmNum;
		this.gmDw = gmDw;
		this.status = status;
		this.type = type;
		this.zfType = zfType;
		this.zfId = zfId;
		this.price = price;
		this.dljb = dljb;

	}

}
