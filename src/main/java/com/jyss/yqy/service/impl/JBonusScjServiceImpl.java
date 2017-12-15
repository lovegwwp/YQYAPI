package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFxj;
import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JBonusScjResult;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusScjService;



@Service
@Transactional
public class JBonusScjServiceImpl implements JBonusScjService{
	
	@Autowired
	private JBonusScjMapper bonusScjMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private XtclMapper xtclMapper;
	
	
	/**
	 * 查询用户市场奖
	 */
	@Override
	public JBonusScjResult selectJBonusScjByUid(int uId,int page,int limit) {
		JBonusScjResult result = new JBonusScjResult();
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByUid(uId);
		JBonusScj bonusScj = bonusScjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		//设置市场奖总金额
		float totalPv = bonusScjMapper.selectTotalPv(uId);
		List<UserBean> userList = userMapper.getUserNameById(uId);
		UserBean userBean = userList.get(0);
		int level = userBean.getIsChuangke();
		
		//查询返现比列和封顶值
		Xtcl xtcl1 = xtclMapper.getClsValue("scj_type", "1");        //初级获得金额
		float float1 = Float.parseFloat(xtcl1.getBz_value());        //0.12
		Xtcl xtcl2 = xtclMapper.getClsValue("scj_type", "2");        //中级获得金额
		float float2 = Float.parseFloat(xtcl2.getBz_value());        //0.16
		Xtcl xtcl3 = xtclMapper.getClsValue("scj_type", "3");        //高级获得金额
		float float3 = Float.parseFloat(xtcl3.getBz_value());        //0.22
		Xtcl xtcl4 = xtclMapper.getClsValue("scj_type", "4");        //初级获得金额
		float float4 = Float.parseFloat(xtcl4.getBz_value());        //3000.00
		Xtcl xtcl5 = xtclMapper.getClsValue("scj_type", "5");        //中级获得金额
		float float5 = Float.parseFloat(xtcl5.getBz_value());        //6000.00
		Xtcl xtcl6 = xtclMapper.getClsValue("scj_type", "6");        //高级获得金额
		float float6 = Float.parseFloat(xtcl6.getBz_value());        //9000.00
		
		if(level == 2){
			result.setTotal((totalPv*float1) <= float4 ? (float)(Math.round((totalPv*float1)*100))/100 : float4);
		}else if(level == 3){
			result.setTotal((totalPv*float2) <= float5 ? (float)(Math.round((totalPv*float2)*100))/100 : float5);
		}else if(level == 4){
			result.setTotal((totalPv*float3) <= float6 ? (float)(Math.round((totalPv*float3)*100))/100 : float6);
		}
		//设置本周明细
		PageHelper.startPage(page, limit);
		List<JBonusScj> scjList = bonusScjMapper.selectJBonusScjWek(uId);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(scjList);
		result.setData(scjList);
		return result;
		
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByDay(int uId,int page,int limit,String beginTime,String endTime) {
		JBonusScjResult result = new JBonusScjResult();
		PageHelper.startPage(page, limit);
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByDay(uId, beginTime, endTime);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(bonusScjList);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByDay(uId, beginTime, endTime);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}

	@Override
	public JBonusScjResult selectJBonusScjByMonth(int uId,int page,int limit,String month) {
		JBonusScjResult result = new JBonusScjResult();
		PageHelper.startPage(page, limit);
		List<JBonusScj> bonusScjList = bonusScjMapper.selectJBonusScjByMonth(uId, month);
		PageInfo<JBonusScj> pageInfo = new PageInfo<JBonusScj>(bonusScjList);
		List<JBonusScj> scjList = bonusScjMapper.selectScjTotalByMonth(uId, month);
		JBonusScj bonusScj = scjList.get(0);
		result.setDepartA(bonusScj.getaPv());
		result.setDepartB(bonusScj.getbPv());
		result.setTotal(null);
		result.setData(bonusScjList);
		return result;
		
	}
	

}
