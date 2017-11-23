package com.jyss.yqy.entity;

import java.util.List;

public class JBonusFdjResult {
	
	private Double earnings;          //今日收益
	private Double total;             //总收益金额
	private List<JBonusFdj> data;     //辅导奖列表
	
	
	
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
	public List<JBonusFdj> getData() {
		return data;
	}
	public void setData(List<JBonusFdj> data) {
		this.data = data;
	}
	
	
	
	

}
