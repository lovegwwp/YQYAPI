package com.jyss.yqy.entity;

import java.util.List;

public class JBonusFxjResult {
	
	private Float earnings;          //今日收益
	private Float total;             //总收益金额
	private List<JBonusFxj> data;     //辅导奖列表
	
	
	
	public Float getEarnings() {
		return earnings;
	}
	public void setEarnings(Float earnings) {
		this.earnings = earnings;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public List<JBonusFxj> getData() {
		return data;
	}
	public void setData(List<JBonusFxj> data) {
		this.data = data;
	}
	
	
	
	

}
