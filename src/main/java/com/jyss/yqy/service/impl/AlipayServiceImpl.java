package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.alipay.api.AlipayResponse;
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
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.UMobileLoginMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.AlipayService;
import com.jyss.yqy.utils.FirstLetterUtil;
import com.jyss.yqy.utils.ZxingCodeUtil;

@Service
@Transactional
public class AlipayServiceImpl implements AlipayService {
	private static Log log = LogFactory.getLog(AlipayServiceImpl.class);

	@Autowired
	private OrdersBMapper obMapper;
	@Autowired
	private XtclMapper clMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UMobileLoginMapper uMobileLoginMapper;

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
				.setGatewayUrl("https://openapi.alipay.com/gateway.do")
				.setCharset("GBK").setFormat("json").build();
	}

	// 预下单 --type 1=初级，2=中级，3=高级代理人',

	public Map<String, Object> addDlrOrder(
			@RequestParam("filePath") String filePath, @RequestParam int money,
			@RequestParam int gmID) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xx门店当面付扫码消费”
		String subject = "代理人消费";
System.out.println(33);
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
		String storeId = "2088921686525534";// test_store_id"=2088921686525534

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
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付
		// //// 验证当前用户是否合法///////code='-1=其他，0=无支付密码，1=有支付密码，'////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);
		if (ub.getIsChuangke() < 2) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
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
			if (jb.equals("0")) {
				m.put("status", "false");
				m.put("message", "代理人消费金额错误！");
				m.put("code", "-4");
				m.put("data", mm);
				return m;
			}
		}
		// //所购买的商品信息==亚麻籽油
				int pv = 0;
				String singlePV = "300";
				// //查找对应PV
				Xtcl dlpv = clMapper.getClsValue("pv_type", "1");
				if (dlpv != null && !dlpv.getBz_value().equals("")) {
					singlePV = dlpv.getBz_value();
				}
				try {
					pv = Integer.parseInt(singlePV) * Integer.parseInt(hs);
				} catch (Exception e) {
					m.put("status", "false");
					m.put("message", "商品信息错误！");
					m.put("code", "-3");
					m.put("data", mm);
					return m;
				}
		List<Goods> gList = new ArrayList<Goods>();
		gList = obMapper.getGoods("4");
		if (gList == null || gList.size() == 0) {
			m.put("status", "false");
			m.put("message", "该商品信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		Goods good = gList.get(0);
System.out.println(2222222);
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
						"http://121.40.29.64:8081/YQYAPI/YQYB/DlrAliNotify.action");// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
		// .setGoodsDetailList(goodsDetailList);
		
        System.out.println(111111111);
		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		System.out.println(result.toString());
		switch (result.getTradeStatus()) {
		case SUCCESS:
			log.info("支付宝预下单成功: )");
			// String tradeNo =result.getResponse().get
			AlipayTradePrecreateResponse response = result.getResponse();
			dumpResponse(response);

			// 需要修改为运行机器上的路径
			// String filePath =
			// String.format("/Users/sudo/Desktop/qr-%s.png",response.getOutTradeNo());
			// log.info("filePath:" + filePath);
			// ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

			// 自我业务处理
			// 生成订单，插入数据库
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
					good.getName(), good.getPics(), hs, "盒", "-1", "1",
					good.getPrice(), money, pv, jb, code, "zfId", 1);
			int count = 0;
			count = obMapper.addOrder(ob);
			// ///user表===isChuangke字段修改/////
			String isChuangke = "2";
			if (jb.equals("1")) {
				isChuangke = "2";// //初级代理人
			}else if (jb.equals("2")) {// //中级代理人
				isChuangke = "3";
			} else if (jb.equals("3")) {
				isChuangke = "4";// //高级代理人
			}else if (jb.equals("4")) {
				isChuangke = "5";// //经理人
			}
			String bCode = "";
			bCode = FirstLetterUtil.getFirstLetter(gmr);
			if (bCode == null || bCode.equals("")) {
				bCode = gmr;
			}
			if (count == 1) {
				count = 0;
				count = userMapper.upUserAllStatus("", bCode, "1", isChuangke, "",
						gmID + "");
			}
			// /////////////////////////
			if (count == 1) {
				m.put("status", "true");
				// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
				m.put("message", "提交订单成功！");
				mm.put("outtradeno", outTradeNo);
				mm.put("money", money + "");
				m.put("code", "0");
				m.put("data", mm);
				return m;
			}
			m.put("code", "-5");
			m.put("data", mm);
			return m;
		case FAILED:
			log.error("支付宝预下单失败!!!");
			m.put("status", "false");
			m.put("message", "下单失败！");
			break;

		case UNKNOWN:
			log.error("系统异常，预下单状态未知!!!");
			m.put("status", "false");
			m.put("message", "系统异常，下单失败！");
			break;

		default:
			log.error("不支持的交易状态，交易返回异常!!!");
			m.put("status", "false");
			m.put("message", "不支持的交易状态，交易返回异常!");
			break;
		}
		m.put("code", "-3");
		m.put("data", mm);
		return m;
	}

	// 预下单 --type 1=初级，2=中级，3=高级代理人',
	public Map<String, Object> addDlrOrder2(
			@RequestParam("filePath") String filePath, @RequestParam int money,
			@RequestParam int gmID) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// (必填) 订单标题，粗略描述用户的支付目的。如“xx品牌xxx门店当面付扫码消费”
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
		String storeId = "2088921686525534";// test_store_id"=2088802858369537

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfCode", "");
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);
		if (ub.getIsChuangke() < 2) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		if (ub.getPwd() == null || ub.getPwd().equals("")
				|| ub.getPwd().equals("0")) {
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
			if (jb.equals("0")) {
				m.put("status", "false");
				m.put("message", "代理人消费金额错误！");
				m.put("code", "-4");
				m.put("data", mm);
				return m;
			}
		}
		// //所购买的商品信息==亚麻籽油
		int pv = 0;
		String singlePV = "300";
		// //查找对应PV
		Xtcl dlpv = clMapper.getClsValue("pv_type", "1");
		if (dlpv != null && !dlpv.getBz_value().equals("")) {
			singlePV = dlpv.getBz_value();
		}
		try {
			pv = Integer.parseInt(singlePV) * Integer.parseInt(hs);
		} catch (Exception e) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}

		List<Goods> gList = new ArrayList<Goods>();
		gList = obMapper.getGoods("4");
		if (gList == null || gList.size() == 0) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}
		Goods good = gList.get(0);
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
				good.getName(), good.getPics(), hs, "盒", "-1", "1",
				good.getPrice(), money, pv, jb, code, "zfId", 1);
		int count = 0;
		count = obMapper.addOrder(ob);
		// ///user表===isChuangke字段修改/////
		String isChuangke = "2";
		if (jb.equals("2")) {// //中级代理人
			isChuangke = "3";
		} else if (jb.equals("3")) {
			isChuangke = "4";// //高级代理人
		}
		String bCode = "";
		bCode = FirstLetterUtil.getFirstLetter(gmr);
		if (bCode == null || bCode.equals("")) {
			bCode = gmr;
		}
		if (count == 1) {
			count = 0;
			count = userMapper.upUserAllStatus("", bCode, "1", isChuangke, "",
					gmID + "");
		}
		// /////////////////////////
		if (count == 1) {
			m.put("status", "true");
			// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
			m.put("message", "提交订单成功！");
			mm.put("outtradeno", outTradeNo);
			mm.put("money", money + "");
			m.put("code", "0");
			m.put("data", mm);
			return m;
		}
		m.put("code", "-5");
		m.put("data", mm);
		return m;
	}

	public Map<String, Object> addYmzOrder2(
			@RequestParam("filePath") String filePath,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,
			@RequestParam("spID") int spID) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfCode", zfCode);
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付

		Goods goods = null;
		goods = obMapper.getGoodsByid(spID + "");
		if (goods == null) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// (必填) 订单标题，粗略描述用户的支付目的。如“xx品牌xxx门店当面付扫码消费”
		String subject = "亚麻籽油消费";
		float price = 0;
		float money = 0;
		try {
			price = goods.getPrice();
			money = (float) (gmNum * price);
		} catch (Exception e) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}
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
		String storeId = "2088921686525534";// test_store_id"=2088921686525534

		// 支付超时，定义为120分钟
		String timeoutExpress = "120m";
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "2");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);
		if (ub.getIsChuangke() < 2) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		if (ub.getPwd() == null || ub.getPwd().equals("")
				|| ub.getPwd().equals("0")) {
			zfCode = "0";
		} else {
			zfCode = "1";
		}
		mm.put("zfCode", zfCode);
		mm.put("xjjf", ub.getCashScore() + "");
		// //// 商品明细列表，需填写购买商品详细信息，进行创建相应订单///////////
		String gmr = "";
		String singlePV = "300";
		// //查找对应PV
		Xtcl dlpv = clMapper.getClsValue("pv_type", "1");
		if (dlpv != null && !dlpv.getBz_value().equals("")) {
			singlePV = dlpv.getBz_value();
		}
		int pv = 0;
		try {
			pv = Integer.parseInt(singlePV) * gmNum;
		} catch (Exception e) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}
		if (ub.getRealName() == null || ub.getRealName().isEmpty()) {
			gmr = "XXX";
		} else {
			gmr = ub.getRealName();
		}
		// 创建扫码支付请求builder，设置请求参数
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
				.setSubject(subject).setTotalAmount(totalAmount)
				.setOutTradeNo(outTradeNo)
				.setUndiscountableAmount(undiscountableAmount)
				.setSellerId(sellerId)
				.setBody(body)
				.setOperatorId(operatorId)
				.setStoreId(storeId)
				// .setExtendParams(extendParams)
				.setTimeoutExpress(timeoutExpress)
				.setNotifyUrl(
						"http://121.40.29.64:8081/YQYAPI/YQYB/YmzAliNotify.action");// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
		// .setGoodsDetailList(goodsDetailList);

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		System.out.println(result.toString());
		switch (result.getTradeStatus()) {
		case SUCCESS:
			log.info("支付宝预下单成功: )");
			AlipayTradePrecreateResponse response = result.getResponse();
			dumpResponse(response);

			// 自我业务处理
			// //商品二维码
			String outPutPath = filePath + outTradeNo + ".png";
			String code = "orderCodePng/" + outTradeNo + ".png";
			mm.put("zxingpng", Constant.httpUrl + code);// 订单二维码
			// /生成二维码
			ZxingCodeUtil.zxingCodeCreate(outTradeNo, outPutPath, "2");// /2=代表B端订单
			OrdersB orderb = new OrdersB(outTradeNo, gmID + "", gmr,
					ub.getAccount(), goods.getName(), goods.getPics(), gmNum
							+ "", "盒", "-1", "1", goods.getPrice(), money, pv,
					"0", code, "zfId", 1);
			int count = 0;
			count = obMapper.addOrder(orderb);
			if (count == 1) {
				m.put("status", "true");
				// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
				m.put("message", "提交订单成功！");
				mm.put("outtradeno", outTradeNo);
				mm.put("money", money + "");
				m.put("code", "0");
				m.put("data", mm);
				return m;
			}
			m.put("code", "-4");
			m.put("data", mm);
			return m;

		case FAILED:
			log.error("支付宝预下单失败!!!");
			m.put("status", "false");
			m.put("message", "下单失败！");
			break;

		case UNKNOWN:
			log.error("系统异常，预下单状态未知!!!");
			m.put("status", "false");
			m.put("message", "系统异常，下单失败！");
			break;

		default:
			log.error("不支持的交易状态，交易返回异常!!!");
			m.put("status", "false");
			m.put("message", "不支持的交易状态，交易返回异常!");
			break;
		}
		m.put("code", "-3");
		m.put("data", mm);
		return m;
	}

	// 获取代理参数
	public Map<String, Object> getDlInfo(int money) {
		Map<String, Object> mm = new HashMap<String, Object>();
		String info = "";
		String jb = "1";
		List<Xtcl> fylist = new ArrayList<Xtcl>();
		// //查询充值人对应等级
		fylist = clMapper.getClsBy("dyf_type", money + "");
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
		Xtcl dlhs = clMapper.getClsValue("dyjf_type", hsjbid);
		if (dlhs != null && !dlhs.getBz_value().equals("")) {
			hs = dlhs.getBz_value();
		}
		mm.put("hs", hs);
		return mm;
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
