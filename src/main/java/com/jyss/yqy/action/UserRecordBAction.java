package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.JBonusFxjService;
import com.jyss.yqy.service.JBonusGljService;
import com.jyss.yqy.service.JRecordService;
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
	private JBonusGljService bonusGljService;
	@Autowired
	private JBonusFxjService bonusFxjService;
	@Autowired
	private ScoreBalanceService sBackService;
	@Autowired
	private XtclService clService;
	@Autowired
	private UserService userService;

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

	@RequestMapping("/fdj/computeFDJ")
	@ResponseBody
	public void insertJBonusFdj() {
		Map<String, String> map = userRecordBService.insertJBonusFdj();
		logger.info(map.get("message"));
	}

	/**
	 * 计算市场奖和积分
	 */
	@RequestMapping("/scj/computeSCJ")
	@ResponseBody
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
	@RequestMapping("/fxj/computeFXJ")
	@ResponseBody
	public void insertJBonusFxj() {
		Map<String, String> map = bonusFxjService.insertJBonusFxj();
		logger.info(map.get("message"));
	}

	// /**积分双倍返还***/////
	public void insertBackScore() {
		int count = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		List<ScoreBack> sbLIst = sBackService.getBackScore("", "1", CommTool
				.getNowTimestamp().toString(), CommTool.getTommorowTimestamp()
				.toString());
		if (sbLIst != null && sbLIst.size() > 0) {
			Xtcl cl = clService.getClsValue("jjbl_type", "xj");
			Xtcl cl2 = clService.getClsValue("jjbl_type", "gw");
			float cashPercent = 0.7f;
			float shopPercent = 0.2f;
			if (cl != null && cl.getBz_value() != null
					&& !cl.getBz_value().equals("")) {
				cashPercent = Float.parseFloat(cl.getBz_value());
			}
			if (cl2 != null && cl2.getBz_value() != null
					&& !cl2.getBz_value().equals("")) {
				shopPercent = Float.parseFloat(cl.getBz_value());
			}
			for (ScoreBack scoreBack : sbLIst) {
				if (scoreBack != null && scoreBack.getBackNum() != 0) {
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
					List<UserBean> ubList = userService.getUserByUuid(scoreBack
							.getUuuid());
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
					sb.setScore(shopScore);
					sb.setJyScore(jyShopScore);
					count2 = sBackService.addShoppingScoreBalance(sb);
					// //修改uuid对应的购物积分
					if (count + count2 == 2) {
						count3 = userService.updateUserBackScore(jyCashScore,
								jyShopScore, scoreBack.getUuuid());
					}
					// //修改对应积分记录表剩余返还次数
					if (count3 == 1) {
						sBackService.upBackNum(
								scoreBack.getUuuid(),
								scoreBack.getBackNum() - 1,
								CommTool.getAddAfterWeekTimestamp(
										scoreBack.getBackTime()).toString());
					}
				}
			}
		}

	}

}
