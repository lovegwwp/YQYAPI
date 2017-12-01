package com.jyss.yqy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.mapper.JRecordMapper;
import com.jyss.yqy.service.JRecordService;

@Service
@Transactional
public class JRecordServiceImpl implements JRecordService{
	
	@Autowired
	private JRecordMapper recordMapper;
	
	public Map<String,String> insertJBonusScj(){
		Map<String,String> map = new HashMap<String,String>();
		
		List<JRecord> recordList = recordMapper.selectJRecord();
		for (JRecord jRecord : recordList) {
			
		}
		return map;
		
	}
	
	

}
