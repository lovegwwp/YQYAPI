package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyss.yqy.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JRecordService;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserRecordBService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.XtclService;
import com.jyss.yqy.utils.CommTool;

@Controller
public class UserRecordBAction {

	private static final Logger logger = LoggerFactory
			.getLogger(UserRecordBAction.class);

	@Autowired
	private UserRecordBService userRecordBService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private JRecordService recordService;
	@Autowired
	private ScoreBalanceService sBackService;
	@Autowired
	private XtclService clService;
	@Autowired
	private UserService userService;	
	@Autowired
	private OrdersBService obService;

	/**
	 * 绑定用户关系
	 */

	@RequestMapping("/b/writeCode")
	@ResponseBody
	public Map<String, String> addUserRecordB(String token, String bCode) {
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList != null && loginList.size() > 0) {
			UMobileLogin uMobileLogin = loginList.get(0);
			Map<String, String> map = userRecordBService.insertUserRecordB(
					uMobileLogin.getuUuid(), bCode);
			return map;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "-3");
		map.put("status", "false");
		map.put("message", "请重新登陆！");
		map.put("data", "");
		return map;
	}

	/**
	 * 计算辅导奖和积分
	 */

	/*@RequestMapping("/fdj/computeFDJ")
	@ResponseBody
	public void insertJBonusFdj() {
		Map<String, String> map = userRecordBService.insertJBonusFdj();
		logger.info(map.get("message"));
	}*/

	/**
	 * 计算市场奖和积分
	 */
	//@RequestMapping("/scj/computeSCJ")
	//@ResponseBody
	public void insertJBonusScj() {
		Map<String, String> map = recordService.insertJBonusScj();
		logger.info(map.get("message"));
	}

	/**
	 * 计算管理奖的积分
	 */
	/*
	 * @RequestMapping("/glj/computeScore")
	 * 
	 * @ResponseBody public void insertScore(){ Map<String, String> map =
	 * bonusGljService.insertScore(); logger.info(map.get("message")); }
	 */

	/**
	 * 计算分销奖和积分
	 */
	/*@RequestMapping("/fxj/computeFXJ")
	@ResponseBody
	public void insertJBonusFxj() {
		Map<String, String> map = bonusFxjService.insertJBonusFxj();
		logger.info(map.get("message"));
	}*/

	// /**积分按照后台设置比例返还===扣额度***/////
	@RequestMapping("/score/computeScore")
	public void insertBackScore() {
		int count = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		List<ScoreBack> sbLIst = sBackService.getBackScore("", "1", "0",
				CommTool.getNowTimestamp().toString(), CommTool.getTommorowTimestamp().toString());
		if (sbLIst != null && sbLIst.size() > 0) {
			Xtcl cl = clService.getClsValue("jjbl_type", "xj");
			Xtcl cl2 = clService.getClsValue("jjbl_type", "gw");
			float cashPercent = 0.7f;
			float shopPercent = 0.3f;
			if (cl != null && cl.getBz_value() != null
					&& !cl.getBz_value().equals("")) {
				cashPercent = Float.parseFloat(cl.getBz_value());
			}
			if (cl2 != null && cl2.getBz_value() != null
					&& !cl2.getBz_value().equals("")) {
				shopPercent = Float.parseFloat(cl2.getBz_value());
			}
			for (ScoreBack scoreBack : sbLIst) {
				/////判断积分额度，进行相应额度减少
				List<UserBean> uulist = userService.getUserByUuid(scoreBack.getUuuid());
				///用户唯一
				if (uulist!=null&&uulist.size()==1&&uulist.get(0).getTotalPv()>0) {
					///现有剩余额度和返还额度进行比较
					if (uulist.get(0).getTotalPv()<scoreBack.getEachScore()) {
						scoreBack.setEachScore(uulist.get(0).getTotalPv());
					}
				
				if (scoreBack != null && scoreBack.getLeftNum() != 0) {
					float cashScore = 0;
					float shopScore = 0;
					float jyCashScore = 0;
					float jyShopScore = 0;
					cashScore = scoreBack.getEachScore() * cashPercent;
					shopScore = scoreBack.getEachScore() * shopPercent;
					ScoreBalance sb = new ScoreBalance();
					sb.setCategory(9);// /积分返还
					sb.setuUuid(scoreBack.getUuuid());
					sb.setType(1);// 收入
//					List<UserBean> ubList = userService.getUserByUuid(scoreBack
//							.getUuuid());
//					if (ubList != null && ubList.size() == 1) {
						jyCashScore = uulist.get(0).getCashScore();
						jyShopScore = uulist.get(0).getShoppingScore();
					//}
					jyCashScore = jyCashScore + cashScore;
					jyShopScore = jyShopScore + shopScore;
					// //现金积分记录
					sb.setScore(cashScore);
					sb.setJyScore(jyCashScore);
					count = sBackService.addCashScoreBalance(sb);
					// //购物积分记录
					sb.setScore(shopScore);
					sb.setJyScore(jyShopScore);
					count2 = sBackService.addShoppingScoreBalance(sb);
					// //修改uuid对应的购物积分
					if (count + count2 == 2) {
						count3 = userService.updateUserBackScore(jyCashScore,
								jyShopScore, scoreBack.getUuuid());
					}
					// //修改对应积分记录表剩余返还次数..每周固定时间返还
					if (count3 == 1) {
						count3 = 0;
						count3 = sBackService.upBackNum(
								scoreBack.getUuuid(),
								scoreBack.getLeftNum() - 1,
								CommTool.getAddAfterWeekTimestamp(
										scoreBack.getBackTime()).toString());
						//////返还积分 ，增加现金。购物额度，减少totalpv额度
						if (count3 == 1) {
						count3 = userService.updateScoreByFHJ(cashScore+"", shopScore+"",
								(-scoreBack.getEachScore())+"", uulist.get(0).getId() + "", uulist.get(0).getIsChuangke()+"");
						}
						if (count3 == 1) {
							// ///如果返还记录已完成，则判断是否有其他封存记录，按照等级高低来解封，每次解封一条
							if ((scoreBack.getLeftNum() - 1) == 0) {
								List<ScoreBack> sbLIst2 = sBackService
										.getBackScore(scoreBack.getUuuid(),
												"-1", "0", "", "");
								if (sbLIst2 != null && sbLIst2.size() >= 1) {
									sBackService.upBackStatusByID(sbLIst2
											.get(0).getId() + "", "1", "-1");
								}

							}
						}
					}
				}
			}
		  }
		}

	}

	public Map<String,Object> addBalance(String uuid, float total) {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("count", "0");
		m.put("cashScore", "0");
		m.put("shopScore", "0");
		Xtcl cl = clService.getClsValue("jjbl_type", "xj");
		Xtcl cl2 = clService.getClsValue("jjbl_type", "gw");
		float cashPercent = 0.7f;
		float shopPercent = 0.3f;
		float jyCashScore = 0;
		float jyShopScore = 0;
		float cashScore=0;
		float shopScore =0;
		int count = 0;
		if (cl != null && cl.getBz_value() != null
				&& !cl.getBz_value().equals("")) {
			cashPercent = Float.parseFloat(cl.getBz_value());
		}
		if (cl2 != null && cl2.getBz_value() != null
				&& !cl2.getBz_value().equals("")) {
			shopPercent = Float.parseFloat(cl2.getBz_value());
		}

		cashScore = total * cashPercent;
		shopScore = total * shopPercent;
		
		ScoreBalance sb = new ScoreBalance();
		sb.setCategory(11);// /分红奖
		sb.setuUuid(uuid);
		sb.setType(1);// 收入
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList != null && ubList.size() == 1) {
			jyCashScore = ubList.get(0).getCashScore();
			jyShopScore = ubList.get(0).getShoppingScore();
		}
		jyCashScore = jyCashScore + cashScore;
		jyShopScore = jyShopScore + shopScore;
		// //现金积分记录
		sb.setScore(cashScore);
		sb.setJyScore(jyCashScore);
		count = sBackService.addCashScoreBalance(sb);
		// //购物积分记录
		if (count == 1) {
			count = 0;
			sb.setScore(shopScore);
			sb.setJyScore(jyShopScore);
			count = sBackService.addShoppingScoreBalance(sb);
			m.put("count", count+"");
			m.put("cashScore", cashScore+"");
			m.put("shopScore", shopScore+"");
			return m;
		}else{
			return  m;
		}	
	}

	// /**全球分红奖***/////
	// //2018-2-1==计算当天（截止0点，比如24号定时计算24号0点之前的）代言人+代理人的全部费用===平均分配给当天的高级代理人==user表的积分进行相应改动。2记录表增加数据
	//@RequestMapping("/fhj/computeFHJ")
	public void CountTotalFh() {
		String count ="0";
		// ///代言人的总金额---订单表查询当天记录
		UserTotalAmount uta = obService.getRecordDyrSum(CommTool.getYestodayZeroTimestamp().toString(), CommTool.getZeroTimestamp().toString());
		// ///代理人的总金额
		UserTotalAmount uta2 = obService.getOrdersDlrSum(CommTool.getYestodayZeroTimestamp().toString(), CommTool.getZeroTimestamp().toString());
		// ///所有总金额
        double total =uta.getAmount()+uta2.getAmount();
		//分红比例
		Xtcl cl = clService.getClsValue("qqfhj_type", "1");
		double value = 0.05;
		if (cl != null && cl.getBz_value() != null
				&& !cl.getBz_value().equals("")) {
			value = Double.parseDouble(cl.getBz_value());
		}

		// //////获取高级代理人4用户列表、///
		List<UserBean> ublist = userService.getUserByFHJ(null, "1", "2", "4");
		if (ublist != null && ublist.size() > 0) {
			int totalNum = ublist.size();// /高级代理人人数
			// /每个人可以分到的钱/////
			float eachTotal = (float) (total * value / ublist.size());
			for (UserBean userBean : ublist) {
				if (userBean != null && userBean.getId() != 0) {
					float totalPv = userBean.getTotalPv();
					// //有额度才能进行发钱 超过额度 按照额度发钱
					if (totalPv > 0) {	
						if (totalPv < eachTotal) {
							eachTotal = totalPv;
						}
						Map<String,Object> m=addBalance(userBean.getUuid(),eachTotal);
						count =  (String) m.get("count");
						if (count.equals("1")) {
							userService.updateScoreByFHJ(m.get("cashScore")+"", m.get("shopScore")+"",
									(-eachTotal)+"", userBean.getId() + "", "4");
						}
						
					}
				}
			}
		}

	}


	/**
	 * 每天定时更改用户等级////==在所有奖项之后计算
	 */
	@RequestMapping("/updateDldjByDay")
	@ResponseBody
	public void updateDldj() {
		////查出u_user所用代理人等级
		List<UserBean> ueseList = userService.getUserInfo(null,null,null,"1","2",null);
		////查出u_u_user_r_record_b 已有等级
		List<UUserRRecordB> uRecordBList = userRecordBService.getRecordBGroupByRid();
		///两次循环不同等级，如果等级不同，则进行查出u_u_user_r_record_b相应更改
        for (UUserRRecordB  recordeb:uRecordBList){
			for (UserBean  ub:ueseList){
				//同一用户进行等级比较
				if (recordeb.getrId()==ub.getId()){
					//等级不同进行更改
					if(recordeb.getType()!=ub.getIsChuangke()){
						userRecordBService.updateTypeByUid(ub.getIsChuangke()+"",recordeb.getrId()+"","1");
					}
				}

			}
		}
		System.out.print("用户初始绑定关系等级更改完毕");
	}

}
