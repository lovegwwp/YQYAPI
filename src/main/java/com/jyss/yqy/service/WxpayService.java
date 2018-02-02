package com.jyss.yqy.service;

import java.util.Map;

public interface WxpayService {

	public Map<String, Object> dlrWxpay(String filePath, float money, int gmID);

	public Map<String, Object> ymzWxpay(String filePath, int gmID, int gmNum,
			int spID);

	// public String wxNotifyResult(HttpServletRequest request);

	// public int updateOrder(String orderNum);

	// public int updateOrderAndUser(String orderNum);

}
