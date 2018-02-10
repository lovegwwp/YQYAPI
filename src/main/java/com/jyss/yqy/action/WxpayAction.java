package com.jyss.yqy.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.jyss.yqy.config.WXPayConfigImpl;
import com.jyss.yqy.entity.Cwzf;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.ScoreBack;
import com.jyss.yqy.entity.UUserRRecordB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.ScoreBalanceService;
import com.jyss.yqy.service.UserRecordBService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.XtclService;

@Controller
public class WxpayAction {
	@Autowired
	private UserService userService;
	@Autowired
	private XtclService clService;
	@Autowired
	private OrdersBService ordersBService;
	@Autowired
	private ScoreBalanceService sBackService;
	@Autowired
	private UserRecordBService recordBService;

	private static Log log = LogFactory.getLog(WxpayAction.class);

	// 预下单--当面付--生成二维码 --type 视频== 2=视频= 3=通话=',
	@RequestMapping(value = "/pat/wxpay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> alipay(@RequestParam int money,
			@RequestParam int timeAccount, @RequestParam int type,
			@RequestParam String macId) {
		Map<String, String> mapRe = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();
		Map<String, String> mapResponse = new HashMap<String, String>();
		String outTradeNo = macId + System.currentTimeMillis() / 1000;
		// 先判断此用户是否存在
		// Patients pat = patService.getMe(macId);
		// if (pat == null) {
		// mapRe.put("status", "false");
		// mapRe.put("result", "此用户不存在");
		// return mapRe;
		// }
		// 支付主题
		String subject = "明斯特-";
		if (type == 2) {
			subject += "视频套餐扫码支付";
		} else if (type == 3) {
			subject += "通话套餐扫码支付";
		}
		// appid
		// data.put("appid", subject);
		// 商户id
		// data.put("mch_id", subject);
		// 提交支付终端IP
		data.put("spbill_create_ip", "121.40.29.64");

		data.put("body", subject);
		data.put("out_trade_no", outTradeNo);
		data.put("device_info", macId);
		data.put("fee_type", "CNY");
		// 单位为分
		data.put("total_fee", money * 100 + "");
		// data.put("total_fee", money + "");

		data.put("notify_url",
				"http://121.40.29.64:8081/SSM/pat/WXnotify.action");
		// 支付方式 app支付
		data.put("trade_type", "APP");
		// 随机数
		// CommTool.getNonceStr(30)
		data.put("nonce_str", WXPayUtil.generateNonceStr());
		System.out.println("动态随机字符" + WXPayUtil.generateNonceStr());
		// 商品ID
		// data.put("product_id", "12");
		// 签名
		String sign = "";
		try {
			sign = WXPayUtil.generateSignature(data,
					new WXPayConfigImpl().getKey());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		data.put("sign", sign);
		// 生成订单
		WXPayConfigImpl config;
		WXPay wxPay;
		try {
			config = new WXPayConfigImpl();
			wxPay = new WXPay(config);
			mapResponse = wxPay.unifiedOrder(data);// 下单
			System.out.println(mapResponse.toString());
			// 下单成功自我业务处理
			String returnCode = mapResponse.get("return_code");// SUCCESS,FAIL
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = mapResponse.get("result_code");
				if ("SUCCESS".equals(resultCode)) {

					Cwzf cwzf = new Cwzf(0, macId, 1, timeAccount, money, type,
							2, 1, null, null, "", "", "付钱账号", "付钱昵称",
							outTradeNo, "", subject,
							mapResponse.get("prepay_id"));
					int count = 0;
					// count = cwService.addCw(cwzf);
					if (count == 1) {
						mapRe.put("status", "true");
						mapRe.put("qrcode", mapResponse.get("code_url")); // 预支付返回给网页端二维码
						mapRe.put("prepayId", mapResponse.get("prepay_id")); // 返回给网页端预支付ID
						cwzf.setAlipayId(mapResponse.get("prepay_id"));
						mapRe.put("outtradeno", outTradeNo);
						return mapRe;
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			mapRe.put("status", "false");
			mapRe.put("message", "插入订单失败！");
			e.printStackTrace();
			return mapRe;
		}
		/*
		 * mapRe.put("status", "false"); mapRe.put("message", "插入订单失败！");
		 */
		mapRe.put("status", "false");
		mapRe.put("message", "初始化订单失败！");
		return mapRe;
	}

	@RequestMapping(value = "/YQYB/DlrWxNotify", method = RequestMethod.POST)
	public String notifyResult(HttpServletRequest request) {
		log.info("收到微信异步通知！");
		BufferedReader reader = null;
		String returnStr = "";
		try {
			// 拿出所有参数
			reader = request.getReader();
			String line = "";
			String outtradeno = "";
			String xmlString = null;
			StringBuffer inputString = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			xmlString = inputString.toString();
			request.getReader().close();

			log.info("==========xmlString,微信回调请求数据：============");
			log.info(xmlString);
			System.out.println("==========xmlString,微信回调请求数据：============");
			log.info("==========xmlString,微信回调请求数据  =============");
			// 验签
			boolean flag = checkResponseParams(xmlString);
			System.out.println("验签参数  ============" + flag);
			if (flag == true) {
				System.out.println("===============开始验签==========");
				Map<String, String> map = WXPayUtil.xmlToMap(xmlString);
				log.info("===========map:==========");
				Set<String> set = map.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String name = it.next();
					String val = map.get(name);
					log.info("key : " + name + ", value : " + val);
				}
				log.info("===========map ==========");
				String returnCode = map.get("return_code");
				String resultCode = map.get("result_code");
				if ("SUCCESS".equals(returnCode)) {
					outtradeno = map.get("out_trade_no");
					log.info("outtradeno: " + outtradeno);
					// 修改订单状态
					int isSucc = 0;
					isSucc = updateOrderAndUser("outtradeno");
					if (isSucc == 1) {
						// return "success";
						returnStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
						System.out.println(returnStr);
						return returnStr;
					} else {
						returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[数据更新失败]]></return_msg></xml>";
						System.out.println(returnStr);
						return returnStr;
					}
				} else {
					returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[数据更新失败]]></return_msg></xml>";
					System.out.println(returnStr);
					return returnStr;
				}

			} else {
				returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名校验失败]]></return_msg></xml>";
				System.out.println(returnStr);
				return returnStr;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[平台错误]]></return_msg></xml>";
		System.out.println(returnStr);
		return returnStr;

	}

	@RequestMapping(value = "/YQYB/YmzWxNotify", method = RequestMethod.POST)
	public String YmzWxNotify(HttpServletRequest request) {
		log.info("收到微信异步通知！");
		BufferedReader reader = null;
		String returnStr = "";
		try {
			// 拿出所有参数
			reader = request.getReader();
			String line = "";
			String outtradeno = "";
			String xmlString = null;
			StringBuffer inputString = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			xmlString = inputString.toString();
			request.getReader().close();

			log.info("==========xmlString,微信回调请求数据：============");
			log.info(xmlString);
			System.out.println("==========xmlString,微信回调请求数据：============");
			log.info("==========xmlString,微信回调请求数据  =============");
			// 验签
			boolean flag = checkResponseParams(xmlString);
			System.out.println("验签参数  ============" + flag);
			if (flag == true) {
				System.out.println("===============开始验签==========");
				Map<String, String> map = WXPayUtil.xmlToMap(xmlString);
				log.info("===========map:==========");
				Set<String> set = map.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String name = it.next();
					String val = map.get(name);
					log.info("key : " + name + ", value : " + val);
				}
				log.info("===========map ==========");
				String returnCode = map.get("return_code");
				String resultCode = map.get("result_code");
				int isSucc = 0;
				if ("SUCCESS".equals(returnCode)) {
					outtradeno = map.get("out_trade_no");
					log.info("outtradeno: " + outtradeno);
					// //个人业务处理
					isSucc = updateOrder("outtradeno");
					if (isSucc == 1) {
						// return "success";
						returnStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
						System.out.println(returnStr);
						return returnStr;
					} else {
						returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[数据更新失败]]></return_msg></xml>";
						System.out.println(returnStr);
						return returnStr;
					}
				} else {
					returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[数据更新失败]]></return_msg></xml>";
					System.out.println(returnStr);
					return returnStr;
				}

			} else {
				returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名校验失败]]></return_msg></xml>";
				System.out.println(returnStr);
				return returnStr;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnStr = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[平台错误]]></return_msg></xml>";
		System.out.println(returnStr);
		return returnStr;

	}

	public boolean checkResponseParams(String xmlStr) {
		try {
			WXPayConfigImpl config = new WXPayConfigImpl();
			WXPay wxPay = new WXPay(config);
			Map<String, String> notifyMap = WXPayUtil.xmlToMap(xmlStr); // 转换成map
			if (wxPay.isPayResultNotifySignatureValid(notifyMap)) {
				// 签名正确
				// 进行处理。
				// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
				return true;
			} else {
				// 签名错误，如果数据里没有sign字段，也认为是签名错误
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// /////////////////成为代理人购买/////////////////////

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
							"1","0","", "");
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

}
