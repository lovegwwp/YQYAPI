package com.jyss.yqy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jyss.yqy.entity.BaseConfig;
import com.jyss.yqy.entity.Xtcl;

public interface XtclService {

	/**
	 * 根据标识符取得维护常量
	 * 
	 * @param bz_type
	 * @param bz_value
	 * @return
	 */
	List<Xtcl> getCls();

	/**
	 * 根据标识符搜索
	 * 
	 * @param bz_type
	 * @param bz_value
	 * @return
	 */
	List<Xtcl> getClsBy(@Param("bz_type") String bz_type,
			@Param("bz_value") String bz_value);

	/**
	 * 获取常量下拉
	 * 
	 * @param bz_type
	 * @param pid
	 * @return
	 */
	List<Xtcl> getClsCombox(@Param("bz_type") String bz_type,
			@Param("pid") String pid);

	/**
	 * 根据标识符取得标志对应常量值
	 * 
	 * @param bz_type
	 * @param bz_id
	 * @return
	 */
	Xtcl getClsValue(@Param("bz_type") String bz_type,
			@Param("bz_id") String bz_id);

	/**
	 * 新增
	 * 
	 * @param cl
	 * @return
	 */
	int addCl(Xtcl cl);

	/**
	 * 修改
	 * 
	 * @param cl
	 * @return
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
}
