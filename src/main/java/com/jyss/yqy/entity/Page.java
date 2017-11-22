package com.jyss.yqy.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageInfo;

public class Page<E> {
	private long total;
	private List<E> rows = new ArrayList<E>();

	public Page() {
	}

	public Page(PageInfo<E> pageInfo) {
		this.total = pageInfo.getTotal();
		this.rows = pageInfo.getList();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

}
