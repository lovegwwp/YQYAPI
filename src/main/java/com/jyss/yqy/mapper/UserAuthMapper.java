package com.jyss.yqy.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.UserAuth;

public interface UserAuthMapper {

	//添加实名用户信息
	int insertUserAuth(UserAuth userAuth);

}
