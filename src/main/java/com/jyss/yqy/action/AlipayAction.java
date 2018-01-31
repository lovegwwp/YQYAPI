package com.jyss.yqy.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.AlipayAppService;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.service.UserRecordBService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.XtclService;

@Controller
public class AlipayAction {

	private static Log log = LogFactory.getLog(AlipayAction.class);

	@Autowired
	private OrdersBService obService;
	@Autowired
	private XtclService clService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrdersBService ordersBService;
	@Autowired
	private ScoreBalanceService sBackService;
	@Autowired
	private UserRecordBService recordBService;
	@Autowired
	private AlipayAppService aliAppService;
	@Autowired

	// 支付宝当面付2.0服务
	private static AlipayTradeService tradeService;

	// 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	private static AlipayTradeService tradeWithHBService;

	// 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
	private static AlipayMonitorService monitorService;


	// 获取代理参数
	public Map<String, Object> getDlInfo(int money) {
		Map<String, Object> mm = new HashMap<String, Object>();
		String info = "";
		String jb = "1";
		List<Xtcl> fylist = new ArrayList<Xtcl>();
		// //查询充值人对应等级
		fylist = clService.getClsBy("dyf_type", money + "");
		if (fylist != null && fylist.size() == 1) {
			jb = fylist.get(0).getBz_id();
		} else {
			// ///查不到对应充值等级
			mm.put("jb", "0");
			mm.put("hs", "0");
			return mm;
		}
		mm.put("jb", jb);
		// //查询级别对应的亚麻籽油盒数
		String hs = "10";
		String hsjbid = "4";
		if (jb.equals("2")) {
			hsjbid = "5";
		} else if (jb.equals("3")) {
			hsjbid = "6";
		}
		Xtcl dlhs = clService.getClsValue("dyjf_type", hsjbid);
		if (dlhs != null && !dlhs.getBz_value().equals("")) {
			hs = dlhs.getBz_value();
		}
		mm.put("hs", hs);
		return mm;
	}


	
	@RequestMapping(value = "/YQYB/DlrAliNotify", method = RequestMethod.POST)
	public String DlrAliNotify(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("收到支付宝异步通知！");
//		Map<String, String> params = new HashMap<String, String>();
//
//		// 取出所有参数是为了验证签名
//		Enumeration<String> parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//			String parameterName = parameterNames.nextElement();
//			params.put(parameterName, request.getParameter(parameterName));
//		}
//		boolean signVerified;
//		try {
//			signVerified = AlipaySignature.rsaCheckV1(params,
//					Configs.getAlipayPublicKey(), "UTF-8");
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//			return "failed";
//		}
//		if (signVerified) {
//			String outtradeno = params.get("out_trade_no");
//			log.info(outtradeno + "号订单回调通知。");
//			// System.out.println("验证签名成功！");
//			log.info("验证签名成功！");
//
//			// 若参数中的appid和填入的appid不相同，则为异常通知
//			if (!Configs.getAppid().equals(params.get("app_id"))) {
//				log.warn("与付款时的appid不同，此为异常通知，应忽略！");
//				return "failed";
//			}
//			// ////自我业务处理
//			String status = params.get("trade_status");
//			int isSucc = 0;
//			if (status.equals("WAIT_BUYER_PAY")) { // 如果状态是正在等待用户付款
//				isSucc = updateOrderAndUser(outtradeno);
//			} else {
//				log.warn("付款状态异常，非代付款，此为异常通知，应忽略！");
//				return "failed";
//
//			}
//			if (isSucc == 1) {
//				log.info(outtradeno + "订单的状态已经修改为" + status);
//				return "success";
//			}
//
//		} else { // 如果验证签名没有通过
//			return "failed";
//		}
//		return "failed";
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params=  request.getParameterMap();
		Map<String, String> m = new HashMap<String, String>();
		String signVerified="";	
		m = aliAppService.checkResponseParams(params);
		signVerified = m.get("result");
		if (signVerified.equals("SUCCESS")) {
			String outtradeno = m.get("tradeNo");
			// ////自我业务处理
			int isSucc = 0;
			isSucc = updateOrderAndUser(outtradeno);
			if (isSucc == 1) {
				log.info(outtradeno + "订单的状态已经修改");
				return "success";
			}

		} else { // 如果验证签名没有通过
			return "failed";
		}
		return "failed";
	}

	@RequestMapping(value = "/YQYB/YmzAliNotify", method = RequestMethod.POST)
	public String YmzAliNotify(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("收到支付宝异步通知！");
		Map<String, String[]> params = new HashMap<String, String[]>();
		params=  request.getParameterMap();
		Map<String, String> m = new HashMap<String, String>();
		String signVerified="";	
		m = aliAppService.checkResponseParams(params);
		signVerified = m.get("result");
		if (signVerified.equals("SUCCESS")) {
			String outtradeno = m.get("tradeNo");
			// ////自我业务处理
			int isSucc = 0;
			isSucc = updateOrder(outtradeno);			
			if (isSucc == 1) {
				log.info(outtradeno + "订单的状态已经修改");
				return "success";
			}

		} else { // 如果验证签名没有通过
			return "failed";
		}
		return "failed";
	}

	// 简单打印应答
	private void dumpResponse(AlipayResponse response) {
		if (response != null) {
			log.info(String.format("code:%s, msg:%s", response.getCode(),
					response.getMsg()));
			if (StringUtils.isNotEmpty(response.getSubCode())) {
				log.info(String.format("subCode:%s, subMsg:%s",
						response.getSubCode(), response.getSubMsg()));
			}
			log.info("body:" + response.getBody());
		}
	}

	// /////////////////成为代理人购买/////////////////////

	/**
	 * 代理人支付成功状态修改
	 */
	public int updateOrderAndUser(String orderNum) {
		int count = 0;
		int count1 = 0;
		// 查询订单存在
		List<OrdersB> obList = ordersBService.getOrdersBy("-1", orderNum, "");
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
							"1", "", "");
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
	public int updateOrder(String orderNum) {
		int count = 0;
		// 查询订单存在
		List<OrdersB> obList = ordersBService.getOrdersBy("-1", orderNum, "");
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
				// //2=一级代理人 3=二级代理人 4=三级代理人5经理人',
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

}
