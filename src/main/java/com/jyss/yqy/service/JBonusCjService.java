package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusResult;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface JBonusCjService {

	JBonusResult getJBonusCj(@Param("uId") Integer uId);

	//计算层奖
	Map<String,String> insertJBonusCj();


}
