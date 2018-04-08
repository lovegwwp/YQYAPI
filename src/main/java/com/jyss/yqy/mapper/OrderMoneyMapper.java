package com.jyss.yqy.mapper;

import com.jyss.yqy.entity.OrderMoney;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderMoneyMapper {

	int insertOrderMoney(OrderMoney orderMoney);



}
