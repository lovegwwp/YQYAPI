package com.jyss.yqy.entity;

import java.io.Serializable;
import java.util.Date;

public class JRecord implements Serializable{
	
	private Integer id;
	private Integer uId;         //用户id
	private String uName;        //用户的名字
	private String uAccount;      //用户的账号
	private Integer parentId;    //用户的上级id
	private Integer depart;      //用户的部门  0无部门 1市场A 2市场B
	private Integer floor;       //用户的层级
	private Float pv;            //用户的个人pv
	private Float jyPv;          //用户的结余pv
	private Integer status;      //0禁用 1正常
	private Date created;        //创建时间
	private Date modifyTime;      //修改时间
	
	
	
	public String getuAccount() {
		return uAccount;
	}
	public void setuAccount(String uAccount) {
		this.uAccount = uAccount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getuId() {
		return uId;
	}
	public void setuId(Integer uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getDepart() {
		return depart;
	}
	public void setDepart(Integer depart) {
		this.depart = depart;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Float getPv() {
		return pv;
	}
	public void setPv(Float pv) {
		this.pv = pv;
	}
	public Float getJyPv() {
		return jyPv;
	}
	public void setJyPv(Float jyPv) {
		this.jyPv = jyPv;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	

}
