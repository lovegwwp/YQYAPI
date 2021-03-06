package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ThOrders;
import com.jyss.yqy.entity.Thd;
import org.springframework.stereotype.Repository;

@Repository
public interface ThdMapper {

	/**
	 * 用户登陆
	 */

	List<Thd> findThdByTel(@Param("tel") String tel,@Param("id") String id);

	/**
	 * 修改密码
	 */

	void updatePwd(@Param("tel") String tel,
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

	/**
	 * 修改提货端客户信息
	 * 
	 * @param thd
	 * @return
	 */
	int upThdInfo(Thd thd);

}
