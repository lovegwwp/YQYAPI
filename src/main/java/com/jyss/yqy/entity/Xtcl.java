package com.jyss.yqy.entity;

public class Xtcl {
	private int id;
	private String bz_type;
	private String bz_id;
	private String bz_value;
	private String bz_info;
	private int pid;
	private int status;
	private String hrefStr;

	public String getHrefStr() {
		return hrefStr;
	}

	public void setHrefStr(String hrefStr) {
		this.hrefStr = hrefStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBz_info() {
		return bz_info;
	}

	public void setBz_info(String bz_info) {
		this.bz_info = bz_info;
	}

	public String getBz_type() {
		return bz_type;
	}

	public void setBz_type(String bz_type) {
		this.bz_type = bz_type;
	}

	public String getBz_id() {
		return bz_id;
	}

	public void setBz_id(String bz_id) {
		this.bz_id = bz_id;
	}

	public String getBz_value() {
		return bz_value;
	}

	public void setBz_value(String bz_value) {
		this.bz_value = bz_value;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
