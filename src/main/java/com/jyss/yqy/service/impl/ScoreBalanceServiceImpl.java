package com.jyss.yqy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		// TODO Auto-generated method stub
		return sbMapper.getCashScoreBalance(uUuid);
	}

	@Override
	public List<ScoreBalance> getShoppingScoreBalance(String uUuid) {
		// TODO Auto-generated method stub
		return sbMapper.getShoppingScoreBalance(uUuid);
	}

	@Override
	public int addCashScoreBalance(ScoreBalance sb) {
		// TODO Auto-generated method stub
		sb.setEnd(2);
		sb.setCreatedAt(CommTool.getNowTimestamp());
		return sbMapper.addCashScoreBalance(sb);
	}

	@Override
	public int addShoppingScoreBalance(ScoreBalance sb) {
		// TODO Auto-generated method stub
		sb.setEnd(2);
		sb.setCreatedAt(CommTool.getNowTimestamp());
		return sbMapper.addShoppingScoreBalance(sb);
	}

}
