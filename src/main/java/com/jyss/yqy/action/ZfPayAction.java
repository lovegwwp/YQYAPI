package com.jyss.yqy.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.AlipayAppService;
import com.jyss.yqy.service.AlipayService;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserRecordBService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.WxpayService;
import com.jyss.yqy.service.XtclService;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.PasswordUtil;
import com.jyss.yqy.utils.ZxingCodeUtil;

@Controller
public class ZfPayAction {

	@Autowired
	private AlipayService aliService;
	@Autowired
	private AlipayAppService aliAppService;
	@Autowired
	private WxpayService wxService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrdersBService ordersBService;
	@Autowired
	private ScoreBalanceService sBackService;
	@Autowired
	private UserRecordBService recordBService;
	@Autowired
	private XtclService clService;
	@Autowired
	private ScoreBalanceService sbService;

	// type///// 支付方式：1=支付宝，2=微信，3=现金支付
	@RequestMapping(value = "/b/dlrOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> dlrOrder(@RequestParam float money,
			@RequestParam String type, @RequestParam String token,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String filePath = request.getSession().getServletContext()
				.getRealPath("/");
		int index = filePath.indexOf("YQYAPI");
		filePath = filePath.substring(0, index) + "orderCodePng/";
		File f = new File(filePath);
		CommTool.judeDirExists(f);
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuid = uMobileLogin.getuUuid();
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList == null || ubList.size() == 0) {
			map.put("status", "false");
			map.put("message", "用户信息错误！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}

		UserBean ub = ubList.get(0);
		int gmID = ub.getId();
		// ///判断支付方式=== 走不同支付
		if (type.equals("1")) {
			// map = aliService.addDlrOrder(filePath, money, gmID);
			map = aliAppService.getDLROrderString(filePath, money, gmID);
			// map = aliService.addDlrOrder2(filePath, money, gmID);
		} else if (type.equals("2")) {
			map = wxService.dlrWxpay(filePath, money, gmID);
		} else {
			map.put("status", "false");
			map.put("message", "商品信息错误");
			map.put("code", "-3");
			map.put("data", "");
			return map;
		}
		return map;

	}

	@RequestMapping(value = "/b/ymzOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ymzOrder(@RequestParam String type,
			@RequestParam String token, @RequestParam int spId,
			@RequestParam int num, HttpServletRequest request) {
		Map<String, Object> mmap = new HashMap<String, Object>();
		String filePath = request.getSession().getServletContext()
				.getRealPath("/");
		int index = filePath.lastIndexOf("YQYAPI");
		filePath = filePath.substring(0, index) + "orderCodePng/";
		File f = new File(filePath);
		CommTool.judeDirExists(f);
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			mmap.put("status", "false");
			mmap.put("message", "请重新登录");
			mmap.put("code", "-1");
			mmap.put("data", "");
			return mmap;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuid = uMobileLogin.getuUuid();
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList == null || ubList.size() == 0) {
			mmap.put("status", "false");
			mmap.put("message", "用户信息错误！");
			mmap.put("code", "-2");
			mmap.put("data", "");
			return mmap;
		}

		UserBean ub = ubList.get(0);
		int gmID = ub.getId();
		// ///判断支付方式=== 走不同支付
		if (type.equals("1")) {
			// 支付宝
			// mmap = aliService.addYmzOrder2(filePath, gmID, num, spId);
			mmap = aliAppService.getYmzOrderString(filePath, gmID, num, spId);
		} else if (type.equals("2")) {
			// 微信
			mmap = wxService.ymzWxpay(filePath, gmID, num, spId);
		} else if (type.equals("3")) {
			// 现金积分
			String payPwd = request.getParameter("payPwd");// /支付密码
			mmap = ymzCashScorePay(filePath, gmID, num, spId, payPwd);
		} else {
			mmap.put("status", "false");
			mmap.put("message", "商品信息错误!");
			mmap.put("code", "-3");
			mmap.put("data", "");
			return mmap;

		}

		return mmap;

	}

