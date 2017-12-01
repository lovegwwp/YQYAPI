package com.jyss.yqy.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface WxpayService {

	Map<String, Object> dlrWxpay(String filePath, int money, int gmID);

	public String wxNotifyResult(HttpServletRequest request);

}
