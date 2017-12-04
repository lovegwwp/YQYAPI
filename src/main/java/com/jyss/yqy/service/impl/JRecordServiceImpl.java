package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.JRecordMapper;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.service.JRecordService;

@Service
@Transactional
public class JRecordServiceImpl implements JRecordService{
	
	@Autowired
	private JRecordMapper recordMapper;
	@Autowired
	private JBonusScjMapper bonusScjMapper;
	@Autowired
	private OrdersBMapper ordersBMapper;
	
	/**
	 * 计算市场奖
	 */
	public Map<String,String> insertJBonusScj(){
		Map<String,String> map = new HashMap<String,String>();
		insertJbonusScj();       //修改今日pv
		
		List<JRecord> uIdList = recordMapper.selectJRecord();  //查询用户列表
		for (int i = 0; i < uIdList.size(); i++) {
        	JRecord jRecord = uIdList.get(i);
			JBonusScj bonusScj = new JBonusScj();
			List<JRecord> recordList = recordMapper.selectJRecordByPid(jRecord.getuId());  //查询子列表
			if(recordList != null && recordList.size()>1){
				bonusScj.setuId(jRecord.getuId());
				JRecord record1 = recordList.get(0);
				JRecord record2 = recordList.get(1);
				Integer depart1 = record1.getDepart();
				Integer depart2 = record2.getDepart();
				if(depart1 == 1 && depart2 == 2){
					bonusScj.setaId(record1.getuId());
					bonusScj.setbId(record2.getuId());
					float total1 = getTotal(record1.getuId());
					bonusScj.setaPv(total1);
					float total2 = getTotal(record2.getuId());
					bonusScj.setbPv(total2);
					bonusScj.setPv(Math.min(total1, total2));
					bonusScj.setStatus(1);
					bonusScjMapper.insertBonusScj(bonusScj);
					
					if(total1 >= total2){
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record1.getuId());
						recordMapper.updateJRecordByUid(null, 0.00+"", record2.getuId());
					}else {
						recordMapper.updateJRecordByUid(null, 0.00+"", record1.getuId());
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record2.getuId());
					}
				}else if(depart1 == 2 && depart2 == 1){
					bonusScj.setaId(record2.getuId());
					bonusScj.setbId(record1.getuId());
					float total1 = getTotal(record2.getuId());
					bonusScj.setaPv(total1);
					float total2 = getTotal(record1.getuId());
					bonusScj.setbPv(total2);
					bonusScj.setPv(Math.min(total1, total2));
					bonusScj.setStatus(1);
					bonusScjMapper.insertBonusScj(bonusScj);
					
					if(total1 >= total2){
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record2.getuId());
						recordMapper.updateJRecordByUid(null, 0.00+"", record1.getuId());
					}else {
						recordMapper.updateJRecordByUid(null, 0.00+"", record2.getuId());
						recordMapper.updateJRecordByUid(null, Math.abs(total1-total2)+"", record1.getuId());
					}
				}
				map.put("message", "市场奖计算成功！"+new Date());
			}
			
		}
		return map;
		
	}
	
	/**
	 * 计算递归后总pv
	 */
	private float getTotal(int id){
		Float total = 0.00f;
		List<JRecord> list = getJRecordList(id);
        for (int i = 0; i < list.size(); i++) {
        	JRecord record = list.get(i);
        	total += record.getPv();
        }
		return total;
	}

	/**
	 * 二叉树递归的前序遍历
	 */
	public List<JRecord> getJRecordList(int uId){
		List<JRecord> result = new ArrayList<JRecord>();
		getJRecordList(uId,result);
		return result;
	}
	private void getJRecordList(int id,List<JRecord> result){
		//如果节点为空，返回
        if("null".equals(String.valueOf(id))){
            return;
        }
        //不为空，则加入节点的值
        List<JRecord> list = recordMapper.selectJRecordByUid(id);
        if(list != null && list.size()>0 ){
        	//JRecord record = list.get(0);
        	result.add(list.get(0));
        	List<JRecord> recordList = recordMapper.selectJRecordByPid(id);
        	if(recordList != null && recordList.size()>1){
        		JRecord record1 = recordList.get(0);
        		JRecord record2 = recordList.get(1);
        		Integer depart1 = record1.getDepart();
        		Integer depart2 = record2.getDepart();
        		if(depart1 == 1 && depart2 == 2){
        			//先递归左孩子
        			getJRecordList(record1.getuId(),result);
        			//再递归右孩子
        	        getJRecordList(record2.getuId(),result);
        		}else if(depart1 == 2 && depart2 == 1){
        			//先递归左孩子
        			getJRecordList(record2.getuId(),result);
        			//再递归右孩子
        	        getJRecordList(record1.getuId(),result);
        		}
        	}
        }
		
	}
	
	
	/**
	 * 修改今日pv
	 */
	private void insertJbonusScj(){
		List<JRecord> list = recordMapper.selectJRecord(); 
		for (JRecord record : list) {
			recordMapper.updateJRecordByUid(0.00+"",null,record.getuId());  //将每日的pv值置0
		}
		List<JRecord> orderPvList = ordersBMapper.getSuccessOrderPv();
		for (JRecord jRecord : orderPvList) {
			recordMapper.updateJRecordByUid(jRecord.getPv()+"",null,jRecord.getuId());
		}
	}
	

}
