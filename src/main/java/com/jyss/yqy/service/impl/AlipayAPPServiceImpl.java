package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.UMobileLoginMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.AlipayAppService;
import com.jyss.yqy.utils.FirstLetterUtil;
import com.jyss.yqy.utils.ZxingCodeUtil;

@Service
@Transactional
public class AlipayAPPServiceImpl implements AlipayAppService {
	@Autowired
	private OrdersBMapper obMapper;
	@Autowired
	private XtclMapper clMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UMobileLoginMapper uMobileLoginMapper;

	private static Logger log = Logger.getLogger(AlipayServiceImpl.class);

	@Override
	public Map<String, Object> getDLROrderString(@RequestParam("filePath") String filePath, @RequestParam float money,
			@RequestParam int gmID) {
		
		//////////前期业务字段处理///////////////////
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
		// 需保证商户系统端不能重复，建议通过数据库sequence生成，
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);
		String subject = "代理人消费";		
		String totalAmount = money + "";
		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = "易起云商品消费：" + money + "元";
		// 支付超时，定义为120分钟
		String timeoutExpress = "30m";
		String notifyUrl ="http://121.40.29.64:8081/YQYAPI/YQYB/DlrAliNotify.action";
		
		/////////////用户本地数据判断////////////////
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("responseBody", "");///支付宝返回
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfCode", "");
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付
		// //// 验证当前用户是否合法///////code='-1=其他，0=无支付密码，1=有支付密码，'////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "1");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "用户信息错误！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);
		/////不能是代理人，才能购买
		if (ub.getIsChuangke() >= 2) {
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
		
		////////支付组件初始化////////////////////

		AliConfig config = new AliConfig();
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(config.getURL(),
				config.getAPP_ID(), config.getAPP_PRIVATE_KEY(),
				config.getFORMAT(), config.getCHARSET(),
				config.getALIPAY_PUBLIC_KEY(), config.getSIGN_TYPE());

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body); // "我是测试数据"
		model.setSubject(subject); // "App支付测试Java"
		model.setOutTradeNo(outTradeNo); // outtradeno,应用系统内的订单编号
		model.setTimeoutExpress(timeoutExpress); // "30m"
		model.setTotalAmount(totalAmount); // "0.01"
		model.setProductCode("QUICK_MSECURITY_PAY"); // "QUICK_MSECURITY_PAY"
		//model.setSellerId(config.getSELLER_ID());
		request.setBizModel(model);
		//request.setBizContent(bizContent);
		request.setNotifyUrl(notifyUrl); // "商户外网可以访问的异步地址"

		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient
					.sdkExecute(request);			 
			System.out.println(response.getBody());	
			////////////自我订单业务////////
			if (response.isSuccess()) {
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
					count = userMapper.upUserAllStatus("", bCode, "", "", "",
							gmID + "");
				}
				// /////////////////////////
				if (count == 1) {
					m.put("status", "true");
					m.put("message", "提交订单成功！");
					mm.put("outtradeno", outTradeNo);
					mm.put("money", money + "");
					mm.put("responseBody", response.getBody());
					m.put("code", "0");
					m.put("data", mm);
					return m;
				}
				m.put("code", "-5");
				m.put("data", mm);
				return m;
				
			}else{
				m.put("status", "false");
				m.put("message", "下单失败！");
			}
					
		} catch (AlipayApiException e) {
			e.printStackTrace();
			m.put("status", "false");
			m.put("message", "下单失败！");
		}
		return m;
	}

	@Override
	public Map<String,String> checkResponseParams(Map<String, String[]> requestParams) {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> m = new HashMap<String, String>();
		m.put("result", "");
		m.put("tradeNo", "");
		log.info("========Alipay============");

		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			log.info(">>>>>key: " + name + ", value: " + valueStr);
			params.put(name, valueStr);
		}
		log.info("========Alipay============");
		try {
			AliConfig config = new AliConfig();
			boolean flag = AlipaySignature.rsaCheckV1(params,
					config.getALIPAY_PUBLIC_KEY(), config.getCHARSET(),
					config.getSIGN_TYPE());

			String[] tradeNo = requestParams.get("out_trade_no");
			String[] tradeStatus = requestParams.get("trade_status");

			if (flag == true) {// 验证成功
				if (tradeStatus.length == 1) {
					if (tradeStatus[0].equals("TRADE_FINISHED")
							|| tradeStatus[0].equals("TRADE_SUCCESS")) {
						log.info(">>>>>is OK to update");
						// 商品交易成功之后的业务逻辑代码

						log.info(">>>>>tradeNo:" + tradeNo[0]);
						m.put("result", "SUCCESS");
						m.put("tradeNo", tradeNo[0]);
						return m;
					}
					if(tradeStatus[0].equals("TRADE_CLOSED")){
						log.info(">>>>>trade cancel");
						m.put("result", "CLOSED");
						return m;
					}
				}
			} else {// 验证失败
				log.info(">>>>>验签失败" + tradeNo[0]);
				log.info(">>>>>交易被关闭了");
				m.put("result", "fail");
				return m;
			}

		} catch (AlipayApiException e) {
			e.printStackTrace();
			m.put("result", "false");
			return m;
		}
		m.put("result", "false");
		return m;
	}
	
	
	// 获取代理参数
	public Map<String, Object> getDlInfo(float money) {
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
	
	@Override
	public Map<String, Object> getYmzOrderString(
			@RequestParam("filePath") String filePath,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,
			@RequestParam("spID") int spID) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("responseBody", "");
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
	
		// 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
		String body = "易起云商品消费：" + money + "元";

		// 支付超时，定义为120分钟
		String timeoutExpress = "30m";
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
		
		String notifyUrl = "http://121.40.29.64:8081/YQYAPI/YQYB/YmzAliNotify.action";
		////////支付组件初始化////////////////////

		AliConfig config = new AliConfig();
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(config.getURL(),
				config.getAPP_ID(), config.getAPP_PRIVATE_KEY(),
				config.getFORMAT(), config.getCHARSET(),
				config.getALIPAY_PUBLIC_KEY(), config.getSIGN_TYPE());

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body); // "我是测试数据"
		model.setSubject(subject); // "App支付测试Java"
		model.setOutTradeNo(outTradeNo); // outtradeno,应用系统内的订单编号
		model.setTimeoutExpress(timeoutExpress); // "30m"
		model.setTotalAmount(totalAmount); // "0.01"
		model.setProductCode("QUICK_MSECURITY_PAY"); // "QUICK_MSECURITY_PAY"
		//model.setSellerId(config.getSELLER_ID());
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl); // "商户外网可以访问的异步地址"

		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient
					.sdkExecute(request);			 
			System.out.println(response.getBody());	
			////////////自我订单业务////////
			if (response.isSuccess()) {
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
					mm.put("responseBody", response.getBody());
					m.put("code", "0");
					m.put("data", mm);
					return m;
				}
				m.put("code", "-4");
				m.put("data", mm);
				return m;

			}else{
				m.put("status", "false");
				m.put("message", "下单失败！");
			}
					
		} catch (AlipayApiException e) {
			e.printStackTrace();
			m.put("status", "false");
			m.put("message", "下单失败！");
		}
		return m;
	}
}
