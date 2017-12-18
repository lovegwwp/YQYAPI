package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ScoreBalance;

public interface ScoreBalanceMapper {
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
	
	//现金积分插入(创建时间减12小时)
	int addCashScore(ScoreBalance sb);
	
	//购物积分插入(创建时间减12小时)
	int addShoppingScore(ScoreBalance sb);

}
