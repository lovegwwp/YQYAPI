package com.jyss.yqy.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.Goods;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.utils.CommTool;

@Controller
public class OrdersBAction {

	@Autowired
	private OrdersBService obService;

	// ///////////////////b端商品==亚麻籽油///////////////////////////////
	// 亚麻籽油商品详情
	@RequestMapping("/getGoods")
	@ResponseBody
	public Map<String, Object> getGoods() {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		List<Goods> gList = new ArrayList<Goods>();
		gList = obService.getGoods("4");
		if (gList == null || gList.size() == 0) {
			m.put("data", "");
			return m;
		}
		for (Goods goods : gList) {
			goods.setPics("http://shop.chuangke.cohcreate.com/"
					+ goods.getPics());
		}
		m.put("data", gList);
		return m;

	}

	// ///////////////////b端===订单信息///////////////////////////////
	// 购买商品 ，添加订单
	@RequestMapping("/addOrder")
	@ResponseBody
	public ResponseEntity addOrder(@RequestParam("myJson") String myJson) {
		// TODO Auto-generated method stub
		System.out.println(myJson);

		if (myJson == null || myJson.equals("")) {
			return new ResponseEntity("false", "发送数据为空！");
		}
		OrdersB ob = null;
		try {
			ob = JSON.parseObject(myJson, OrdersB.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("false", "解析错误！");
		}
		if (ob == null) {
			return new ResponseEntity("false", "解析错误！");
		}
		int count = 0;
		// 根据购买人ID 查找相应信息
		ob.setGmr("LXH");
		ob.setTel("15161197896");
		ob.setOrderSn(CommTool.getOrderSn(ob.getGmId()));
		ob.setStatus("1");// 正常订单，待提货
		ob.setGmDw("盒");
		count = obService.addOrder(ob);
		if (count == 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");
	}

	// 未付款订单，付款成功，修改状态
	@RequestMapping("/upZfStatus")
	@ResponseBody
	public ResponseEntity upZfStatus(@RequestParam("orderSn") String orderSn) {
		// TODO Auto-generated method stub
		int count = 0;
		if (orderSn == null || orderSn.equals("")) {
			return new ResponseEntity("false", "订单号为空！");
		}
		count = upMyOrderStatus("1", "-1", orderSn);
		if (count == 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");

	}

	// 提货成功，修改订单状态
	@RequestMapping("/thd/upOrderStatus")
	@ResponseBody
	public ResponseEntity upOrderStatus(@RequestParam("orderSn") String orderSn) {
		// TODO Auto-generated method stub
		int count = 0;
		if (orderSn == null || orderSn.equals("")) {
			return new ResponseEntity("false", "订单号为空！");
		}
		count = upMyOrderStatus("2", "1", orderSn);
		if (count == 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");

	}

	public int upMyOrderStatus(String status, String statusBefore,
			String orderSn) {
		int count = 0;
		count = obService.upOrderStatus(status, statusBefore, orderSn);
		return count;
	}

	// 查询个人订单
	@RequestMapping("/getMyOrders")
	@ResponseBody
	public Map<String, Object> getMyOrders(@RequestParam("gmId") String gmId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit) {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		List<OrdersB> obList = new ArrayList<OrdersB>();
		PageHelper.startPage(page, limit);// 分页语句
		obList = obService.getOrdersBy("", "", gmId);
		PageInfo<OrdersB> pageInfoOrder = new PageInfo<OrdersB>(obList);
		m.put("data", new Page<OrdersB>(pageInfoOrder));
		return m;

	}

	// 删除订单
	@RequestMapping("/delOrder")
	@ResponseBody
	public ResponseEntity delOrder(@RequestParam("orderSn") String orderSn) {
		// TODO Auto-generated method stub
		int count = 0;
		if (orderSn == null || orderSn.equals("")) {
			return new ResponseEntity("false", "订单号为空！");
		}
		count = obService.delOrder(orderSn);
		if (count == 1) {
			return new ResponseEntity("true", "操作成功！");
		}
		return new ResponseEntity("false", "操作失败！");

	}
}
