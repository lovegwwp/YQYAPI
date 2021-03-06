package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.UserTotalAmount;
import org.springframework.stereotype.Repository;

@Repository
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
	 * @return
	 */
	List<OrdersB> getOrdersBy(@Param("status") String status,
			@Param("orderSn") String orderSn, @Param("gmId") String gmId);
	
	List<OrdersB> getOrdersByPay(@Param("status") String status,
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
	 * 修改订单代理等级状态
	 *
	 * @param dljb
	 * @param statusBefore
	 * @param orderSn
	 * @return
	 */
	int upOrderDljb(@Param("dljb") String dljb,
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

	// 查询已完成的订单total
	List<JRecord> getSuccessOrderTotal();
	
	// 查询已完成的订单pv
	List<JRecord> getSuccessOrderPv();
	
	////分红奖统计///////
	////代理人
	UserTotalAmount getOrdersDlrSum(@Param("kssj") String kssj,@Param("jssj") String jssj);
	////代言人
	UserTotalAmount getRecordDyrSum(@Param("kssj") String kssj,@Param("jssj") String jssj);

}
