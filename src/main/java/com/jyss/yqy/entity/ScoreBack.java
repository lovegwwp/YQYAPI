package com.jyss.yqy.entity;

import java.util.Date;

public class ScoreBack {

	private int id;
	private String uuuid;
	private int dlType;
	private int backNum;
	private int leftNum;
	private int backScore;
	private int eachScore;
	private Date createdAt;
	private Date backTime;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuuid() {
		return uuuid;
	}

	public void setUuuid(String uuuid) {
		this.uuuid = uuuid;
	}

	public int getDlType() {
		return dlType;
	}

	public void setDlType(int dlType) {
		this.dlType = dlType;
	}

	public int getBackNum() {
		return backNum;
	}

	public void setBackNum(int backNum) {
		this.backNum = backNum;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public int getBackScore() {
		return backScore;
	}

	public void setBackScore(int backScore) {
		this.backScore = backScore;
	}

	public int getEachScore() {
		return eachScore;
	}

	public void setEachScore(int eachScore) {
		this.eachScore = eachScore;
	}

	public Date getBackTime() {
		return backTime;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
