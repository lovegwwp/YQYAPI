package com.jyss.yqy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.jyss.yqy.config.WXPayConfigImpl;
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.OrdersBMapper;
import com.jyss.yqy.mapper.UMobileLoginMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.WxpayService;
import com.jyss.yqy.utils.FirstLetterUtil;
import com.jyss.yqy.utils.ZxingCodeUtil;

@Service
@Transactional
public class WxpayServiceImpl implements WxpayService {

	@Autowired
	private OrdersBMapper obMapper;
	@Autowired
	private XtclMapper clMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UMobileLoginMapper uMobileLoginMapper;
	private static Log log = LogFactory.getLog(WxpayServiceImpl.class);

	public Map<String, Object> dlrWxpay(String filePath, float money, int gmID) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		HashMap<String, String> data = new HashMap<String, String>();
		HashMap<String, String> payInfo = new HashMap<String, String>();
		Map<String, Object> mm = new HashMap<String, Object>();
		Map<String, String> mapResponse = new HashMap<String, String>();
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// 支付主题
		String subject = "代理人消费";
		// appid
		// data.put("appid", subject);
		// 商户id
		// data.put("mch_id", subject);
		// 提交支付终端IP
		data.put("spbill_create_ip", "121.40.29.64");

		data.put("body", subject);
		data.put("out_trade_no", outTradeNo);
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		// 单位为分
		data.put("total_fee", money * 100 + "");
		// data.put("total_fee", money + "");

