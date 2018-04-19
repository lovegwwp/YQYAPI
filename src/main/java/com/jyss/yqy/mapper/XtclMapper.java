package com.jyss.yqy.mapper;

import java.util.List;

import com.jyss.yqy.entity.*;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface XtclMapper {
	/**
	 * 根据标识符取得维护常量
	 * 
	 * @return
	 */
	List<Xtcl> getCls();

	/**
	 * 根据标识符搜索
	 */
	List<Xtcl> getClsBy(@Param("bz_type") String bz_type,@Param("bz_value") String bz_value);

	/**
	 * 获取常量下拉
	 * 
	 * @param bz_type
	 * @param pid
	 * @return
	 */
	List<Xtcl> getClsCombox(@Param("bz_type") String bz_type,
			@Param("pid") String pid);


	//根据标识符取得标志对应常量值
	Xtcl getClsValue(@Param("bz_type") String bz_type,@Param("bz_id") String bz_id);


	/**
	 * 新增
	 * 
	 * @param cl
	 * @return
	 */
	int addCl(Xtcl cl);


	/**
	 * 修改
	 */
	int updateCl(Xtcl cl);


	/**
	 * 删除 批量
	 * 
	 * @param ids
	 * @return
	 */
	int deleteCl(@Param("ids") List<Long> ids);

	// /////////////////////系统基础维护表/////////////////////////////////
	/**
	 * 系统基础维护表
	 * 
	 * @return
	 */
	List<BaseConfig> getBcs(@Param("key") String key,
			@Param("title") String title);

	/**
	 * 系统基础地域表
	 * 
	 * @return
	 */
	List<BaseArea> getBaseAreas(@Param("status") String status,
			@Param("area") String area);


	//查询分享
	List<BaseShare> getBaseShare(@Param("shareKey") String shareKey);

	//安卓版本更新
	List<Xtgx> selectXtgx(@Param("type") Integer type);

}
