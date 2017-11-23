package com.jyss.yqy.entity;

import java.util.List;

public class JBonusGljResult {
	
	private Double earnings;          //今日收益
	private Double total;             //总收益金额
	private List<JBonusGlj> list;     //管理奖列表
	
	
	public Double getEarnings() {
		return earnings;
	}
	public void setEarnings(Double earnings) {
		this.earnings = earnings;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<JBonusGlj> getList() {
		return list;
	}
	public void setList(List<JBonusGlj> list) {
		this.list = list;
	}
	
	

}
