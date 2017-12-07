package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jyss.yqy.entity.JBonusScj;
import com.jyss.yqy.entity.JRecord;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusScjMapper;
import com.jyss.yqy.mapper.JRecordMapper;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.UserMapper;
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
	@Autowired
	private UserMapper userMapper;

	
	/**
	 * 计算市场奖
	 */
	@Override
	public Map<String,String> insertJBonusScj(){
		
		insertJbonusScj();       //修改今日pv
		
		Map<String,String> map = new HashMap<String,String>();
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
	 * 计算总pv
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
	 * 计算市场奖前修改今日pv
	 */
	private void insertJbonusScj(){
		int record = recordMapper.updateJRecord();     //将每日的pv值清0
		if(record > 0){
			List<JRecord> orderPvList = ordersBMapper.getSuccessOrderPv();
			for (JRecord jRecord : orderPvList) {
				recordMapper.updateJRecordByUid(jRecord.getPv()+"",null,jRecord.getuId());
			}
		}
	}

	
	/**
	 * 添加市场用户
	 */
	@Override
	public Map<String,String> insertJRecord(String uAccount,String pAccount,int depart) {
		Map<String, String> map = new HashMap<String,String>();
		if(StringUtils.isEmpty(uAccount)){
			map.put("message", "账号不能为空！");
			return map;
		}
		List<JRecord> recordList = recordMapper.selectAllJRecord(uAccount);
		if(recordList != null && recordList.size()>0){
			map.put("message", "账号已被分配，不可重复分配！");
			return map;
		}
		JRecord jRecord = new JRecord();
		if(depart == 0){
			List<JRecord> list = recordMapper.selectJRecordByPid(0);
			if(list != null && list.size()>0){
				map.put("message", "最上级已存在，不可再添加！");
				return map;
			}
			List<UserBean> uList = userMapper.getUserIdByAccount(uAccount);
			if(uList != null && uList.size()>0){
				UserBean uUserBean = uList.get(0);
				jRecord.setuId(uUserBean.getId());
				jRecord.setuAccount(uAccount);
				jRecord.setuName(uUserBean.getRealName());
				jRecord.setParentId(0);
				jRecord.setDepart(depart);
				jRecord.setFloor(1);
				jRecord.setStatus(1);
				int count = recordMapper.insertJRecord(jRecord);
				if(count == 1){
					map.put("message", "添加成功！");
					return map;
				}
				map.put("message", "添加失败！");
				return map;
			}
			map.put("message", "用户不可用！");
			return map;
		}else if(depart == 1 || depart == 2){
			List<UserBean> uList = userMapper.getUserIdByAccount(uAccount);
			if(uList != null && uList.size()>0){
				UserBean uUserBean = uList.get(0);
				List<UserBean> pList = userMapper.getUserIdByAccount(pAccount);
				if(pList != null && pList.size()>0){
					UserBean pUserBean = pList.get(0);
					
					List<JRecord> recordPList = recordMapper.selectJRecordByUid(pUserBean.getId());
					if(recordPList != null && recordPList.size()==1){
						JRecord recordP = recordPList.get(0);
						Integer floor = recordP.getFloor();
						floor++;
						//确认市场分配
						List<JRecord> pJRecordList = recordMapper.selectJRecordByPid(pUserBean.getId());
						if(pJRecordList != null && pJRecordList.size() == 1){
							JRecord record1 = pJRecordList.get(0);
							Integer depart1 = record1.getDepart();
							if((depart == 1 && depart1 == 2) || (depart == 2 && depart1 == 1)){
								jRecord.setuId(uUserBean.getId());
								jRecord.setuAccount(uAccount);
								jRecord.setuName(uUserBean.getRealName());
								jRecord.setParentId(pUserBean.getId());
								jRecord.setDepart(depart);
								jRecord.setFloor(floor);
								jRecord.setStatus(1);
								int count = recordMapper.insertJRecord(jRecord);
								if(count == 1){
									map.put("message", ""+uAccount+"分配市场成功！");
									return map;
								}
								map.put("message", "添加失败！");
								return map;
							}
							map.put("message", "该市场已存在用户！");
							return map;
							
						}else if(pJRecordList == null || pJRecordList.size() == 0){
							jRecord.setuId(uUserBean.getId());
							jRecord.setuAccount(uAccount);
							jRecord.setuName(uUserBean.getRealName());
							jRecord.setParentId(pUserBean.getId());
							jRecord.setDepart(depart);
							jRecord.setFloor(floor);
							jRecord.setStatus(1);
							int count = recordMapper.insertJRecord(jRecord);
							if(count == 1){
								map.put("message", ""+uAccount+"分配市场成功！");
								return map;
							}
							map.put("message", "添加失败！");
							return map;
						}
						map.put("message", "上级用户已有两个市场，请选择其他上级！");
						return map;
					}
					map.put("message", "上级用户还没被分配市场！");
					return map;
				}
				map.put("message", "上级账号不可用！");
				return map;
			}
			map.put("message", "账号不可用！");
			return map;
		}
		map.put("message", "请确认分配市场！");
		return map;
	}
	
	/**
	 * 展示市场用户
	 */
	@Override
	public List<JRecord> getJRecordList(){
		return recordMapper.selectAllJRecord(null);
	}
	
	
	/**
	 * 修改市场用户
	 */
	@Override
	public  Map<String,String> updateJRecord(int id,String account){
		Map<String,String> map = new HashMap<String,String>();
		if(StringUtils.isEmpty(account)){
			map.put("message", "账号不能为空！");
			return map;
		}
		List<JRecord> list = recordMapper.selectJRecordById(id);
		if(list != null && list.size()>0){
			List<UserBean> userList = userMapper.getUserIdByAccount(account);
			if(userList != null && userList.size()>0){
				List<JRecord> recordList = recordMapper.selectAllJRecord(account);
				if(recordList == null || recordList.size() == 0){
					UserBean userBean = userList.get(0);
					JRecord record = new JRecord();
					record.setId(id);
					record.setuId(userBean.getId());
					record.setuAccount(account);
					record.setuName(userBean.getRealName());
					int count = recordMapper.updateJRecordById(record);
					if(count == 1){
						JRecord record2 = list.get(0);
						List<JRecord> pRecordList = recordMapper.selectJRecordByPid(record2.getuId());
						for (JRecord pRecord : pRecordList) {
							recordMapper.upJRecordById(userBean.getId(), pRecord.getId());
						}
						map.put("message", "修改成功！");
						return map;
						
					}
					map.put("message", "修改失败！");
					return map;
				}
				map.put("message", "用户已被分配，修改失败！");
				return map;
			}
			map.put("message", "账号不可用！");
			return map;
		}
		map.put("message", "请重新修改！");
		return map;
	}
	
	
	/**
	 * 删除市场用户
	 */
	@Override
	public Map<String,String> deleteJRecord(int id){
		Map<String,String> map = new HashMap<String,String>();
		List<JRecord> list = recordMapper.selectJRecordById(id);
		if(list != null && list.size()>0){
			JRecord record = list.get(0);
			List<JRecord> recordList = recordMapper.selectJRecordByPid(record.getuId());
			if(recordList != null && recordList.size()>0){
				for (JRecord jRecord : recordList) {
					deleteJRecord(jRecord.getId());
				}
			}
			int count = recordMapper.deleteJRecordById(id);
			if(count > 0){
				map.put("message", "删除成功！");
				return map;
			}
			map.put("message", "删除失败！");
			return map;
		}
		map.put("message", "删除失败！");
		return map;
	}

}
