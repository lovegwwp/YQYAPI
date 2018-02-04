package com.jyss.yqy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.JBonusFhjResult;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusFhjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class JBonusFhjServiceImpl implements JBonusFhjService {
	
	@Autowired
	private XtclMapper xtclMapper;
	@Autowired
	private ScoreBalanceMapper scoreBalanceMapper;
	
	

	/**
	 * 查询本周
	 */
	@Override
	public JBonusFhjResult getJBonusFhj(String uUUid) {
		Xtcl xtcl = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
		float float1 = Float.parseFloat(xtcl.getBz_value());                     //0.7

		float earnings = scoreBalanceMapper.selectEarnings(uUUid, "11");
		float earnings1 = (float)(Math.round((earnings / float1)*100))/100;       //现金转pv

		float total = scoreBalanceMapper.selectTotal(uUUid, "11");
		float total1 = (float)(Math.round((total / float1)*100))/100;

		List<ScoreBalance> list = scoreBalanceMapper.selectJBonusFhjWek(uUUid, "11");
		for (ScoreBalance scoreBalance : list) {
			float score = (float) (Math.round((scoreBalance.getScore() / float1) * 100)) / 100;
			scoreBalance.setScore(score);
			scoreBalance.setOrderSn("分红奖");
		}
		JBonusFhjResult result = new JBonusFhjResult();
		result.setEarnings(earnings1);
		result.setTotal(total1);
		result.setData(list);
		return result;
	}


	/**
	 * 两个日期查询
	 */
	@Override
	public JBonusFhjResult selectJBonusFhjByDay(String uUUid, int page, int limit,
												String beginTime, String endTime) {
		Xtcl xtcl = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
		float float1 = Float.parseFloat(xtcl.getBz_value());                     //0.7

		float totalByDay = scoreBalanceMapper.selectFhjTotalByDay(uUUid, "11", beginTime, endTime);
		float total = (float)(Math.round((totalByDay / float1)*100))/100;        //现金转pv

		PageHelper.startPage(page, limit);
		List<ScoreBalance> list = scoreBalanceMapper.selectJBonusFhjByDay(uUUid, "11", beginTime, endTime);
		for (ScoreBalance scoreBalance : list) {
			float score = (float) (Math.round((scoreBalance.getScore() / float1) * 100)) / 100;
			scoreBalance.setScore(score);
			scoreBalance.setOrderSn("分红奖");
		}

		PageInfo<ScoreBalance> pageInfo = new PageInfo<ScoreBalance>(list);
		JBonusFhjResult result = new JBonusFhjResult();
		result.setEarnings(0f);
		result.setTotal(total);
		result.setData(list);
		return result;

	}


	/**
	 * 月份查询
	 */
	@Override
	public JBonusFhjResult selectJBonusFhjByMonth(String uUUid, int page, int limit, String month) {
		Xtcl xtcl = xtclMapper.getClsValue("jjbl_type", "xj");      //现金积分比例
		float float1 = Float.parseFloat(xtcl.getBz_value());                     //0.7

		float totalByMonth = scoreBalanceMapper.selectFhjTotalByMonth(uUUid, "11", month);
		float total = (float)(Math.round((totalByMonth / float1)*100))/100;        //现金转pv

		PageHelper.startPage(page, limit);
		List<ScoreBalance> list = scoreBalanceMapper.selectJBonusFhjByMonth(uUUid, "11", month);
		for (ScoreBalance scoreBalance : list) {
			float score = (float) (Math.round((scoreBalance.getScore() / float1) * 100)) / 100;
			scoreBalance.setScore(score);
			scoreBalance.setOrderSn("分红奖");
		}

		PageInfo<ScoreBalance> pageInfo = new PageInfo<ScoreBalance>(list);
		JBonusFhjResult result = new JBonusFhjResult();
		result.setEarnings(0f);
		result.setTotal(total);
		result.setData(list);
		return result;
	}
}
