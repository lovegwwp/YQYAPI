package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ThOrders;
import com.jyss.yqy.entity.Thd;

public interface ThdService {
	/**
	 * 用户登陆
	 */
	public Map<String, Object> login(@Param("tel") String tel,
			@Param("password") String password);

	/**
	 * 用户查询
	 */
	public List<Thd> findThdByTel(@Param("tel") String tel);

	/**
	 * 密码修改
	 */

	public void updatePwd(@Param("tel") String tel,
			@Param("password") String password, @Param("salt") String salt);

	/**
	 * 提货点扫码记录
	 * 
	 * @param thId
	 * @return
	 */
	List<ThOrders> getThdOrdersBy(@Param("thId") String thId);

	/**
	 * 增加订单
	 * 
	 * @param thOrder
	 * @return
	 */
	int addThOrder(ThOrders thOrder);

}
