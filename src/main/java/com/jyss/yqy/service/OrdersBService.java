package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;

public interface OrdersBService {

	/**
	 * 获取B端商品=亚麻籽油
	 * 
	 * @param username
	 * @return
	 */
	List<Goods> getGoods(@Param("type") String type);

	/**
	 * 获取B端订单信息
	 * 
	 * @param username
	 * @return
	 */
	List<OrdersB> getOrdersBy(@Param("status") String status,
			@Param("orderSn") String orderSn, @Param("gmId") String gmId);

	/**
	 * 新增订单
	 * 
	 * @param ob
	 * @return
	 */
	int addOrder(OrdersB ob);

	/**
	 * 修改订单状态
	 * 
	 * @param status
	 * @param statusBefore
	 * @param orderSn
	 * @return
	 */
	int upOrderStatus(@Param("status") String status,
			@Param("statusBefore") String statusBefore,
			@Param("orderSn") String orderSn);

	/**
	 * 删除订单
	 * 
	 * @param orderSn
	 * @return
	 */
	int delOrder(@Param("orderSn") String orderSn);

}
