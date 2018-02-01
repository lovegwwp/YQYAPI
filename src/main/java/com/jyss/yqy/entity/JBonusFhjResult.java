package com.jyss.yqy.entity;

import java.util.List;

public class JBonusFhjResult {
	
	private Float earnings;           //今日收益
	private Float total;              //总收益金额
	private List<ScoreBalance> data;     //分红奖列表


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

	public List<ScoreBalance> getData() {
		return data;
	}

	public void setData(List<ScoreBalance> data) {
		this.data = data;
	}
}
