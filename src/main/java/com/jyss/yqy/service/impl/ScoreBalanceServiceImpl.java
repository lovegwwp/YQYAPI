package com.jyss.yqy.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.utils.CommTool;

@Service
public class ScoreBalanceServiceImpl implements ScoreBalanceService {

	@Autowired
	private ScoreBalanceMapper sbMapper;

	@Override
	public List<ScoreBalance> getCashScoreBalance(String uUuid) {
		return sbMapper.getCashScoreBalance(uUuid);
	}

	@Override
	public List<ScoreBalance> getShoppingScoreBalance(String uUuid) {
		return sbMapper.getShoppingScoreBalance(uUuid);
	}

	@Override
	public List<ScoreBalance> getElecScoreBalance(String uUuid) {
		return sbMapper.getElecScoreBalance(uUuid);
	}

	@Override
	public List<ScoreBalance> getEntryScoreBalance(String uUuid) {
		return sbMapper.getEntryScoreBalance(uUuid);
	}

	@Override
	public List<ScoreBalance> getScoreDetails(String uUuid) {
		return sbMapper.getScoreDetails(uUuid);
	}

	@Override
	public int addCashScoreBalance(ScoreBalance sb) {
		sb.setEnd(2);
		sb.setStatus(1);
		sb.setCreatedAt(CommTool.getNowTimestamp());
		return sbMapper.addCashScoreBalance(sb);
	}

	@Override
	public int addShoppingScoreBalance(ScoreBalance sb) {
		// TODO Auto-generated method stub
		sb.setEnd(2);
		sb.setStatus(1);
		sb.setCreatedAt(CommTool.getNowTimestamp());
		return sbMapper.addShoppingScoreBalance(sb);
	}

	@Override
	public List<ScoreBack> getBackScore(String uuuid, String status,String leftNum,
			String backTime, String backTime1) {
		// TODO Auto-generated method stub
		return sbMapper.getBackScore(uuuid, status,leftNum, backTime, backTime1);
	}

	@Override
	public int addBackScore(ScoreBack sBack) {
		// TODO Auto-generated method stub
		sBack.setStatus(1);
		sBack.setCreatedAt(CommTool.getNowTimestamp());
		sBack.setBackTime(CommTool.getAfterWeekTimestamp());
		return sbMapper.addBackScore(sBack);
	}

	@Override
	public int upBackNum(String uId, int backNum, String backTime) {
		// TODO Auto-generated method stub
		return sbMapper.upBackNum(uId, backNum, backTime);
	}

	@Override
	public int upBackStatus(String uId, String status, String statusBefore) {
		// TODO Auto-generated method stub
		return sbMapper.upBackStatus(uId, status, statusBefore);
	}

	@Override
	public int upBackStatusByID(String id, String status, String statusBefore) {
		// TODO Auto-generated method stub
		return sbMapper.upBackStatusByID(id, status, statusBefore);
	}

}
