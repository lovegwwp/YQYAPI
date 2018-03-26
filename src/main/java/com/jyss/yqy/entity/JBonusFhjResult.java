package com.jyss.yqy.entity;

import java.io.Serializable;
import java.util.List;

public class JBonusFhjResult implements Serializable{
	
	private Double earnings;             //今日收益
	private Double total;                 //总收益金额
	private List<JBonusFhj> data;     //分红奖列表


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

	public List<JBonusFhj> getData() {
		return data;
	}

	public void setData(List<JBonusFhj> data) {
		this.data = data;
	}
}
