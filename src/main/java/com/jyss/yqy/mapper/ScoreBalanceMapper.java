package com.jyss.yqy.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.ScoreBalance;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreBalanceMapper {

	/**
	 * 现金积分查询
	 */
	List<ScoreBalance> getCashScoreBalance(@Param("uUuid") String uUuid);

	/**
	 * 购物积分查询
	 */
	List<ScoreBalance> getShoppingScoreBalance(@Param("uUuid") String uUuid);

	/**
	 * 现金积分插入
	 */
	int addCashScoreBalance(ScoreBalance sb);

	/**
	 * 购物积分插入
	 */
	int addShoppingScoreBalance(ScoreBalance sb);



	// 现金积分插入(结算时间减12小时)
	int addCashScore(ScoreBalance sb);

	// 购物积分插入(结算时间减12小时)
	int addShoppingScore(ScoreBalance sb);

	//电子券插入(结算时间减12小时)
	int addElecScore(ScoreBalance sb);

	//税费，平台管理费，手续费插入(结算时间减12小时)
	int addScoreDetails(ScoreBalance sb);

	//报单券插入(结算时间减12小时)
	int addEntryScore(ScoreBalance sb);



	//现金积分查询（转账）
	List<ScoreBalance> getCashScore(@Param("uuid")String uuid);

	//电子券积分查询（转账）
	List<ScoreBalance> getElecScore(@Param("uuid")String uuid);

	//报单券积分查询（转账）
	List<ScoreBalance> getEntryScore(@Param("uuid")String uuid);



	//插入报单券充值记录
	int insertEntryScore(ScoreBalance sb);

	//查询报单券充值记录
	List<ScoreBalance> selectEntryScore(@Param("orderSn")String orderSn);

	//更新报单券充值记录
	int updateEntryScore(ScoreBalance sb);



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
