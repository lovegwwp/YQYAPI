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
import com.jyss.yqy.utils.FirstLetterUtil;

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
	public Map<String, Object> getDlInfo(float money) {
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
		String hsjbid = "1";
		if (jb.equals("1")) {
			hsjbid = "5";
		} else if (jb.equals("2")) {
			hsjbid = "6";
		} else if (jb.equals("3")) {
			hsjbid = "7";
		} else if (jb.equals("4")) {
			hsjbid = "8";
		}
		Xtcl dlhs = clService.getClsValue("dyjf_type", hsjbid);
		if (dlhs != null && !dlhs.getBz_value().equals("")) {
			hs = dlhs.getBz_value();
		}
		mm.put("hs", hs);
		return mm;
	}

	@RequestMapping(value = "/DlrAliNotify", method = RequestMethod.POST)
	public String DlrAliNotify(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("收到支付宝异步通知！");

		Map<String, String[]> params = new HashMap<String, String[]>();
		params = request.getParameterMap();
		Map<String, String> m = new HashMap<String, String>();
		String signVerified = "";
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

	@RequestMapping(value = "/YmzAliNotify", method = RequestMethod.POST)
	public String YmzAliNotify(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("收到支付宝异步通知！");
		Map<String, String[]> params = new HashMap<String, String[]>();
		params = request.getParameterMap();
		Map<String, String> m = new HashMap<String, String>();
		String signVerified = "";
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
	//@RequestMapping(value = "/test", method = RequestMethod.POST)
	//@ResponseBody
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
				String jb = ordersB.getDljb();
				String isChuangke = "5";// 经理人
				if (jb.equals("1")) {
					isChuangke = "2";// //初级代理人
				} else if (jb.equals("2")) {// //中级代理人
					isChuangke = "3";
				} else if (jb.equals("3")) {
					isChuangke = "4";// //高级代理人
				} else if (jb.equals("4")) {
					isChuangke = "5";// //经理人
				}
				if (count == 1) {
					count = 0;
					count1 = userService.upUserAllStatus("", "", "1",
							isChuangke, "", ordersB.getGmId() + "");
				}
				if (count1 == 1) {
					// 查询积分返还==2018.2.1==之前返还积分【推荐别人成为代理人开始增加一条返还记录表，一周后开始返还积分】
					// /===2018.2.1==之后===模式不变===可以返积分多次==例；初级的积分在返还，升级为中级了，则返还中级的，返完之后，返初级的。
					// //当前成为代理人的推荐人uuid
					List<UUserRRecordB> rbList = recordBService.getRecordB(
							ordersB.getGmId(), "", "");
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
					// /创客等级 ==2=一级代理人 3=二级代理人 4=三级代理人5经理人',
					int dlType = uList.get(0).getIsChuangke();
					// //判断是否有返还记录
					List<ScoreBack> sbaList = sBackService.getBackScore(puuid,
							"1", "0", "", "");
					// /没有1=第一次返还记录==就增加，2=u_ser的total_pv增加额度
					if (sbaList == null || sbaList.size() == 0) {
						int count3 = addScoreBack(dlType, puuid);
						return count3;
					}
				}
				return count1;
			}
		}
		return count;
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
	 * 购买支付成功状态修改
	 */
	public int updateOrder(String orderNum) {
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
				// //2=一级代理人 3=二级代理人 4=三级代理人5经理人',
				int dlrLevel = ubList.get(0).getIsChuangke();
				int level = dlrLevel;
				// /最高等级时不进行相应变化
				// if (dlrLevel != 4) {
				// /查找等级对应亚麻籽油数量，进行比对升级 4,5,6对应初中高级盒数
				int compareLevel = dlrLevel + 3;
				// /查找往上一等级对应盒数
				Xtcl dlHs = clService.getClsValue("dyjf_type", compareLevel
						+ "");
				int compareNum = Integer.parseInt(dlHs.getBz_value());
				if (gmNum >= compareNum) {
					// //超过数量的购买。直接升级
					if (dlrLevel == 5) {
						dlrLevel = 2;// //经理人升级为初级代理人
					}else if(dlrLevel!= 4){
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
								count = ordersBService.upOrderDljb(dljb,"1",orderNum);
							}
						}
					} else {
						//修改订单代理等级
						if (!dljb.equals("0")){
							count = ordersBService.upOrderDljb(dljb,"1",orderNum);
						}
					}
					if (count == 1) {
						count = 0;

						// /代理人升级，1=包括返还记录的增加 以及2=低等级返还记录的状态-1数据封存3=用户
						// total_pv的额度增加
						// //判断是否有返还记录
						String uuuid = ubList.get(0).getUuid();
						List<ScoreBack> sbaList = sBackService.getBackScore(
								uuuid, "1", "0", "", "");
						int ccc = 0;
						int count3 = 0;
						// //有返还记录，进行封存，再增加
						if (sbaList != null && sbaList.size() > 0) {
							for (ScoreBack scoreBack : sbaList) {
								if (scoreBack != null
										&& scoreBack.getUuuid() != null
										&& !(scoreBack.getUuuid().equals(""))) {
									count++;
									ccc += sBackService.upBackStatus(
											scoreBack.getUuuid(), "-1", "1");
								}
							}
							if (count == sbaList.size()) {
								count3 = addScoreBack(dlrLevel, uuuid);
								return count3;
							}

						} else {
							count3 = addScoreBack(dlrLevel, uuuid);
							return count3;
						}
					}
				}
				// }
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

}
