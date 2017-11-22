package com.jyss.yqy.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.jyss.yqy.entity.Cwzf;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.XtclService;

@Controller
public class AlipayAction {

	@Autowired
	// private CwzfService cwService;
	private static Log log = LogFactory.getLog(AlipayAction.class);

	@Autowired
	private OrdersBService obService;
	@Autowired
	private XtclService clService;
	@Autowired
	private UserService userService;

	// 支付宝当面付2.0服务
	private static AlipayTradeService tradeService;

	// 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	private static AlipayTradeService tradeWithHBService;

	// 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
	private static AlipayMonitorService monitorService;

	static {
		/**
		 * 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
		 * Configs会读取classpath下的zfbinfo
		 * .properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
		 */
		Configs.init("zfbinfo.properties");

		/**
		 * 使用Configs提供的默认参数 AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
		 */
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

		// 支付宝当面付2.0服务（集成了交易保障接口逻辑）
		tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder()
				.build();

		/**
		 * 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数
		 * 否则使用代码中的默认设置
		 */
		monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
				.setGatewayUrl("http://mcloudmonitor.com/gateway.do")
				.setCharset("GBK").setFormat("json").build();
	}

	// 预下单 --type 1=初级，2=中级，3=高级代理人',
	@RequestMapping(value = "/b/alipay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> alipay(@RequestParam int money,
			@RequestParam int gmID) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
		String subject = "代理人消费";

		// (必填) 订单总金额，单位为元，不能超过1亿元
		// 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
		String totalAmount = money + "";

		// (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
		// 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
		String undiscountableAmount = "0";

		// 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
		// 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
		String sellerId = "";

		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = "易起云商品消费：" + money + "元";

		// 商户操作员编号，添加此参数可以为商户操作员做销售统计
		String operatorId = "test_operator_id";

