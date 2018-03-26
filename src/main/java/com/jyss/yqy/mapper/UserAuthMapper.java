package com.jyss.yqy.mapper;


import com.jyss.yqy.entity.UserAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthMapper {

	//添加实名用户信息
	int insertUserAuth(UserAuth userAuth);

}
