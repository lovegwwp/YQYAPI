package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.mapper.*;
import com.jyss.yqy.utils.PasswordUtil;
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
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
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
	private ScoreBalanceMapper sbMapper;
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
		String body = "易起云商品消费 " + money + "元";
		// 支付超时，定义为120分钟
		String timeoutExpress = "30m";
		String notifyUrl ="http://121.40.29.64:8081/YQYAPI/DlrAliNotify.action";
		
		
		/////////////用户本地数据判断////////////////
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("responseBody", "");///支付宝返回
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("zfPwd", "");
		mm.put("zfCode", "");
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付
		//////////////////////
		if (money==0) {		
				m.put("status", "false");
				m.put("message", "代理人消费金额错误！");
				m.put("code", "-4");
				m.put("data", mm);
				return m;
		}
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
		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")) {
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
//        String alipay_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkuJIEE+7+doITMI+/sczhKZbTmpjzDmLIdtoNXpa/IwnBOYPItz+5sOoYjdn9+ka4Dd1Gq4cDLwGbohLHjiSAbvaKRF0zoUpJTe0cVDGOQNjTg7a0OYTbCmO/Ymyea+coWfiSO1oNFWXmPggz/E2+HSxf411b1P/M5X0Qy0/lwJpT65S00cpCBVRwOh3lJ9S+t6Heag292vcVhq7BsdrHMZHhx+SeaIYVbq/GGyCPzfFuImYFCb76r0Ge2eYgZiAq/H79mZJFs9T54axxszlPONq5CDEZPr5cY1Kgt1pOTqCPDrvLWaaedzHRuN/W0uceOp5xx2qfB1Spz1ZicFrMwIDAQAB";
//        String private_key ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCEYVQphrvywUOrbfujlOQFQHBYKCrZAl3mlso3EUhEKzNUBHpxtQVO1mJvS1m1oI50xCFBhF1kmdTGc0PQU0b0F+RgYSgE5Y0sI3GVPNxJOqzLPVlR9t4JRNA883mVCzIbnvEyV8M2pMQnwtVKBybEM+8vyf7tSMm0Ty98/cTJqgh68uCEDU8FBBIvylpp1Ds9R++45uAXcFITm5SmMxrM2ffZ72tQzwVDAUGaLdkkcYTHd1NeXUXJy2QtlFFQkzKNG2rvPpA9ucjy1O8S/sC+7x+fnDaDARobD/3K0zfhi7CfB8vMZXaOpljCz71xREt2rFY6ZFRSaseY/ILZRhZjAgMBAAECggEABTF4RsTFXMmeKPyAkbNGmrojbiHtRGQmYORrfGuOJTZ4pgQi9ZD/a09Xvzv6gfiRfh0vnM1fiJHMWhitgV+y+eJyECHfmwmSVzfwvcUMc0b8/mAiv+dqYHy1mp6Gl4U/6vt6RNpSizM9ir3G9kd/itTqvjozaQWg/BTbLREbhjm57J4tFrYnP8OTKPtv3bmKKyh0KD1T/MovggwboMyEwhn9dHs6achUh4DBbkECsbx7ntMMW/rS5gDQGYplz1UUZ+khm2WCaKJQic1xtymCnUiaNIl7lbt8UeTBVvKMlanlGY1vjQAMk3s3obA3ODyakdVKCEL9LrzElyhD1V/sQQKBgQDG+0eSDt+JUr4SGYUmqYZ0cYwnSuS9n0Pvx0c68FRJ5eU2vhRyim1/Jd7zO0OSzwnJ/x/rulTck7WTaU7S0T/cUKQfIrwNwD1Ysb1uqKpiYrfV2Xuew109vWISTGKaS51Fbcm26RfowRsoziwIk2MQd4KZPjDqQm3dwd4lX/Q6YQKBgQCqUFzF9JtntWCxeBcdh85VtkN36AkVA/SYCz9ffrFbC+wivf2Ip9Z9d7dfTA1UTIaPvJ/5gttqn1yZ3gLYcBob40uhxtKVyvsTvXWLtMl82Y5D/CBhqNS+2tWGwamEkmYHRuVaPwx51e2nJyNEgRicoi08lPFBEOOW53Z8xoMvQwKBgQCoJvuhk44WR2U2eHiMZqCoULiHEARjtm679+TbCvPAC1Z7v4AaF59W3tMdK4z8SJhWKpJ4K9vBF9ZPP6QMBib4cPFxGnJfEIEHLhUOqdxrDk+amZKdZS2rmhqBqil9iL7cSF45g5vf5yijgC+4A42pAcXM6MB/hym/SDEJ1p/WoQKBgQCWCTGRWglFdW13KfoDE85bh7MsAIdvsgpZnx72+182e+xMNt8Q8fpskXhDRXZAmyG3ok2zuumcpkMncYNENI1rn/LS05pUR3qkSzUwG9WcXPONRKEqJ1czwwh0LEsv9OBY7MXDmONeuW0g4cOZ57hM1DnRssxNq5kzKTkidqa+jQKBgQCEZPTHY6WPt/vjVQtq4Y+x6u2YRHD9+m2G8HM9U85Io/Nqq5iXPGf42pR7BADEYm+rkhG0yCOuLMkY75vfLJr2EASdwyfccZ/a/yAukfc1JJFnX4tsz9oGqHZpFcG7vzhumfxIMlguN2hvx1xpJrXOOcJZK2Utb1YYh0gI2RP4Qw==";
//        String appid ="2018011501886037";
//		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appid,private_key,
//				"json","UTF-8",alipay_public_key,"RSA2");
		
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body); // "易起云商品消费 0.1元"
		model.setSubject(subject); // "代理人消费"
		model.setOutTradeNo(outTradeNo); // outtradeno,应用系统内的订单编号
		model.setTimeoutExpress(timeoutExpress); // "30m"
		model.setTotalAmount(totalAmount); // "0.1"
		model.setProductCode("QUICK_MSECURITY_PAY"); // "QUICK_MSECURITY_PAY"
		//model.setSellerId(config.getSELLER_ID());////不需要
		request.setBizModel(model);
		//request.setBizContent(bizContent);
		request.setNotifyUrl(notifyUrl); // "http://121.40.29.64:8081/YQYAPI/YQYB/DlrAliNotify.action"
	
