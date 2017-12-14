package com.jyss.yqy.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.JBonusFdj;
import com.jyss.yqy.entity.JBonusFdjResult;
import com.jyss.yqy.entity.JBonusFxjResult;


public interface JBonusFxjService {
	
	public JBonusFxjResult getJBonusFxj(int uId);
	
	public JBonusFxjResult selectJBonusFxjByDay(int uId, String beginTime,String endTime);
    
	public JBonusFxjResult selectJBonusFxjByMonth(int uId, String month);
    
    public Map<String, String> insertJBonusFxj();

}