	// ////现金积分购买商品///////
	public Map<String, Object> ymzCashScorePay(String filePath, int gmID,
			int gmNum, int spID, String payPwd) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		Map<String, Object> mm = new HashMap<String, Object>();
		Map<String, String> mapResponse = new HashMap<String, String>();
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// 支付主题
		String subject = "亚麻籽油消费";
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", outTradeNo);
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfCode", zfCode);
		mm.put("zfPwd", "");// 支付密码
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "3");// 支付方式：1=支付宝，2=微信，3=现金支付

		Goods goods = null;
		goods = ordersBService.getGoodsByid(spID + "");
		if (goods == null) {
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}
		float price = 0;
		float money = 0;
		try {
			price = goods.getPrice();
			money = (float) (gmNum * price);
		} catch (Exception e) {
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！！！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}

		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userService.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
		}
		UserBean ub = ublist.get(0);
		String uuid = ub.getUuid();
		int isChuangke = ub.getIsChuangke();
		if (ub.getIsChuangke() < 2) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
		}
		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")
				|| ub.getPayPwd().equals("0")) {
			zfCode = "0";
			mapRe.put("status", "false");
			mapRe.put("message", "支付密码错误！");
			mapRe.put("code", "-6");
			mapRe.put("data", mm);
			return mapRe;
		} else {
			zfCode = "1";
			// mm.put("zfPwd", ub.getPayPwd());//支付密码
			if (!(PasswordUtil.generatePayPwd(payPwd).equals(ub.getPayPwd()))) {
				mapRe.put("status", "false");
				mapRe.put("message", "支付密码错误！");
				mapRe.put("code", "-6");
				mapRe.put("data", mm);
				return mapRe;
			}
		}
		mm.put("zfCode", zfCode);
		mm.put("xjjf", ub.getCashScore() + "");

		if (ub.getCashScore() < money) {
			mapRe.put("status", "false");
			mapRe.put("message", "现金积分不足！！！！");
			mapRe.put("code", "-1");
			mapRe.put("data", mm);
			return mapRe;
		}
		float cashScore = ub.getCashScore();
		// //// 商品明细列表，需填写购买商品详细信息，进行创建相应订单///////////
		String jb = "0";
		// //所购买的商品信息==亚麻籽油
		int pv = 0;
		String singlePV = "300";
		// //查找对应PV
		Xtcl dlpv = clService.getClsValue("pv_type", "1");
		if (dlpv != null && !dlpv.getBz_value().equals("")) {
			singlePV = dlpv.getBz_value();
		}
		try {
			pv = Integer.parseInt(singlePV) * gmNum;
		} catch (Exception e) {
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}

		// //商品二维码
		String outPutPath = filePath + outTradeNo + ".png";
		String code = "orderCodePng/" + outTradeNo + ".png";
		mm.put("zxingpng", Constant.httpUrl + code);// 订单二维码
		// /生成二维码
		ZxingCodeUtil.zxingCodeCreate(outTradeNo, outPutPath, "2");// /2=代表B端订单
		String gmr = "";

		if (ub.getRealName() == null || ub.getRealName().isEmpty()) {
			gmr = "XXX";
		} else {
			gmr = ub.getRealName();
		}
		OrdersB ob = new OrdersB(outTradeNo, gmID + "", gmr, ub.getAccount(),
				goods.getName(), goods.getPics(), gmNum + "", "盒", "1", "1",
				goods.getPrice(), money, pv, jb, code, "zfId", 3);
		int count = 0;		
		float leftmoney = 0;
		leftmoney = cashScore - money;
		// ///现金积分减少
		count = userService.updateUserBackScore(leftmoney, null, uuid);
		///生成订单
		if (count == 1) {
			count = 0;		
			count = ordersBService.addOrder(ob);
		}
		///现金积分订单明细	
		if (count == 1) {
			count = 0;	
			ScoreBalance sb = new ScoreBalance();
			sb.setuUuid(uuid);
			sb.setCategory(8);//b端消费
			sb.setType(2);//支出
			sb.setScore(money);
			sb.setJyScore(leftmoney);
			sb.setOrderSn(outTradeNo);
			count = sbService.addCashScoreBalance(sb);
		}
		
		// 扣完支付的现金积分,判断是否可以升级,若可以升级，则订单状态dljb也要跟着相应更改
		if (count == 1) {
			count = 0;
			count = updateOrder(uuid, isChuangke, gmNum, gmID + "",outTradeNo);
		}
		if (count == 1) {
			Date sjc = new Date();
			mm.put("outtradeno", outTradeNo);
			mm.put("xjjf", leftmoney + "");
			mm.put("money", money + "");
			mapRe.put("status", "true");
			mapRe.put("message", "提交订单成功！");
			mapRe.put("code", "0");
			mapRe.put("data", mm);
			return mapRe;
		}

		mapRe.put("code", "-4");
		mapRe.put("data", "");
		mapRe.put("status", "false");
		mapRe.put("message", "下单失败！");
		return mapRe;
	}

	// //不管是第一次成为代理人，还是后续升级代理人 都要先判断一下 scoreback是否有返还记录 ，有的话
	// 之前记录状态为-1封存，没有正常状态1添加
	public int addScoreBack(int dlType, String puuid) {
		// 没有1=第一次返还记录==就增加，2=u_ser的total_pv增加额度
		int cun = 0;
		ScoreBack sBack = new ScoreBack();
		sBack.setUuuid(puuid);
		sBack.setDlType(dlType); // 2 ,3,4
		String bz_id = (dlType - 1) + "";
		// /返还日期
		Xtcl cl = clService.getClsValue("fhzq_type", "1");
		int backNum = 100;
		if (cl != null) {
			backNum = Integer.parseInt(cl.getBz_value());
		}
		// 返还积分
		Xtcl cl2 = clService.getClsValue("dyjf_type", bz_id);
		sBack.setBackNum(backNum);
		sBack.setLeftNum(backNum);
		int backScore = 300;
		if (cl2 != null) {
			backScore = Integer.parseInt(cl2.getBz_value());
		}
		sBack.setBackScore(backScore);
		int eachScore = backScore / backNum;
		sBack.setEachScore(eachScore);
		cun = sBackService.addBackScore(sBack);
		// 2=u_ser的total_pv增加额度
		if (cun == 1) {
			cun = 0;
			cun = userService.upTotalPv(puuid, backScore + "");
		}
		return cun;
	}

	// /////////////////订单购买/////////////////////

	/**
	 * 购买支付成功状态修改==ymz
	 */
	public int updateOrder(String uuid, int dlrLevel, int gmNum, String gmID,String orderNo) {

		int count = 0;
		// /最高等级时user=chuangke不进行相应变化,但是返还要增加
		// if (dlrLevel != 4) {
		int level = dlrLevel;
		// /查找等级对应亚麻籽油数量，进行比对升级 5,6,7,8对应初中高级，经理人盒数
		int compareLevel = dlrLevel + 3;
		// /查找往上一等级对应盒数
		Xtcl dlHs = clService.getClsValue("dyjf_type", compareLevel + "");
		int compareNum = Integer.parseInt(dlHs.getBz_value());
		if (gmNum >= compareNum) {
			// //超过数量的购买。直接升级
			if (dlrLevel == 5) {
				dlrLevel = 2;// //经理人升级为初级代理人
			} else if(dlrLevel!= 4){
				dlrLevel = dlrLevel + 1;
			}
			////2018.2.27=修改用户等级，再修改订单dljb
			String dljb ="0";//订单代理级别==【1=初级代理，2=高级代理，3=高级代理，4=经理人，0=不标识状态】
			if(dlrLevel==2){
				dljb ="1";
			}else if(dlrLevel==3){
				dljb ="2";
			}else if(dlrLevel==4){
				dljb ="3";
			}else if(dlrLevel==5){
				dljb ="4";
			}
			if (level != 4) {
				//修改用户等级
				count = userService.upUserAllStatus("", "", "", dlrLevel + "",
						"", gmID);
				//修改订单代理等级
				if (count==1){
					count =0;
					if (!dljb.equals("0")){
					   count = ordersBService.upOrderDljb(dljb,"1",orderNo);
					}
				}
			} else {
				//修改订单代理等级
				if (!dljb.equals("0")){
				   count = ordersBService.upOrderDljb(dljb,"1",orderNo);
				}
			}
			if (count == 1) {
				count = 0;

				// /代理人升级，1=包括返还记录的增加 以及2=低等级返还记录的状态-1数据封存3=用户 total_pv的额度增加
				// //判断是否有返还记录
				List<ScoreBack> sbaList = sBackService.getBackScore(uuid, "1",
						"0", "", "");
				int ccc = 0;
				int count3 = 0;
				// //有返还记录，进行封存，再增加
				if (sbaList != null && sbaList.size() > 0) {
					for (ScoreBack scoreBack : sbaList) {
						if (scoreBack != null && scoreBack.getUuuid() != null
								&& !(scoreBack.getUuuid().equals(""))) {
							count++;
							ccc += sBackService.upBackStatus(
									scoreBack.getUuuid(), "-1", "1");
						}
					}

					if (count == sbaList.size()) {
						count3 = addScoreBack(dlrLevel, uuid);
						return count3;
					}

				} else {
					count3 = addScoreBack(dlrLevel, uuid);
					return count3;
				}
			}
		}
		// }
		return 1;
	}



	/**
	 * 代理人支付成功状态修改
	 */
	public int updateOrderAndUser(String orderNum) {
		int count = 0;
		int count1 = 0;
		// 查询订单存在
		List<OrdersB> obList = ordersBService.getOrdersByPay("-1", orderNum, "");
		if (obList == null || obList.size() != 1) {
			count = 0;
			return count;
		}
		// 更改订单状态
		count = ordersBService.upOrderStatus("1", "-1", orderNum);
		if (count == 1) {
			List<OrdersB> orders = ordersBService.getOrdersBy("1", orderNum,
					null);
			if (orders != null && orders.size() == 1) {
				OrdersB ordersB = orders.get(0);
				// 更改代理人状态
				count1 = userService.upUserAllStatus("1", null, null, null,
						null, ordersB.getGmId());
				if (count1 == 1) {
					// 查询积分返还
					// //当前成为代理人的推荐人uuid
					List<UUserRRecordB> rbList = recordBService.getRecordB(
							ordersB.getGmId(), "", "1");
					if (rbList == null || rbList.size() != 1) {
						return 0;
					}
					String pId = rbList.get(0).getrId() + "";
					List<UserBean> uList = userService.getUserById(pId, "1",
							"2");
					if (uList == null || uList.size() != 1) {
						return 1;// /无上级直接返回
					}
					String puuid = uList.get(0).getUuid();
					int dlType = uList.get(0).getIsChuangke();
					// //判断是否有返还记录
					List<ScoreBack> sbaList = sBackService.getBackScore(puuid,
							"1", "0", "", "");
					// /没有第一次返还记录==就增加
					if (sbaList == null || sbaList.size() == 0) {
						ScoreBack sBack = new ScoreBack();
						sBack.setUuuid(puuid);
						sBack.setDlType(dlType); // 2 ,3,4
						String bz_id = (dlType - 1) + "";
						Xtcl cl = clService.getClsValue("fhzq_type", "1");
						int backNum = 100;
						if (cl != null) {
							backNum = Integer.parseInt(cl.getBz_value());
						}
						Xtcl cl2 = clService.getClsValue("dyjf_type", bz_id);
						sBack.setBackNum(backNum);
						sBack.setLeftNum(backNum);
						int backScore = 300;
						if (cl2 != null) {
							backScore = Integer.parseInt(cl2.getBz_value());
						}
						sBack.setBackScore(backScore);
						int eachScore = backScore / backNum;
						sBack.setEachScore(eachScore);
						int count3 = sBackService.addBackScore(sBack);
						return count3;
					}
				}
				return count1;
			}
		}
		return count;
	}

	// /////////////////订单购买/////////////////////

	/**
	 * 购买支付成功状态修改
	 */
	public int updateOrder1(String orderNum) {
		int count = 0;
		// 查询订单存在
		List<OrdersB> obList = ordersBService.getOrdersByPay("-1", orderNum, "");
		if (obList == null || obList.size() != 1) {
			count = 0;
			return count;
		}
		// 更改订单状态
		count = ordersBService.upOrderStatus("1", "-1", orderNum);
		if (count == 1) {
			try {
				// /判断代理等级，和购买亚麻籽油相应数量，进行比较是否相应进行升级
				int gmNum = Integer.parseInt(obList.get(0).getGmNum());
				// /查找当前购买人等级；
				String gmID = obList.get(0).getGmId();
				List<UserBean> ubList = userService.getUserById(gmID, "1", "2");// /通过审核用户
				if (ubList == null || ubList.size() != 1) {
					count = 0;
					return count;
				}
				// //2=一级代理人 3=二级代理人 4=三级代理人',
				int dlrLevel = ubList.get(0).getIsChuangke();
				// /最高等级时不进行相应变化
				if (dlrLevel != 4) {
					// /查找等级对应亚麻籽油数量，进行比对升级 4,5,6对应初中高级盒数
					int compareLevel = dlrLevel + 3;
					// /查找往上一等级对应盒数
					Xtcl dlHs = clService.getClsValue("dyjf_type", compareLevel
							+ "");
					int compareNum = Integer.parseInt(dlHs.getBz_value());
					if (gmNum >= compareNum) {
						// //超过数量的购买。直接升级
						dlrLevel = dlrLevel + 1;
						count = userService.upUserAllStatus("", "", "",
								dlrLevel + "", "", gmID);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return count;
	}

	/**
	 * 购买支付成功状态修改、、、、、、现金积分购买
	 */
	public int updateOrder2(String orderNum) {
		int count = 0;
		int count1 = 0;
		// 查询订单存在
		List<OrdersB> obList = ordersBService.getOrdersByPay("-1", orderNum, "");
		if (obList == null || obList.size() != 1) {
			count = 0;
			return count;
		}
		// 更改订单状态
		count = ordersBService.upOrderStatus("1", "-1", orderNum);
		if (count == 1) {
			try {
				// /判断代理等级，和购买亚麻籽油相应数量，进行比较是否相应进行升级
				int gmNum = Integer.parseInt(obList.get(0).getGmNum());
				float price = obList.get(0).getPrice();
				float money = gmNum * price;
				String gmID = obList.get(0).getGmId();
				List<UserBean> ubList = userService.getUserById(gmID, "1", "2");// /通过审核用户
				if (ubList == null || ubList.size() != 1) {
					count = 0;
					return count;
				}
				// /扣除相应积分，并增加积分记录
				float cashScore = 0;
				cashScore = ubList.get(0).getCashScore();
				cashScore = cashScore - money;
				count1 = userService.updateUserBackScore(cashScore,
						ubList.get(0).getShoppingScore(), ubList.get(0)
								.getUuid());
				if (count1 != 1) {
					return 0;
				}
				count1 = 0;
				ScoreBalance sb = new ScoreBalance();
				sb.setCategory(8);
				sb.setType(2);
				sb.setuUuid(ubList.get(0).getUuid());
				sb.setScore(money);
				sb.setJyScore(cashScore);
				sb.setOrderSn(orderNum);
				count1 = sBackService.addCashScoreBalance(sb);
				if (count1 != 1) {
					return 0;
				}
				// /查找当前购买人等级；
				// //2=一级代理人 3=二级代理人 4=三级代理人',
				int dlrLevel = ubList.get(0).getIsChuangke();
				// /最高等级时不进行相应变化
				if (dlrLevel != 4) {
					// /查找等级对应亚麻籽油数量，进行比对升级 4,5,6对应初中高级盒数
					int compareLevel = dlrLevel + 3;
					// /查找往上一等级对应盒数
					Xtcl dlHs = clService.getClsValue("dyjf_type", compareLevel
							+ "");
					int compareNum = Integer.parseInt(dlHs.getBz_value());
					if (gmNum >= compareNum) {
						// //超过数量的购买。直接升级
						dlrLevel = dlrLevel + 1;
						count = userService.upUserAllStatus("", "", "",
								dlrLevel + "", "", gmID);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return count;
	}


/////////////type=[1=初始合伙人购买。2=之后复销]//////////////userElec/1=使用电子抵扣////0不使用
	@RequestMapping(value = "/b/yqyOrderPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> yqyOrderPay(@RequestParam Integer type,@RequestParam String payPwd,@RequestParam("userElec") Integer userElec,
										   @RequestParam String token, @RequestParam Integer spId,@RequestParam("hhrmoney") Float hhrmoney,
										   @RequestParam Integer num, HttpServletRequest request) {
		Map<String, Object> mmap = new HashMap<String, Object>();
		String filePath = request.getSession().getServletContext().getRealPath("/");
		int index = filePath.lastIndexOf("YQYAPI");
		filePath = filePath.substring(0, index) + "orderCodePng/";
		File f = new File(filePath);
		CommTool.judeDirExists(f);
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			mmap.put("status", "false");
			mmap.put("message", "请重新登录");
			mmap.put("code", "-1");
			mmap.put("data", "");
			return mmap;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuid = uMobileLogin.getuUuid();
		List<UserBean> ubList = userService.getUserByUuid(uuid);
		if (ubList == null || ubList.size() == 0) {

			mmap.put("status", "false");
			mmap.put("message", "用户信息错误！");
			mmap.put("code", "-2");
			mmap.put("data", "");
			return mmap;

		}

		UserBean ub = ubList.get(0);
		int gmID = ub.getId();
		////type=[1=初始合伙人购买。2=之后复销]
		if(type==1){
			mmap = aliAppService.getHhrOrderString(filePath,gmID, hhrmoney, spId,payPwd);
		}else if(type==2){
			mmap = aliAppService.getGoodOrderString(filePath,userElec, gmID, num, spId,payPwd);
		}

		return mmap;

	}
}
