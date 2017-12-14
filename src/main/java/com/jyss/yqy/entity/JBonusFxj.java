package com.jyss.yqy.entity;

import java.io.Serializable;

public class JBonusFxj implements Serializable{

    private Integer id;     
    private Integer uId;       //用户的id
    private String name;       //用户的真实名字
    private Integer parentId;   //推荐人id
    private Float amount;       //推荐人获得的金额
    private Integer status;      //状态   0禁用 1正常
    private String created;       //时间
    
    
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
    
    
    
 
}