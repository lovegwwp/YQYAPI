package com.jyss.yqy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ScoreBalance {

	private Integer id;
	private Integer end;// 1=A端 2=B端'
	private String uUuid;// 用户uuid
	private Integer category;// 来源或去向[1=A端取现，2=A端消费，3=A端返佣，4=分红奖，5=层奖，6=量奖，7=共享奖]
	private Integer type;// 1=收入 2=支出
	private Float score;// 积分数额
	private Float jyScore;// 结余数额
	private Date createdAt;
	private Date jsTime;   //结算时间
	private String title;//
	private String orderSn;// 订单号
	private String cjsj;// /格式化创建时间
	private Integer status;// 1=收入 2=支出
	private Integer secoCate;//1=税费，2=平台管理费
	private String zzCode;  //


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getuUuid() {
		return uUuid;
	}

	public void setuUuid(String uUuid) {
		this.uUuid = uUuid;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Float getJyScore() {
		return jyScore;
	}

	public void setJyScore(Float jyScore) {
		this.jyScore = jyScore;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getJsTime() {
		return jsTime;
	}

	public void setJsTime(Date jsTime) {
		this.jsTime = jsTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSecoCate() {
		return secoCate;
	}

	public void setSecoCate(Integer secoCate) {
		this.secoCate = secoCate;
	}

	public String getZzCode() {
		return zzCode;
	}

	public void setZzCode(String zzCode) {
		this.zzCode = zzCode;
	}


}