		// (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
		String storeId = "2088102172143232";// test_store_id"=2088802858369537

		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		ExtendParams extendParams = new ExtendParams();
		extendParams.setSysServiceProviderId("2088100200300400500");

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfCode", "");
		// //// 验证当前用户是否合法///////code='-1=其他，0=无支付密码，1=有支付密码，'////
		List<UserBean> ublist = userService.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-1");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);
		if (ub.getPwd() == null || ub.getPwd().equals("")) {
			zfCode = "0";
		} else {
			zfCode = "1";
		}
		mm.put("zfCode", zfCode);
		mm.put("xjjf", ub.getCashScore() + "");
		// //// 商品明细列表，需填写购买商品详细信息，进行创建相应订单///////////
		String hs = "10";
		String jb = "1";
		dlReMap = getDlInfo(money);
		if (!dlReMap.isEmpty()) {
			hs = (String) dlReMap.get("hs");
			jb = (String) dlReMap.get("jb");
		}
		// //所购买的商品信息==亚麻籽油
		List<Goods> gList = new ArrayList<Goods>();
		gList = obService.getGoods("4");
		if (gList == null || gList.size() == 0) {
			m.put("status", "false");
			m.put("message", "该商品信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		Goods good = gList.get(0);

		// 创建扫码支付请求builder，设置请求参数
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
				.setSubject(subject)
				.setTotalAmount(totalAmount)
				.setOutTradeNo(outTradeNo)
				.setUndiscountableAmount(undiscountableAmount)
				.setSellerId(sellerId)
				.setBody(body)
				.setOperatorId(operatorId)
				.setStoreId(storeId)
				.setExtendParams(extendParams)
				.setTimeoutExpress(timeoutExpress)
				.setNotifyUrl(
						"http://192.168.0.26:8080/YQYAPI/orderNotify.action");// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
		// .setGoodsDetailList(goodsDetailList);

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		switch (result.getTradeStatus()) {
		case SUCCESS:
			log.info("支付宝预下单成功: )");

			AlipayTradePrecreateResponse response = result.getResponse();
			dumpResponse(response);

			// 需要修改为运行机器上的路径
			// String filePath =
			// String.format("/Users/sudo/Desktop/qr-%s.png",response.getOutTradeNo());
			// log.info("filePath:" + filePath);
			// ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

			// 自我业务处理
			// 生成订单，插入数据库
			OrdersB ob = new OrdersB(outTradeNo, gmID + "", ub.getRealName(),
					ub.getAccount(), good.getName(), good.getPics(), hs, "盒",
					"-1", "1", good.getPrice(), jb);
			int count = 0;
			count = obService.addOrder(ob);
			if (count == 1) {
				m.put("status", "true");
				// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
				m.put("message", "提交订单成功！");
				m.put("outtradeno", outTradeNo);
				m.put("money", money + "");
				return m;
			}

		case FAILED:
			log.error("支付宝预下单失败!!!");
			m.put("status", "false");
			m.put("message", "下单失败！");
			m.put("outtradeno", "");
			break;

		case UNKNOWN:
			log.error("系统异常，预下单状态未知!!!");
			m.put("status", "false");
			m.put("message", "系统异常，下单失败！");
			m.put("outtradeno", "");
			break;

		default:
			log.error("不支持的交易状态，交易返回异常!!!");
			m.put("status", "false");
			m.put("message", "不支持的交易状态，交易返回异常!");
			m.put("outtradeno", "");
			break;
		}

		return m;
	}

	// 预下单 --type 1=初级，2=中级，3=高级代理人',
	@RequestMapping(value = "/b/alipay2", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> alipay2(@RequestParam int money,
			@RequestParam int gmID) {
		Map<String, String> m = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
		String subject = "代理人消费";

		// (必填) 订单总金额，单位为元，不能超过1亿元
		// 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
		String totalAmount = money + "";

		// (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
		// 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
		String undiscountableAmount = "0";

		// 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
		// 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
		String sellerId = "";

		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = "易起云商品消费：" + money + "元";

		// 商户操作员编号，添加此参数可以为商户操作员做销售统计+
		String operatorId = "test_operator_id";

		// (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
		String storeId = "2088102172143232";// test_store_id"=2088802858369537

		// 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
		// ExtendParams extendParams = new ExtendParams();
		// extendParams.setSysServiceProviderId("2088100200300400500");

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userService.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("outtradeno", "");
			m.put("money", "");
			return m;
		}
		UserBean ub = ublist.get(0);
		// //// 商品明细列表，需填写购买商品详细信息，进行创建相应订单///////////
		String hs = "10";
		String jb = "1";
		dlReMap = getDlInfo(money);
		if (!dlReMap.isEmpty()) {
			hs = (String) dlReMap.get("hs");
			jb = (String) dlReMap.get("jb");
		}
		// //所购买的商品信息==亚麻籽油
		List<Goods> gList = new ArrayList<Goods>();
		gList = obService.getGoods("4");
		if (gList == null || gList.size() == 0) {
			m.put("status", "false");
			m.put("message", "该商品信息错误！");
			m.put("outtradeno", "");
			m.put("money", "");
			return m;
		}
		Goods good = gList.get(0);

		// 创建扫码支付请求builder，设置请求参数
		// AlipayTradePrecreateRequestBuilder builder = new
		// AlipayTradePrecreateRequestBuilder()
		// .setSubject(subject)
		// .setTotalAmount(totalAmount)
		// .setOutTradeNo(outTradeNo)
		// .setUndiscountableAmount(undiscountableAmount)
		// .setSellerId(sellerId)
		// .setBody(body)
		// .setOperatorId(operatorId)
		// .setStoreId(storeId)
		// .setExtendParams(extendParams)
		// .setTimeoutExpress(timeoutExpress)
		// .setNotifyUrl(
		// "http://192.168.0.26:8080/YQYAPI/orderNotify.action");//
		// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
		// // .setGoodsDetailList(goodsDetailList);
		//
		// AlipayF2FPrecreateResult result =
		// tradeService.tradePrecreate(builder);
		// switch (result.getTradeStatus()) {
		// case SUCCESS:
		// log.info("支付宝预下单成功: )");
		//
		// AlipayTradePrecreateResponse response = result.getResponse();
		// dumpResponse(response);

		// 需要修改为运行机器上的路径
		// String filePath =
		// String.format("/Users/sudo/Desktop/qr-%s.png",response.getOutTradeNo());
		// log.info("filePath:" + filePath);
		// ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

		// 自我业务处理
		// 生成订单，插入数据库
		String gmr = "";

		if (ub.getRealName() == null || ub.getRealName().isEmpty()) {
			gmr = "XXX";
		} else {
			gmr = ub.getRealName();
		}
		OrdersB ob = new OrdersB(outTradeNo, gmID + "", gmr, ub.getAccount(),
				good.getName(), good.getPics(), hs, "盒", "-1", "1",
				good.getPrice(), jb);
		int count = 0;
		count = obService.addOrder(ob);
		if (count == 1) {
			m.put("status", "true");
			// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
			m.put("message", "提交订单成功！");
			m.put("outtradeno", outTradeNo);
			m.put("money", money + "");
			return m;
		}

		// case FAILED:
		// log.error("支付宝预下单失败!!!");
		// m.put("status", "false");
		// m.put("message", "下单失败！");
		// m.put("outtradeno", "");
		// break;
		//
		// case UNKNOWN:
		// log.error("系统异常，预下单状态未知!!!");
		// m.put("status", "false");
		// m.put("message", "系统异常，下单失败！");
		// m.put("outtradeno", "");
		// break;
		//
		// default:
		// log.error("不支持的交易状态，交易返回异常!!!");
		// m.put("status", "false");
		// m.put("message", "不支持的交易状态，交易返回异常!");
		// m.put("outtradeno", "");
		// break;
		// }

		return m;
	}

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

	@RequestMapping(value = "/orderNotify", method = RequestMethod.POST)
	public String notifyResult(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("收到支付宝异步通知！");
		Map<String, String> params = new HashMap<String, String>();

		// 取出所有参数是为了验证签名
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			params.put(parameterName, request.getParameter(parameterName));
		}
		boolean signVerified;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params,
					Configs.getAlipayPublicKey(), "UTF-8");
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failed";
		}
		if (signVerified) {
			String outtradeno = params.get("out_trade_no");
			log.info(outtradeno + "号订单回调通知。");
			// System.out.println("验证签名成功！");
			log.info("验证签名成功！");

			// 若参数中的appid和填入的appid不相同，则为异常通知
			if (!Configs.getAppid().equals(params.get("app_id"))) {
				log.warn("与付款时的appid不同，此为异常通知，应忽略！");
				return "failed";
			}

			// 在数据库中查找订单号对应的订单，并将其金额与数据库中的金额对比，若对不上，也为异常通知
			Cwzf cw = null;
			// cw = cwService.getCwByNo(outtradeno).get(0);
			if (cw == null) {
				log.warn(outtradeno + "查无此订单！");
				return "failed";
			}
			if (cw.getCzMoney() != Double.parseDouble(params
					.get("total_amount"))) {
				log.warn("与付款时的金额不同，此为异常通知，应忽略！");
				return "failed";
			}
			// 0 未知状态 1预下单状态 2支付成功 3交易超时 4交易失败 5等待付款 ',
			if (cw.getStatus() == 2)
				return "success"; // 如果订单已经支付成功了，就直接忽略这次通知

			String status = params.get("trade_status");
			Cwzf cwzf = null;
			cwzf.setMacOrderId(outtradeno);
			int isSucc = 0;
			if (status.equals("WAIT_BUYER_PAY")) { // 如果状态是正在等待用户付款
				if (cw.getStatus() != 5)
					cwzf.setStatus(5);
			} else if (status.equals("TRADE_CLOSED")) { // 如果状态是未付款交易超时关闭，或支付完成后全额退款
				if (cw.getStatus() != 3)
					cwzf.setStatus(3);
			} else if (status.equals("TRADE_SUCCESS")
					|| status.equals("TRADE_FINISHED")) { // 如果状态是已经支付成功
				cwzf.setStatus(2);
			} else {
				cwzf.setStatus(0);
			}
			// 修改订单状态
			// isSucc = cwService.upCw(cwzf);
			if (isSucc == 1) {
				// 具体对应业务
				isSucc = 0;
				int cztYPE = cw.getCzType();
				// 1 基础付费设置 2视频套餐设置 3通话套餐设置',
				if (cztYPE == 3) {
					// isSucc = patService.upTalkTimeByCz(cw.getCzTime(),
					// cw.getAccount());
				} else if (cztYPE == 2) {
					// isSucc = patService.upVedioTimeByCz(cw.getCzTime(),
					// cw.getAccount());
				}
				if (isSucc == 1) {
					log.info(outtradeno + "订单的状态已经修改为" + status);
					return "success";
				}
			}

		} else { // 如果验证签名没有通过
			return "failed";
		}
		return "success";
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

}
