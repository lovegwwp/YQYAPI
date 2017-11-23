package com.jyss.yqy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.service.OrdersBService;

@Service
@Transactional
public class OrdersBServiceImpl implements OrdersBService {

	@Autowired
	private OrdersBMapper obMapper;

	@Override
	public List<OrdersB> getOrdersBy(String status, String orderSn, String gmId) {
		// TODO Auto-generated method stub
		return obMapper.getOrdersBy(status, orderSn, gmId);
	}

	@Override
	public int addOrder(OrdersB ob) {
		// TODO Auto-generated method stub
		ob.setType("1");
		return obMapper.addOrder(ob);
	}

	@Override
	public int upOrderStatus(String status, String statusBefore, String orderSn) {
		// TODO Auto-generated method stub
		return obMapper.upOrderStatus(status, statusBefore, orderSn);
	}

	@Override
	public int delOrder(String orderSn) {
		// TODO Auto-generated method stub
		return obMapper.delOrder(orderSn);
	}

	@Override
	public List<Goods> getGoods(String type) {
		// TODO Auto-generated method stub
		return obMapper.getGoods(type);
	}

}
