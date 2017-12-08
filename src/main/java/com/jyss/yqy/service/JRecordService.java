package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JRecord;

public interface JRecordService {
	
	//计算市场奖
	Map<String,String> insertJBonusScj();
	
	

}
