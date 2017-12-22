package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.UserTotalAmount;

public interface OrdersBMapper {

	/**
	 * 获取B端商品=亚麻籽油
	 * 
	 * @param type
	 * @return
	 */
	List<Goods> getGoods(@Param("type") String type);

	/**
	 * 获取B端商品=亚麻籽油
	 * 
	 * @param id
	 * @return
	 */
	Goods getGoodsByid(@Param("id") String id);

	/**
	 * 获取B端订单信息
	 * 
	 * @param username
	 * @return
	 */
	List<OrdersB> getOrdersBy(@Param("status") String status,
			@Param("orderSn") String orderSn, @Param("gmId") String gmId);
	
	/**
	 * 查询A端商品总数量 
	 */
	List<OrdersB> selectTotalOrderABy(@Param("status") String status,
			@Param("orderSn") String orderSn, @Param("gmId") String gmId);
	
	/**
	 * 查询A端订单信息
	 */
	List<OrdersB> selectOrderABy(@Param("status") String status,
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
	 * 修改A端订单状态
	 */
	int updateOrderAStatus(@Param("status") String status,
			@Param("statusBefore") String statusBefore,
			@Param("orderSn") String orderSn);
	

	/**
	 * 删除订单
	 * 
	 * @param orderSn
	 * @return
	 */
	int delOrder(@Param("orderSn") String orderSn);

	// 查询已完成的订单
	List<UserTotalAmount> getSuccessOrder();
	
	// 查询已完成的订单
	List<JRecord> getSuccessOrderPv();

}
