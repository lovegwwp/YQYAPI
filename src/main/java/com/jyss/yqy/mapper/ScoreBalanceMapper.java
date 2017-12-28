package com.jyss.yqy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ScoreBack;
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

	// 现金积分插入(创建时间减6小时)
	int addCashScore(ScoreBalance sb);

	// 购物积分插入(创建时间减6小时)
	int addShoppingScore(ScoreBalance sb);

	// /////////积分返还///////////////
	// 积分记录查询
	List<ScoreBack> getBackScore(@Param("uuuid") String uuuid,
			@Param("status") String status, @Param("backTime") String backTime,
			@Param("backTime1") String backTime1);

	// 插入积分记录
	int addBackScore(ScoreBack sBack);

	// 修改剩余返还次数
	int upBackNum(@Param("uId") String uId, @Param("backNum") int backNum,
			@Param("backTime") String backTime);

}
