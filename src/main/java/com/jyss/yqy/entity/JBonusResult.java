package com.jyss.yqy.entity;

import java.io.Serializable;
import java.util.List;

public class JBonusResult implements Serializable{
	
	private Float earnings;            //今日收益
	private Float total;               //总收益金额
	private List data;                 //列表


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

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}
}
