package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusResult;
import org.apache.ibatis.annotations.Param;

public interface JBonusCjService {

	JBonusResult getJBonusCj(@Param("uId") Integer uId);


}
