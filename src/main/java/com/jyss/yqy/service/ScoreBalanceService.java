package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ScoreBalance;

public interface ScoreBalanceService {
	/**
	 * 现金积分查询
	 * 
	 * @param uUuid
	 * @return
	 */
	List<ScoreBalance> getCashScoreBalance(@Param("uUuid") String uUuid);

	/**
	 * 购物积分查询
	 * 
	 * @param uUuid
	 * @return
	 */
	List<ScoreBalance> getShoppingScoreBalance(@Param("uUuid") String uUuid);

	/**
	 * 现金积分插入
	 * 
	 * @param sb
	 * @return
	 */
	int addCashScoreBalance(ScoreBalance sb);

	/**
	 * 购物积分插入
	 * 
	 * @param sb
	 * @return
	 */
	int addShoppingScoreBalance(ScoreBalance sb);

}