//		
//		model.setBody("111");
//		model.setSubject("2222");
//		model.setOutTradeNo("20171110191203");
//		model.setTimeoutExpress("90m");
//		model.setTotalAmount("1.00");
//		model.setProductCode("QUICK_MSECURITY_PAY");  
//		request.setBizModel(model);
//		request.setNotifyUrl("http://192.168.1.18:89/Student/Notify_Action");//商户外网可以访问的异步地址，不能重定向
		
		///////////////////
		
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
					//mm.put("responseBody","alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018011501886037&biz_content=%7B%22body%22%3A%22111%22%2C%22out_trade_no%22%3A%2220171110191203%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%222222%22%2C%22timeout_express%22%3A%2290m%22%2C%22total_amount%22%3A%221.00%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F192.168.1.18%3A89%2FStudent%2FNotify_Action&sign=NT9t3fhVFGNklaSusETGwop3ugz0Z8yfnzu%2BTQGED1qzwAW0Uxpe25I2n7chcbU%2FvFUIaRBF%2BONrv96FCGwJ72MWBMFirc4MgrElLKd82aIHQ9ejmWQjJ3wmGLxC2xpXP8%2Byh55IHycxa7%2F6AT2tPWPQp9EXL6XPc0iCqqn8WtQEuo4cJ92ej%2BiAt2%2FxAAgObSM%2BW0F6yg9GL79Ck%2Fm44%2BT9ZJQdD4AHbpssQCB4m05j5SLtMUY5gaNmwntVaI2MG6hPK7i9QaGB%2FtQGrpnSnNt0zD0j%2F2qjjwl48zjDSe674dXJrKYLqsLa9FvZSfTfL%2BAyQXfwPKMz0AOaWVGg1g%3D%3D&sign_type=RSA2&timestamp=2018-02-04+14%3A58%3A56&version=1.0");
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
		Xtcl dlhs = clMapper.getClsValue("dyjf_type", hsjbid);
		if (dlhs != null && !dlhs.getBz_value().equals("")) {
			hs = dlhs.getBz_value();
		}
		mm.put("hs", hs);
		return mm;
	}

	// 获取代理参数
	public Map<String, Object> getHhrchuangke(float money) {
		Map<String, Object> mm = new HashMap<String, Object>();
		Integer jb = 0;
		Integer hs = 1;
		Float bs = 3F;
		List<Xtcl> fylist = new ArrayList<Xtcl>();
		// //查询充值人对应等级|

		fylist = clMapper.getClsBy("cjhhr_type", null);
		if (fylist != null && fylist.size() > 0) {
			for (Xtcl xtcl : fylist) {
				String bz_value = xtcl.getBz_value();
				float value = Float.parseFloat(bz_value);
				if(money == value){
					jb = Integer.parseInt(xtcl.getBz_id());
					break;
				}
			}
		}
		if(jb>4){
			jb=4;
		}
		mm.put("jb",jb);
		Xtcl dlbs = clMapper.getClsValue("fhqbs_type", (jb-1)+"");
		if (dlbs != null && !dlbs.getBz_value().equals("")) {
			bs = Float.parseFloat(dlbs.getBz_value());
		}
		mm.put("bs", bs);
		Xtcl dlhs = clMapper.getClsValue("cjhhr_type", (jb+3)+"");
		if (dlhs != null && !dlhs.getBz_value().equals("")) {
			hs = Integer.parseInt(dlhs.getBz_value());
		}
		mm.put("hs", hs);
		return mm;
	}

	// 获取代理参数
	public Map<String, Object> getHhrchuangke2(Integer jbnow,float money) {
		Map<String, Object> mm = new HashMap<String, Object>();
		Integer jb = 0;
		Float bs = 3F;
		float mmobney4 = 0 ;
		float mmobney2 = 0 ;
		float mmobney3 = 0 ;
		List<Xtcl> fylist = new ArrayList<Xtcl>();
		//查询目前等级下一级别对应金钱
		jb = jbnow;
		Xtcl dlm3 = clMapper.getClsValue("cjhhr_type", "3");
		if (dlm3 != null && !dlm3.getBz_value().equals("")) {
			mmobney3 = Float.parseFloat(dlm3.getBz_value());
		}
		Xtcl dlm4 = clMapper.getClsValue("cjhhr_type", "4");
		if (dlm4 != null && !dlm4.getBz_value().equals("")) {
			mmobney4 = Float.parseFloat(dlm4.getBz_value());
		}
		if(money >= mmobney4){
			jb=4;
		}else if(money >= mmobney3){
			jb=3;
		}
		Xtcl dlbs = clMapper.getClsValue("fhqbs_type", (jb-1)+"");
		if (dlbs != null && !dlbs.getBz_value().equals("")) {
			bs = Float.parseFloat(dlbs.getBz_value());
		}
		mm.put("bs", bs);
		mm.put("jb",jb);
		return mm;
	}

	@Override
	public Map<String, Object> getHhrOrderString(
			@RequestParam("filePath") String filePath,
			@RequestParam("gmID") int gmID, @RequestParam("hhrmoney") float hhrmoney,
			@RequestParam("spID") int spID,@RequestParam("payPwd") String payPwd) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("responseBody", "");
		mm.put("money", "");
		mm.put("bdscore", "");///报单券
		mm.put("elecscore", "");//电子券
		mm.put("zfCode", zfCode);
		mm.put("zfPwd", "");
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "1");// 判断是否初次购买=type=[1=初始合伙人购买。2=之后复销]

		Goods goods = null;
		goods = obMapper.getGoodsByid(spID + "");
		if (goods == null) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}
		///判断初始购买用户金额是否错误
		float fhqbs = 3;
		Integer hs = 1;
		float totalPv = 0;
		Integer isChunke = null;
			Map<String, Object> mre = getHhrchuangke(hhrmoney);
			int jb = (Integer) mre.get("jb");
			if (jb == 0) {
				m.put("status", "false");
				m.put("message", "合伙人信息错误！");
				m.put("code", "-3");
				m.put("data", mm);
				return m;
			}
		    hs = (Integer) mre.get("hs");
			fhqbs = (float) mre.get("bs");
			totalPv = fhqbs *hhrmoney;
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "1");
		if (ublist == null || ublist.size() == 0) {
			m.put("status", "false");
			m.put("message", "您还没提交身份信息！");
			m.put("code", "-2");
			m.put("data", mm);
			return m;
		}
		UserBean ub = ublist.get(0);

		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")
				|| ub.getPayPwd().equals("0")) {
			zfCode = "0";
			m.put("status", "false");
			m.put("message", "支付密码错误！");
			m.put("code", "-6");
			m.put("data", mm);
			return m;
		} else {
			zfCode = "1";
			if (!(PasswordUtil.generatePayPwd(payPwd).equals(ub.getPayPwd()))) {
				m.put("status", "false");
				m.put("message", "支付密码错误！");
				m.put("code", "-6");
				m.put("data", mm);
				return m;
			}
		}
		if(ub.getbIsPay() == 1){
			m.put("status", "false");
			m.put("message", "不可重复购买套餐！");
			m.put("code", "-7");
			m.put("data", mm);
			return m;
		}
		float bdMoney =ub.getBdScore();
		float elecMoney =ub.getElectScore();
		mm.put("zfCode", zfCode);
		mm.put("bdscore", bdMoney+ "");
		mm.put("elecscore", elecMoney+ "");
		///////////订单业务///////////////////////////
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		float money = hhrmoney;
		float useBdMoney = hhrmoney;////最后使用的报单券数额
		String gmr = "初始购买";
		if(useBdMoney>bdMoney){
			m.put("status", "false");
			m.put("message", "报单券余额不足！！");
			m.put("code", "-1");
			m.put("data", mm);
			return m;
		}
		// //商品二维码
		String outPutPath = filePath + outTradeNo + ".png";
		String code = "orderCodePng/" + outTradeNo + ".png";
		mm.put("zxingpng", Constant.httpUrl + code);// 订单二维码
		ZxingCodeUtil.zxingCodeCreate(outTradeNo, outPutPath, "2");// /2=代表B端订单
		/////进行商品购买，扣除相应金额，增加订单记录，以及报单券或者电子券记录///////////
		int count = 0;
		////扣用户积分,分红权额度增加（预判断50W额度），并进行判断是否升级
		float totamout = 0;
		////首次消费额(的2倍)
		Xtcl xfe = clMapper.getClsValue("gxjbl_type", "2");
		String bs = "2";
		if (xfe != null && xfe.getBz_value() != null) {
			bs = xfe.getBz_value();
		}
		float b = Float.parseFloat(bs);
		totamout = hhrmoney*b;
		count =	userMapper.upUserMoneyByUUidOrId(null,gmID+"",totalPv,null,null,null,-useBdMoney,totamout,null,jb,1);
		if(count==1){
			    count =0;
				///报单券消费记录\
				ScoreBalance scoreB = new ScoreBalance();
				scoreB.setType(2);//1=收入 2=支出\
				scoreB.setEnd(2);//1=A端 2=B端
				scoreB.setCategory(8);//8=B端消费，
				scoreB.setuUuid(ub.getUuid());
				scoreB.setStatus(1);
				scoreB.setZzCode("YQYB");
				scoreB.setScore(useBdMoney);//使用
				scoreB.setJyScore(bdMoney-useBdMoney);//结余
			    count = sbMapper.addEntryScore(scoreB);
		}
		mm.put("bdscore", (bdMoney-useBdMoney)+ "");
		mm.put("elecscore", (elecMoney)+ "");
		//生成最终订单
		if(count==1){
			count =0;
			OrdersB orderb = new OrdersB(outTradeNo, gmID + "", gmr,
					ub.getAccount(), goods.getName(), goods.getPics(), hs
					+ "", "套餐", "1", "1", goods.getPrice(), money, 0,
					jb+"", code, "zfId", 3);
			count = obMapper.addOrder(orderb);
		}
		if (count == 1) {
			m.put("status", "true");
			// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
			m.put("message", "提交订单成功！");
			mm.put("outtradeno", outTradeNo);
			mm.put("money", money + "");
			mm.put("responseBody", "");
			m.put("code", "0");
			m.put("data", mm);
		}
		m.put("data", mm);
		return m;

	}

	//userElec/1=使用电子抵扣
	@Override
	public Map<String, Object> getGoodOrderString(
			@RequestParam("filePath") String filePath,@RequestParam("userElec") int userElec,
			@RequestParam("gmID") int gmID, @RequestParam("gmNum") int gmNum,
			@RequestParam("spID") int spID,@RequestParam("payPwd") String payPwd) {
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, String> mm = new HashMap<String, String>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("responseBody", "");
		mm.put("money", "");
		mm.put("bdscore", "");///报单券
		mm.put("elecscore", "");//电子券
		mm.put("zfCode", zfCode);
		mm.put("zfPwd", "");
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "1");// 判断是否初次购买=type=[1=初始合伙人购买。2=之后复销]

		Goods goods = null;
		goods = obMapper.getGoodsByid(spID + "");
		if (goods == null) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}

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

		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")
				|| ub.getPayPwd().equals("0")) {
			zfCode = "0";
			m.put("status", "false");
			m.put("message", "支付密码错误！");
			m.put("code", "-6");
			m.put("data", mm);
			return m;
		} else {
			zfCode = "1";
			if (!(PasswordUtil.generatePayPwd(payPwd).equals(ub.getPayPwd()))) {
				m.put("status", "false");
				m.put("message", "支付密码错误！");
				m.put("code", "-6");
				m.put("data", mm);
				return m;
			}
		}
		float bdMoney =ub.getBdScore();
		float elecMoney =ub.getElectScore();
		mm.put("zfCode", zfCode);
		mm.put("bdscore", bdMoney+ "");
		mm.put("elecscore", elecMoney+ "");
        ///////////订单业务///////////////////////////
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		float price = 0;
		float money = 0;
		float useElecMoney = 0;////最后使用的电子券数额
		float useBdMoney = 0;////最后使用的报单券数额
		try {
			//////判断金额购买比例//====如果是复销的话type==2，判断金额是否符合平台规范。电子券最多商品价格的80%（动态值）//
				price = goods.getPrice();
				money = (float) (gmNum * price);
				useBdMoney = money;
				////userElec/1=使用电子抵扣===相应要有电子券抵扣记录
				if(userElec==1) {
					if(elecMoney > 0){
						Xtcl dlhs = clMapper.getClsValue("dzqbl_type", "1");
						String pencent = "0.8";
						if (dlhs != null && dlhs.getBz_value() != null) {
							pencent = dlhs.getBz_value();
						}
						float p = Float.parseFloat(pencent);
						float elecMoneyMax = p * money;////电子券最多可抵扣此商品这些钱
						if(elecMoney > elecMoneyMax){
							useElecMoney = elecMoneyMax;
						}else{
							useElecMoney = elecMoney;
						}
						useBdMoney = money - useElecMoney;////报单券最后使用金额
					}else{
						userElec =0;///无法使用电子券
					}
				}
				if(useBdMoney > bdMoney){
					m.put("status", "false");
					m.put("message", "报单券余额不足！！");
					m.put("code", "-5");
					m.put("data", mm);
					return m;
				}

		} catch (Exception e) {
			m.put("status", "false");
			m.put("message", "商品信息错误！");
			m.put("code", "-3");
			m.put("data", mm);
			return m;
		}

		String gmr = "";
		if (ub.getRealName() == null || ub.getRealName().isEmpty()) {
			gmr = "XXX";
		} else {
			gmr = ub.getRealName();
		}
		// //商品二维码
		String outPutPath = filePath + outTradeNo + ".png";
		String code = "orderCodePng/" + outTradeNo + ".png";
		mm.put("zxingpng", Constant.httpUrl + code);// 订单二维码
		ZxingCodeUtil.zxingCodeCreate(outTradeNo, outPutPath, "2");// /2=代表B端订单
		/////进行商品购买，扣除相应金额，增加订单记录，以及报单券或者电子券记录///////////
		int count = 0;
		////扣用户积分,分红权额度增加（预判断50W额度），并进行判断是否升级
		Integer isLevel = null;
		float fhqbs = 3;
		float totalPv = 0;
		Integer isChunke = null;
		isChunke = ub.getIsChuangke();//当前等级
		///////注意级别升级
		Map<String,Object> mre = getHhrchuangke2(isChunke,money);
		isLevel = (Integer)mre.get("jb");
		if(isChunke < isLevel){
			isChunke = isLevel;
		}else{
			isChunke = null;////没升级就不操作；
		}
		fhqbs = (float)mre.get("bs");
		totalPv = fhqbs * money;////此次消费新增分红权，判断是否超过临界值；
		Xtcl pvcl = clMapper.getClsValue("fhqbs_type", "4");
		float edPv =0;
		if (pvcl != null && pvcl.getBz_value() != null) {
			edPv = Float.parseFloat(pvcl.getBz_value());
		}
		if((ub.getTotalPv()+totalPv)>edPv){
			m.put("status", "false");
			m.put("message", "超过今日分红权额度，不可购买！");
			m.put("code", "-7");
			m.put("data", mm);
			return m;
		}
		count = userMapper.upUserMoneyByUUidOrId(null,gmID+"",totalPv,null,
				null,-useElecMoney,-useBdMoney,null,null,isChunke,null);

		///报单券消费记录\
		ScoreBalance scoreB = new ScoreBalance();
		scoreB.setType(2);//1=收入 2=支出
		scoreB.setEnd(2);//1=A端 2=B端
		scoreB.setCategory(8);//8=B端消费
		scoreB.setuUuid(ub.getUuid());
		scoreB.setStatus(1);
		scoreB.setZzCode("YQYB");
		scoreB.setScore(useBdMoney);//使用
		scoreB.setJyScore(bdMoney-useBdMoney);//结余

		if(count == 1){
			count = 0;
			count = sbMapper.addEntryScore(scoreB);
		}
		//电子券消费记录[userElec==1,使用电子券]
		if(userElec == 1){
			if(count==1){
				count = 0;
				scoreB.setScore(useElecMoney);//使用
				scoreB.setJyScore(elecMoney-useElecMoney);//结余
				count = sbMapper.addElecScore(scoreB);
			}
		}
		mm.put("bdscore", (float)(Math.round((bdMoney-useBdMoney)*100))/100+ "");     // (float)(Math.round(a*100))/100
		mm.put("elecscore", (float)(Math.round(((elecMoney-useElecMoney))*100))/100 + "");
		//生成最终订单
		if(count==1){
			count =0;
			OrdersB orderb = new OrdersB(outTradeNo, gmID + "", gmr,
					ub.getAccount(), goods.getName(), goods.getPics(), gmNum
					+ "", "盒", "1", "1", goods.getPrice(), money, 0,
					"0", code, "zfId", 3);
			count = obMapper.addOrder(orderb);
		}
		if (count == 1) {
			m.put("status", "true");
			// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
			m.put("message", "提交订单成功！");
			mm.put("outtradeno", outTradeNo);
			mm.put("money", money + "");
			mm.put("responseBody", "");
			m.put("code", "0");
			m.put("data", mm);
		}
		m.put("data", mm);
		return m;

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
		mm.put("zfPwd", "");
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
		String body = "易起云商品消费 " + money + "元";

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
		if (ub.getPayPwd() == null || ub.getPayPwd().equals("")
				|| ub.getPayPwd().equals("0")) {
			zfCode = "0";
		} else {
			zfCode = "1";
			String  pwd = ub.getPayPwd();
			//mm.put("zfPwd", pwd);
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

		String notifyUrl = "http://121.40.29.64:8081/YQYAPI/YmzAliNotify.action";
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
			////////////自我订单业务/////////
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
