package com.jyss.yqy.entity;

import java.io.Serializable;
import java.util.List;

public class JBonusScjResult implements Serializable{
	
	private Float departA;            //市场A的pv值
	private Float departB;            //市场B的pv值
	private Float total;              //总收益金额
	private List<JBonusScj> data;     //管理奖列表
	
	public Float getDepartA() {
		return departA;
	}
	public void setDepartA(Float departA) {
		this.departA = departA;
	}
	public Float getDepartB() {
		return departB;
	}
	public void setDepartB(Float departB) {
		this.departB = departB;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public List<JBonusScj> getData() {
		return data;
	}
	public void setData(List<JBonusScj> data) {
		this.data = data;
	}
	
	

	

}
