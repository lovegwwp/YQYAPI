package com.jyss.yqy.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.jyss.yqy.entity.JBonusGlj;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.UUserRRecordBExample;
import com.jyss.yqy.entity.UUserRRecordBExample.Criteria;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.JBonusGljMapper;
import com.jyss.yqy.mapper.UUserRRecordBMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.service.UserRecordBService;

@Service
@Transactional
public class UserRecordBServiceImpl implements UserRecordBService{

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UUserRRecordBMapper userRecordMapper;
	@Autowired
	private JBonusGljMapper bonusGljMapper;

	@Override
	public Map<String,String> insertUserRecordB(String uuid, String bCode) {
		Map<String, String> map = new HashMap<String,String>();
		List<UserBean> userList = userMapper.getUserByUuid(uuid);
		if(userList == null || userList.size()==0){
			map.put("code", "-1");
			map.put("status", "false");
			map.put("message", "用户不存在");
			map.put("data", "");
			return map;
		}
		UserBean userBean = userList.get(0);         //获取被推荐人信息
		int isAuth = userBean.getIsAuth();
		int uLevel = userBean.getIsChuangke();
		List<UserBean> parentList = userMapper.getUserByBCode(bCode);
		if(parentList != null && parentList.size()>0){
			UserBean parentUser = parentList.get(0);      //获取推荐人信息
			int pLevel = parentUser.getIsChuangke();
			//推荐关系表
			if(isAuth==2 && (uLevel==2 || uLevel==3 || uLevel==4) && (pLevel==2 || pLevel==3 || pLevel==4)){
				UUserRRecordB userRRecordB = new UUserRRecordB();
				userRRecordB.setuId(userBean.getId());
				userRRecordB.setrId(parentUser.getId());
				userRRecordB.setStatus(1);
				userRRecordB.setType(parentUser.getIsChuangke());
				userRRecordB.setCreatedAt(new Date());
				int idNum = userRecordMapper.insert(userRRecordB);
				String val=idNum +""; 
				if(val != null && !"".equals(val)){
					//管理奖表
					JBonusGlj bonusGlj = new JBonusGlj();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bonusGlj.setuId(userBean.getId());
					bonusGlj.setuName(userBean.getRealName());
					bonusGlj.setParentId(parentUser.getId());
					bonusGlj.setStatus(1);
					bonusGlj.setCreated(sdf.format(new Date()));
					UUserRRecordBExample example = new UUserRRecordBExample();
					Criteria criteria = example.createCriteria();
					if(pLevel == 4){
						bonusGlj.setParentMoney(120.00);
						bonusGljMapper.insert(bonusGlj);
						
					}else if(pLevel == 3){
						bonusGlj.setParentMoney(80.00);
						bonusGljMapper.insert(bonusGlj);
						
						criteria.andUIdEqualTo(parentUser.getId());
						criteria.andStatusEqualTo(1);
						List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
						if(list != null && list.size()>0){
							
							UUserRRecordB userRecord = list.get(0);
							List<UserBean> nameById = userMapper.getUserNameById(userRecord.getuId());
							if(nameById != null && nameById.size()>0){
								UserBean userBean1 = nameById.get(0);
								JBonusGlj bonusGlj1 = new JBonusGlj();
								bonusGlj1.setuId(userRecord.getuId());
								bonusGlj1.setuName(userBean1.getRealName());
								bonusGlj1.setParentId(userRecord.getrId());
								bonusGlj1.setParentMoney(40.00);
								bonusGlj1.setStatus(1);
								bonusGlj1.setCreated(sdf.format(new Date()));
								bonusGljMapper.insert(bonusGlj1);
							}
						}
					}else if(pLevel == 2){
						bonusGlj.setParentMoney(40.00);
						bonusGljMapper.insert(bonusGlj);
						
						criteria.andUIdEqualTo(parentUser.getId());
						criteria.andStatusEqualTo(1);
						List<UUserRRecordB> list = userRecordMapper.selectByExample(example);
						if(list != null && list.size()>0){
							UUserRRecordB userRecord = list.get(0);
							List<UserBean> nameById = userMapper.getUserNameById(userRecord.getuId());
							if(nameById != null && nameById.size()>0){
								UserBean userBean1 = nameById.get(0);
								int level1 = userBean1.getIsChuangke();
								if(level1 == 3){
									JBonusGlj bonusGlj1 = new JBonusGlj();
									bonusGlj1.setuId(userRecord.getuId());
									bonusGlj1.setuName(userBean1.getRealName());
									bonusGlj1.setParentId(userRecord.getrId());
									bonusGlj1.setParentMoney(80.00);
									bonusGlj1.setStatus(1);
									bonusGlj1.setCreated(sdf.format(new Date()));
									bonusGljMapper.insert(bonusGlj1);
								}else if(level1 == 2){
									JBonusGlj bonusGlj1 = new JBonusGlj();
									bonusGlj1.setuId(userRecord.getuId());
									bonusGlj1.setuName(userBean1.getRealName());
									bonusGlj1.setParentId(userRecord.getrId());
									bonusGlj1.setParentMoney(40.00);
									bonusGlj1.setStatus(1);
									bonusGlj1.setCreated(sdf.format(new Date()));
									bonusGljMapper.insert(bonusGlj1);
									
									UUserRRecordBExample example1 = new UUserRRecordBExample();
									Criteria criteria1 = example1.createCriteria();
									criteria1.andUIdEqualTo(userRecord.getrId());
									criteria1.andStatusEqualTo(1);
									List<UUserRRecordB> list2 = userRecordMapper.selectByExample(example1);
									if(list2 != null && list2.size()>0){
										UUserRRecordB userRecord2 = list2.get(0);
										List<UserBean> nameById2 = userMapper.getUserNameById(userRecord2.getuId());
										if(nameById2 != null && nameById2.size()>0){
											UserBean userBean2 = nameById2.get(0);
											JBonusGlj bonusGlj2 = new JBonusGlj();
											bonusGlj2.setuId(userRecord2.getuId());
											bonusGlj2.setuName(userBean2.getRealName());
											bonusGlj2.setParentId(userRecord2.getrId());
											bonusGlj2.setParentMoney(40.00);
											bonusGlj2.setStatus(1);
											bonusGlj2.setCreated(sdf.format(new Date()));
											bonusGljMapper.insert(bonusGlj2);
										}
									}
								}
							}
						}
						
						
					}
					map.put("code", "0");
					map.put("status", "true");
					map.put("message", "推荐码使用成功！");
					map.put("data", "");
					return map;
				}
			}
			map.put("code", "-2");
			map.put("status", "false");
			map.put("message", "您还不是代理人！");
			map.put("data", "");
			return map;
			
		}
		map.put("code", "-3");
		map.put("status", "false");
		map.put("message", "此推荐码不可用！");
		map.put("data", "");
		return map;
	}
	
	

}