		data.put("notify_url",
				"http://121.40.29.64:8081/YQYAPI/YQYB/DlrWxNotify.action");
		// 支付方式 app支付
		data.put("trade_type", "APP");
		// 随机数
		// CommTool.getNonceStr(30)
		String nonceStr = WXPayUtil.generateNonceStr();
		data.put("nonce_str", nonceStr);
		System.out.println("动态随机字符" + WXPayUtil.generateNonceStr());
		// 商品ID
		// data.put("product_id", "12");
		if (money==0) {		
			mapRe.put("status", "false");
			mapRe.put("message", "代理人消费金额错误！");
			mapRe.put("code", "-4");
			mapRe.put("data", mm);
			return mapRe;
	}
		// //////////////////////////////////////////////
		String zfCode = "-1";// zfCode='-1=其他，0=无支付密码，1=有支付密码，'///
		mm.put("outtradeno", "");
		mm.put("money", "");
		mm.put("xjjf", "");
		mm.put("payInfo", "");
		mm.put("zfCode", "");
		mm.put("zxingpng", "");// 订单二维码
		mm.put("type", "1");// 支付方式：1=支付宝，2=微信，3=现金支付
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "");
		if (ublist == null || ublist.size() == 0) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
		}
		UserBean ub = ublist.get(0);
		if (ub.getIsChuangke() < 2) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
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
				mapRe.put("status", "false");
				mapRe.put("message", "代理人消费金额错误！");
				mapRe.put("code", "-4");
				mapRe.put("data", mm);
				return mapRe;
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
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}

		List<Goods> gList = new ArrayList<Goods>();
		gList = obMapper.getGoods("4");
		if (gList == null || gList.size() == 0) {
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}
		Goods good = gList.get(0);
		// //////////////////////////////////////////////
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
			// ///mapResponse = wxPay.unifiedOrder(data);// 下单
			System.out.println(mapResponse.toString());
			// 下单成功自我业务处理
			// String returnCode = mapResponse.get("return_code");//
			// SUCCESS,FAIL
			// if ("SUCCESS".equals(returnCode)) {
			// String resultCode = mapResponse.get("result_code");
			// String wxpayId = mapResponse.get("prepay_id");
			// if ("SUCCESS".equals(resultCode)) {

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
			OrdersB ob = new OrdersB(outTradeNo, gmID + "", gmr,
					ub.getAccount(), good.getName(), good.getPics(), hs, "盒",
					"-1", "1", good.getPrice(), money, pv, jb, code, "zfId", 2);
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
				count = userMapper.upUserAllStatus("", bCode, "1", isChuangke,
						"", gmID + "");
			}
			// /////////////////////////
			if (count == 1) {
				Date sjc = new Date();
				payInfo.put("timestamp", sjc.getTime() / 1000 + "");
				// data.put("prepay_id", wxpayId);
				payInfo.put("noncestr", nonceStr);
				payInfo.put("prepayid", "");
				payInfo.put("sign", sign);
				mm.put("payInfo", payInfo);
				mm.put("outtradeno", "");
				mm.put("money", money + "");
				mapRe.put("status", "true");
				// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
				mapRe.put("message", "提交订单成功！");
				mapRe.put("code", "0");
				mapRe.put("data", mm);
				return mapRe;
			}
			// }
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			mapRe.put("code", "-5");
			mapRe.put("data", "");
			mapRe.put("status", "false");
			mapRe.put("message", "插入订单失败！");
			e.printStackTrace();
			return mapRe;
		}
		// TODO Auto-generated catch block
		mapRe.put("code", "-5");
		mapRe.put("data", "");
		mapRe.put("status", "false");
		mapRe.put("message", "初始化订单失败！");
		return mapRe;
	}

	public Map<String, Object> ymzWxpay(String filePath, int gmID, int gmNum,
			int spID) {
		Map<String, Object> mapRe = new HashMap<String, Object>();
		Map<String, Object> dlReMap = new HashMap<String, Object>();
		HashMap<String, String> data = new HashMap<String, String>();
		HashMap<String, String> payInfo = new HashMap<String, String>();
		Map<String, Object> mm = new HashMap<String, Object>();
		Map<String, String> mapResponse = new HashMap<String, String>();
		String outTradeNo = System.currentTimeMillis() / 1000 + "O" + gmID
				+ "r" + (long) (Math.random() * 1000L);

		// 支付主题
		String subject = "亚麻籽油消费";
		// appid
		// data.put("appid", subject);
		// 商户id
		// data.put("mch_id", subject);
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
			mapRe.put("status", "false");
			mapRe.put("message", "商品信息错误！");
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
			mapRe.put("message", "商品信息错误！");
			mapRe.put("code", "-3");
			mapRe.put("data", mm);
			return mapRe;
		}

		// 提交支付终端IP
		data.put("spbill_create_ip", "121.40.29.64");

		data.put("body", subject);
		data.put("out_trade_no", outTradeNo);
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		// 单位为分
		data.put("total_fee", money * 100 + "");
		// data.put("total_fee", money + "");
		data.put("notify_url",
				"http://121.40.29.64:8081/YQYAPI/YQYB/YmzWxNotify.action");
		// 支付方式 app支付
		data.put("trade_type", "APP");
		// 随机数
		// CommTool.getNonceStr(30)
		String nonceStr = WXPayUtil.generateNonceStr();
		data.put("nonce_str", nonceStr);
		System.out.println("动态随机字符" + WXPayUtil.generateNonceStr());
		// 商品ID
		// data.put("product_id", "12");
		// //// 验证当前用户是否合法///////////
		List<UserBean> ublist = userMapper.getUserById(gmID + "", "1", "");
		if (ublist == null || ublist.size() == 0) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
		}
		UserBean ub = ublist.get(0);
		if (ub.getIsChuangke() < 2) {
			mapRe.put("status", "false");
			mapRe.put("message", "用户信息错误！");
			mapRe.put("code", "-2");
			mapRe.put("data", mm);
			return mapRe;
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
		String jb = "0";
		// //所购买的商品信息==亚麻籽油
		int pv = 0;
		String singlePV = "300";
		// //查找对应PV
		Xtcl dlpv = clMapper.getClsValue("pv_type", "1");
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

		// //////////////////////////////////////////////
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
			// ///mapResponse = wxPay.unifiedOrder(data);// 下单
			System.out.println(mapResponse.toString());
			// 下单成功自我业务处理
			// String returnCode = mapResponse.get("return_code");//
			// SUCCESS,FAIL
			// if ("SUCCESS".equals(returnCode)) {
			// String resultCode = mapResponse.get("result_code");
			// String wxpayId = mapResponse.get("prepay_id");
			// if ("SUCCESS".equals(resultCode)) {

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
			OrdersB ob = new OrdersB(outTradeNo, gmID + "", gmr,
					ub.getAccount(), goods.getName(), goods.getPics(), gmNum
							+ "", "盒", "-1", "1", goods.getPrice(), money, pv,
					jb, code, "zfId", 2);
			int count = 0;
			count = obMapper.addOrder(ob);
			if (count == 1) {
				Date sjc = new Date();
				payInfo.put("timestamp", sjc.getTime() / 1000 + "");
				// data.put("prepay_id", wxpayId);
				payInfo.put("noncestr", nonceStr);
				payInfo.put("prepayid", "");
				payInfo.put("sign", sign);
				mm.put("payInfo", payInfo);
				mm.put("outtradeno", "");
				mm.put("money", money + "");
				mapRe.put("status", "true");
				// m.put("qrcode", response.getQrCode()); // 返回给客户端二维码
				mapRe.put("message", "提交订单成功！");
				mapRe.put("code", "0");
				mapRe.put("data", mm);
				return mapRe;
			}
			// }
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			mapRe.put("status", "false");
			mapRe.put("message", "插入订单失败！");
			mapRe.put("code", "-4");
			mapRe.put("data", "");
			e.printStackTrace();
			return mapRe;
		}
		mapRe.put("code", "-4");
		mapRe.put("data", "");
		mapRe.put("status", "false");
		mapRe.put("message", "初始化订单失败！");
		return mapRe;
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

}
