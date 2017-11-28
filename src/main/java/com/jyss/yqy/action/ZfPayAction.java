package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ZfPayAction {
	// type///// 支付方式：1=支付宝，2=微信，3=现金支付
	@RequestMapping(value = "/b/dlrOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> dlrOrder(@RequestParam int money,
			@RequestParam String type, @RequestParam int gmID) {
		Map<String, Object> m = new HashMap<String, Object>();
		return m;

	}

	@RequestMapping(value = "/b/ymzOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ymzOrder(@RequestParam int money,
			@RequestParam String type, @RequestParam String token,
			@RequestParam int spId, @RequestParam int num) {
		Map<String, Object> m = new HashMap<String, Object>();
		return m;

	}

}
