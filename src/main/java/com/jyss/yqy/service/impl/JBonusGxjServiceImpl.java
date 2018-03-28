package com.jyss.yqy.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.*;
import com.jyss.yqy.mapper.*;
import com.jyss.yqy.service.JBonusFPService;
import com.jyss.yqy.service.JBonusGxjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class JBonusGxjServiceImpl implements JBonusGxjService {

	@Autowired
	private OrdersBMapper ordersBMapper;
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private JRecordMapper jRecordMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JBonusGxjMapper jBonusGxjMapper;
	@Autowired
	private JBonusFPService jBonusFPService;


	/**
	 * 查询本周（昨日）
	 */
	@Override
	public JBonusResult getJBonusGxj(Integer uId) {

		float earnings = jBonusGxjMapper.selectJBonusGxjDay(uId);
		float total = jBonusGxjMapper.selectTotal(uId);
		List<JBonusGxj> list = jBonusGxjMapper.selectJBonusGxjWek(uId);

		JBonusResult result = new JBonusResult();
		result.setEarnings(earnings);
		result.setTotal(total);
		result.setData(list);
		return result;
	}



	/**
	 * 两个日期查询
	 */
	@Override
	public JBonusResult selectJBonusGxjByDay(Integer uId, int page, int limit, String beginTime, String endTime) {
		float total = jBonusGxjMapper.selectGxjTotalByDay(uId, beginTime, endTime);

		PageHelper.startPage(page, limit);
		List<JBonusGxj> list = jBonusGxjMapper.selectJBonusGxjByDay(uId, beginTime, endTime);
		PageInfo<JBonusGxj> pageInfo = new PageInfo<JBonusGxj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0.0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}



	/**
	 * 月份查询
	 */
	@Override
	public JBonusResult selectJBonusGxjByMonth(Integer uId, int page, int limit, String month) {
		float total = jBonusGxjMapper.selectGxjTotalByMonth(uId, month);

		PageHelper.startPage(page, limit);
		List<JBonusGxj> list = jBonusGxjMapper.selectJBonusGxjByMonth(uId, month);
		PageInfo<JBonusGxj> pageInfo = new PageInfo<JBonusGxj>(list);

		JBonusResult result = new JBonusResult();
		result.setEarnings(0.0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}



	/**
	 * 计算共享奖
	 */
	@Override
	public Map<String, String> insertJBonusGxj() {

		//查询返现比列和封顶值
		Xtcl xtcl1 = xtclMapper.getClsValue("gxjbl_type", "1");        //共享奖比例
		float float1 = Float.parseFloat(xtcl1.getBz_value());       			    //0.1

		List<JRecord> orderPvList = ordersBMapper.getSuccessOrderPv();        //查询昨日新增业绩
		for (JRecord jRecord : orderPvList) {
			List<JRecord> jRecords = jRecordMapper.selectUserGxjByUid(jRecord.getuId());
			if(jRecords != null && jRecords.size() == 1){
				JRecord jRecord1 = jRecords.get(0);

				List<JRecord> recordList = getJRecordList(jRecord1.getParentId());
				//获取人均分配值
				float amount = jRecord.getPv() / recordList.size();
				for (JRecord record : recordList) {
					if(amount <= record.getPv()){
						float jyScore = record.getPv() - amount;
						//添加共享奖
						JBonusGxj bonusGxj = new JBonusGxj();
						bonusGxj.setuId(record.getuId());
						bonusGxj.setsId(jRecord1.getuId());
						bonusGxj.setsName(jRecord1.getuAccount());
						bonusGxj.setAmount(amount);
						bonusGxj.setStatus(1);
						int count = jBonusGxjMapper.insert(bonusGxj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), amount, 7);
							if(flag){
								userMapper.updateUserCjStatus(null,jyScore,record.getuId());
							}
						}
					}else{
						//添加共享奖
						JBonusGxj bonusGxj = new JBonusGxj();
						bonusGxj.setuId(record.getuId());
						bonusGxj.setsId(jRecord1.getuId());
						bonusGxj.setsName(jRecord1.getuAccount());
						bonusGxj.setAmount(record.getPv());
						bonusGxj.setStatus(1);
						int count = jBonusGxjMapper.insert(bonusGxj);
						if(count == 1){
							boolean flag = jBonusFPService.insertScoreBalance(record.getuId(), record.getPv(), 7);
							if(flag){
								userMapper.updateUserCjStatus(null,0.00f,record.getuId());
							}
						}
					}
				}
			}
		}

		Map<String,String> map = new HashMap<String,String>();
		map.put("message", "共享奖和积分计算完成时间："+ new Date());
		return map;
	}


	//查询上层所有消费额大于0的用户列表
	public List<JRecord> getJRecordList(int uId){
		List<JRecord> result = new ArrayList<JRecord>();
		getJRecordList(uId,result);
		return result;
	}
	private void getJRecordList(int uId,List<JRecord> result){
		List<JRecord> jRecords = jRecordMapper.selectUserGxjByUid(uId);
		if(jRecords != null && jRecords.size() == 1){
			JRecord jRecord = jRecords.get(0);
			if(jRecord.getPv() > 0){
				result.add(jRecord);
			}
			getJRecordList(jRecord.getParentId(),result);
		}
	}



}
