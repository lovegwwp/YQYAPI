package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.ScoreBalance;

public interface ScoreBalanceService {
	/**
	 * 现金积分明细
	 */
	List<ScoreBalance> getCashScoreBalance(@Param("uUuid") String uUuid);

	/**
	 * 购物积分明细
	 */
	List<ScoreBalance> getShoppingScoreBalance(@Param("uUuid") String uUuid);


	/**
	 * 电子券积分明细
	 */
	List<ScoreBalance> getElecScoreBalance(@Param("uUuid") String uUuid);


	/**
	 * 报单券积分明细
	 */
	List<ScoreBalance> getEntryScoreBalance(@Param("uUuid") String uUuid);


	/**
	 * 税费，平台管理费，手续费明细
	 */
	List<ScoreBalance> getScoreDetails(@Param("uUuid") String uUuid);


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

	// /////////积分返还///////////////
	// 积分记录查询
	List<ScoreBack> getBackScore(@Param("uuuid") String uuuid,
			@Param("status") String status,@Param("leftNum") String leftNum, @Param("backTime") String backTime,
			@Param("backTime1") String backTime1);

	// 插入积分记录
	int addBackScore(ScoreBack sBack);

	// 修改剩余返还次数
	int upBackNum(@Param("uId") String uId, @Param("backNum") int backNum,
			@Param("backTime") String backTime);
	
	// 修改返还状态==更新返还数据状态1=正常，-1=数据封存状态，-2=返还完毕', -
	int upBackStatus(@Param("uId") String uId, @Param("status") String status,
			@Param("statusBefore") String statusBefore);
	
	// 修改返还状态==更新返还数据状态1=正常，-1=数据封存状态，-2=返还完毕', -
	int upBackStatusByID(@Param("id") String id, @Param("status") String status,
			@Param("statusBefore") String statusBefore);

}
