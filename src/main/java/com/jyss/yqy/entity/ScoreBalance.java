package com.jyss.yqy.entity;

import java.util.Date;

public class ScoreBalance {

	private int id;
	private int end;// 1=A端 2=B端'
	private String uUuid;// 用户uuid
	private int category;// 积分来源或去向[1=A端取现，2=A端消费，3=A端返佣，4=管理奖，5=辅导奖，6=分销奖，7=市场奖，8=B端消费]
	private int type;// 1=收入 2=支出
	private float score;// 积分数额
	private float jyScore;// 结余数额
	private Date createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getuUuid() {
		return uUuid;
	}

	public void setuUuid(String uUuid) {
		this.uUuid = uUuid;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getJyScore() {
		return jyScore;
	}

	public void setJyScore(float jyScore) {
		this.jyScore = jyScore;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
