package com.jyss.yqy.entity;

import java.io.Serializable;


public class JBonusScj implements Serializable{
	
    private Integer id;     
    private Integer uId;      //用户的id
    private Integer aId;      //市场A的用户id
    private Float aPv;        //市场A的总PV值
    private Integer bId;      //市场B的用户id
    private Float bPv;        //市场A的总PV值
    private Float pv;         //用户可得pv值
    private Integer status;        //状态   0禁用 1正常
    private String created;        //时间
    
    
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
	public Integer getaId() {
		return aId;
	}
	public void setaId(Integer aId) {
		this.aId = aId;
	}
	public Float getaPv() {
		return aPv;
	}
	public void setaPv(Float aPv) {
		this.aPv = aPv;
	}
	public Integer getbId() {
		return bId;
	}
	public void setbId(Integer bId) {
		this.bId = bId;
	}
	public Float getbPv() {
		return bPv;
	}
	public void setbPv(Float bPv) {
		this.bPv = bPv;
	}
	public Float getPv() {
		return pv;
	}
	public void setPv(Float pv) {
		this.pv = pv;
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