package com.jyss.yqy.service.impl;

import java.util.List;

import com.jyss.yqy.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.XtclService;

@Service
@Transactional
public class XtclServiceImpl implements XtclService {

	@Autowired
	private XtclMapper xtclMapper;

	@Override
	public List<Xtcl> getCls() {
		// TODO Auto-generated method stub
		return xtclMapper.getCls();
	}

	@Override
	public List<Xtcl> getClsBy(String bz_type, String bz_value) {

		return xtclMapper.getClsBy(bz_type, bz_value);
	}

	@Override
	public int addCl(Xtcl cl) {
		// TODO Auto-generated method stub
		return xtclMapper.addCl(cl);
	}

	@Override
	public int updateCl(Xtcl cl) {
		// TODO Auto-generated method stub
		return xtclMapper.updateCl(cl);
	}

	@Override
	public int deleteCl(List<Long> ids) {
		// TODO Auto-generated method stub
		return xtclMapper.deleteCl(ids);
	}

	@Override
	public List<Xtcl> getClsCombox(String bz_type, String pid) {
		// TODO Auto-generated method stub
		return xtclMapper.getClsCombox(bz_type, pid);
	}

	@Override
	public Xtcl getClsValue(String bz_type, String bz_id) {

		return xtclMapper.getClsValue(bz_type, bz_id);
	}

	// /////////////////////系统基础维护表/////////////////////////////////
	/**
	 * 系统基础维护表
	 * 
	 * @return
	 */

	@Override
	public List<BaseConfig> getBcs(String key, String title) {
		// TODO Auto-generated method stub
		return xtclMapper.getBcs(key, title);
	}

	/**
	 * 系统基础地域表
	 * 
	 * @return
	 */
	@Override
	public List<BaseArea> getBaseAreas(String status, String area) {
		// TODO Auto-generated method stub
		return xtclMapper.getBaseAreas(status, area);
	}


	/**
	 * 查询分享
	 * @return
	 */
	@Override
	public List<BaseShare> getBaseShare(String shareKey) {
		return xtclMapper.getBaseShare(shareKey);
	}

	/**
	 * 安卓版本更新
	 */
	@Override
	public List<Xtgx> selectXtgx(Integer type) {
		return xtclMapper.selectXtgx(type);
	}
}
