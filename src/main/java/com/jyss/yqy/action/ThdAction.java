package com.jyss.yqy.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.OrdersB;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ThOrders;
import com.jyss.yqy.entity.Thd;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.filter.MySessionContext;
import com.jyss.yqy.service.OrdersBService;
import com.jyss.yqy.service.ThdService;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.utils.Base64Image;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.HttpClientUtil;
import com.jyss.yqy.utils.PasswordUtil;

@Controller
@RequestMapping("/thd")
public class ThdAction {
	@Autowired
	private ThdService thdService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
	private OrdersBService obService;
	@Autowired
	private UserService userService;

	/**
	 * 用户登陆
	 */
	@RequestMapping("/thdLogin")
	@ResponseBody
	public Map<String, Object> login(String tel, String password) {
		Map<String, Object> map = thdService.login(tel, password);
		return map;
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping("/sendCode")
	@ResponseBody
	public Map<String, Object> sendCode(@RequestParam("tel") String tel,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (tel != null && !"".equals(tel)) {
			List<Thd> list = thdService.findThdByTel(tel, "");
			if (list != null && list.size() > 0) {
				Thd thd = list.get(0);
				String code = CommTool.getYzm();
				// 添加到session
				HttpSession session = request.getSession();
				String sessionId = "";
				sessionId = session.getId();
				session.removeAttribute("code");
				session.setAttribute("code", code);
				session.removeAttribute("tel");
				session.setAttribute("tel", tel);
				// 设置过期时间(S)
				session.setMaxInactiveInterval(10 * 60);

				String msgDo = HttpClientUtil.MsgDo(tel, "您此次操作的验证码为：" + code
						+ "，请尽快在10分钟内完成验证。");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("sessionId", sessionId);
				if (msgDo.equals("1")) {
					map.put("code", "0");
					map.put("status", "true");
					map.put("message", "操作成功！");
					map.put("data", map2);
					return map;
				}
				map.put("code", "-2");
				map.put("status", "false");
				map.put("message", "操作失败！");
				map.put("data", "");
				return map;
			}
		}
		map.put("code", "-1");
		map.put("status", "false");
		map.put("message", "请输入正确的手机号");
		map.put("data", "");
		return map;
	}

	/**
	 * 根据验证码修改密码
	 */
	@RequestMapping("/upThdPwd")
	@ResponseBody
	public Map<String, String> upHtPwd(@RequestParam("tel") String tel,
			@RequestParam("code") String code,
			@RequestParam("password") String password,
			HttpServletRequest request,
			@RequestParam("sessionId") String sessionId) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(password)) {
			map.put("code", "-1");
			map.put("status", "false");
			map.put("message", "密码不能为空！");
			map.put("data", "");
			return map;
		}
		List<Thd> list = thdService.findThdByTel(tel, "");
		if (list != null && list.size() > 0) {
			Thd thd = list.get(0);
			if (sessionId != null && !"".equals(sessionId)) {
				HttpSession session = MySessionContext.getSession(sessionId);
				String checkTel = (String) session.getAttribute("tel");
				String checkCode = (String) session.getAttribute("code");

				if (code.equals(checkCode) && thd.getTel().equals(checkTel)) {
					String salt = CommTool.getSalt();
					String pwd = PasswordUtil.generate(password, salt);
					thdService.updatePwd(tel, pwd, salt);
					map.put("code", "0");
					map.put("status", "true");
					map.put("message", "密码修改成功！");
					map.put("data", "");
					return map;
				}
			}

		}
		map.put("code", "-2");
		map.put("status", "false");
		map.put("message", "请重新修改！");
		map.put("data", "");
		return map;
	}

	/**
	 * 修改密码
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	public Map<String, String> updatePwd(@RequestParam("tel") String tel,
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("password") String password) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(password)) {
			map.put("code", "-1");
			map.put("status", "false");
			map.put("message", "新密码不能为空！");
			map.put("data", "");
			return map;
		}
		List<Thd> list = thdService.findThdByTel(tel, "");
		if (list != null && list.size() > 0) {
			Thd thd = list.get(0);
			if (PasswordUtil.generate(oldPwd, thd.getSalt()).equals(
					thd.getPassword())) {
				String salt = CommTool.getSalt();
				String pwd = PasswordUtil.generate(password, salt);
				thdService.updatePwd(tel, pwd, salt);
				map.put("code", "0");
				map.put("status", "true");
				map.put("message", "密码修改成功！");
				map.put("data", "");
				return map;
			}
			map.put("code", "-2");
			map.put("status", "false");
			map.put("message", "旧密码不正确！");
			map.put("data", "");
			return map;
		}

		map.put("code", "-3");
		map.put("status", "false");
		map.put("message", "请重新修改！");
		map.put("data", "");
		return map;
	}

	// 查询个人订单
	@RequestMapping("/getThdOrders")
	@ResponseBody
	public Map<String, Object> getThdOrders(
			@RequestParam("token") String token,
			@RequestParam("page") int page, @RequestParam("limit") int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String thId = uMobileLogin.getuUuid();

		List<ThOrders> obList = new ArrayList<ThOrders>();
		if (thId == null || thId.equals("")) {
			map.put("status", "false");
			map.put("message", "数据加载失败！");
			map.put("code", "-2");
			m.put("data", obList);
			return m;
		}
		PageHelper.startPage(page, limit);// 分页语句
		obList = thdService.getThdOrdersBy(thId);
		PageInfo<ThOrders> pageInfoOrder = new PageInfo<ThOrders>(obList);
		m.put("data", new Page<ThOrders>(pageInfoOrder));
		map.put("status", "true");
		map.put("message", "数据加载成功！");
		map.put("code", "0");
		return m;

	}

	// 确认订单
	@RequestMapping("/confirmOrders")
	@ResponseBody
	public Map<String, Object> getThdOrders(
			@RequestParam("token") String token,
			@RequestParam("orderSn") String orderSn,
			@RequestParam("type") String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid就是购买ID
		UMobileLogin uMobileLogin = loginList.get(0);
		String thId = uMobileLogin.getuUuid();
		if (thId == null || thId.equals("")) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}

		// ///type ===2 ====代表b端，1===代表A端商品
		// A端
		if (Integer.parseInt(type) == 1) {
			List<OrdersB> oaList = obService.selectOrderABy("2", orderSn, thId);
			if (oaList == null || oaList.size() == 0) {
				map.put("status", "false");
				map.put("message", "无此订单！！");
				map.put("code", "-3");
				map.put("data", "");
				return map;
			}
			for (OrdersB ordersB : oaList) {
				ThOrders tdOrder = new ThOrders();
				tdOrder.setOrderSn(orderSn);
				tdOrder.setThr(ordersB.getGmr());
				tdOrder.setTel(ordersB.getTel());
				tdOrder.setThSp(ordersB.getGmSp());
				tdOrder.setThNum(ordersB.getGmNum());
				tdOrder.setThDw("件");
				tdOrder.setThId(thId);
				tdOrder.setStatus("1");
				tdOrder.setPrice(ordersB.getPrice());
				thdService.addThOrder(tdOrder);
			}
			int count = obService.updateOrderAStatus("3", "2", orderSn);
			if (count == 1) {
				List<OrdersB> oaList1 = obService.selectTotalOrderABy("3",
						orderSn, thId);
				OrdersB ordersB = oaList1.get(0);
				m.put("tel", ordersB.getTel());
				m.put("num", ordersB.getGmNum() + "件商品");
				m.put("kh", ordersB.getGmr());
				m.put("sp", "共计");
				map.put("data", m);
				map.put("status", "true");
				map.put("message", "确认成功！");
				map.put("code", "0");
				return map;
			}
			map.put("data", "");
			map.put("status", "false");
			map.put("message", "确认失败！");
			map.put("code", "-4");
			return map;

		}

		// B端
		if (Integer.parseInt(type) == 2) {
			// //status='-1未付款状态,1待提货状态，2已提货，订单完成',
			List<OrdersB> obList = new ArrayList<OrdersB>();
			obList = obService.getOrdersBy("1", orderSn, "");
			if (obList == null || obList.size() != 1) {
				map.put("status", "false");
				map.put("message", "B端无此订单！！");
				map.put("code", "-3");
				map.put("data", "");
				return map;
			}
			OrdersB ob = obList.get(0);
			ThOrders tdOrder = new ThOrders(ob.getOrderSn(), ob.getGmr(),
					ob.getTel(), ob.getGmSp(), ob.getGmNum(), ob.getGmDw(),
					"1", thId, ob.getPrice());
			int count = 0;
			// ///提货端--订单插入===
			count = thdService.addThOrder(tdOrder);
			// ///B端--订单状态修改===
			if (count == 1) {
				count = 0;
				count = obService.upOrderStatus("2", "1", ob.getOrderSn());
			}
			if (count == 1) {
				m.put("tel", ob.getTel());
				m.put("num", ob.getGmNum() + ob.getGmDw());
				m.put("kh", ob.getGmr());
				m.put("sp", ob.getGmSp());
				map.put("data", m);
				map.put("status", "true");
				map.put("message", "确认成功！");
				map.put("code", "0");
				return map;
			}
			map.put("data", "");
			map.put("status", "false");
			map.put("message", "确认失败！");
			map.put("code", "-4");
			return map;
		}

		map.put("status", "false");
		map.put("message", "无此订单！");
		map.put("code", "-3");
		map.put("data", "");
		return map;

	}

	// 扫描订单
	@RequestMapping("/scanOrders")
	@ResponseBody
	public Map<String, Object> scanOrders(@RequestParam("token") String token,
			@RequestParam("orderSn") String orderSn,
			@RequestParam("type") String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid就是购买ID
		UMobileLogin uMobileLogin = loginList.get(0);
		String thId = uMobileLogin.getuUuid();
		if (thId == null || thId.equals("")) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		// ///type ===2 ====代表b端，1===代表A端商品

		// A端
		if (Integer.parseInt(type) == 1) {
			List<OrdersB> oaList = obService.selectTotalOrderABy("2", orderSn,
					thId);
			if (oaList != null && oaList.size() == 1) {
				OrdersB oa = oaList.get(0);
				if (oa == null || oa.getTel() == null || oa.getTel().equals("")
						|| oa.getGmNum() == null || oa.getGmNum().equals("")
						|| oa.getGmr() == null || oa.getGmr().equals("")) {
					map.put("status", "false");
					map.put("message", "无此订单！！");
					map.put("code", "-3");
					map.put("data", "");
					return map;
				}
				m.put("tel", oa.getTel());
				m.put("num", oa.getGmNum() + "件商品");
				m.put("kh", oa.getGmr());
				m.put("sp", "共计");
				map.put("data", m);
				map.put("status", "true");
				map.put("message", "扫描成功！");
				map.put("code", "0");
				return map;
			}
			map.put("status", "false");
			map.put("message", "无此订单！！");
			map.put("code", "-3");
			map.put("data", "");
			return map;

		}

		// B端
		if (Integer.parseInt(type) == 2) {
			// //status='-1未付款状态,1待提货状态，2已提货，订单完成',
			List<OrdersB> obList = new ArrayList<OrdersB>();
			obList = obService.getOrdersBy("1", orderSn, "");
			if (obList == null || obList.size() != 1) {
				map.put("status", "false");
				map.put("message", "B端无此订单！！");
				map.put("code", "-3");
				map.put("data", "");
				return map;
			}
			OrdersB ob = obList.get(0);

			m.put("tel", ob.getTel());
			m.put("num", ob.getGmNum() + ob.getGmDw());
			m.put("kh", ob.getGmr());
			m.put("sp", ob.getGmSp());
			map.put("data", m);
			map.put("status", "true");
			map.put("message", "扫描成功！");
			map.put("code", "0");
			return map;
		}

		map.put("status", "false");
		map.put("message", "无此订单！");
		map.put("code", "-3");
		map.put("data", "");
		return map;

	}

	@RequestMapping("/getThdInfo")
	@ResponseBody
	public Map<String, Object> getThdInfo(@RequestParam("token") String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid就是购买ID
		UMobileLogin uMobileLogin = loginList.get(0);
		String thId = uMobileLogin.getuUuid();
		if (thId == null || thId.equals("")) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}

		List<Thd> ThdList = thdService.findThdByTel("", thId);
		if (ThdList == null || ThdList.size() != 1) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		Thd th = ThdList.get(0);
		th.setPics(Constant.httpUrl + th.getPics());
		map.put("data", th);
		map.put("status", "true");
		map.put("message", "加载成功！");
		map.put("code", "0");
		return map;

	}

	// 修改提货端个人信息
	@RequestMapping("/upThdInfo")
	@ResponseBody
	public Map<String, Object> upThdInfo(@RequestParam("token") String token,
			@RequestParam("tel") String tel, @RequestParam("addr") String addr,
			@RequestParam("pics") String pics,
			@RequestParam("thName") String thName, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// //验证token 获取购买人ID===gmID
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "请重新登录");
			map.put("code", "-1");
			return map;
		}
		// /获取最新token ===uuid就是购买ID
		UMobileLogin uMobileLogin = loginList.get(0);
		String thId = uMobileLogin.getuUuid();
		if (thId == null || thId.equals("")) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		Thd t = new Thd();
		int id = 0;
		try {
			id = Integer.parseInt(thId);
		} catch (Exception e) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}

		if (id == 0) {
			map.put("status", "false");
			map.put("message", "提货单用户信息加载失败！");
			map.put("code", "-2");
			map.put("data", "");
			return map;
		}
		// ///////图片生成、、、截取
		if (!pics.equals("") && pics != null) {

			String filePath = request.getSession().getServletContext()
					.getRealPath("/");
			int index = filePath.indexOf("YQYAPI");
			// boolean isOk = false;
			filePath = filePath.substring(0, index) + "uploadThd" + "/";
			File f = new File(filePath);
			CommTool.judeDirExists(f);
			boolean isOk1 = false;
			String filePath1 = filePath + id + CommTool.getSalt() + ".png";
			isOk1 = Base64Image.GenerateImage(pics, filePath1);
			if (isOk1) {
				t.setPics(filePath1.substring(filePath1.indexOf("uploadThd")));
			} else {
				map.put("status", "false");
				map.put("message", "图片上传失败！");
				map.put("code", "-3");
				return map;
			}
		} else {
			t.setPics("");
		}
		int count = 0;
		t.setId(id);
		t.setAddr(addr);
		// t.setPics(pics);
		t.setTel(tel);
		t.setThName(thName);
		count = thdService.upThdInfo(t);
		if (count == 1) {
			map.put("status", "true");
			map.put("message", "修改成功！");
			map.put("code", "0");
			return map;
		}
		map.put("status", "false");
		map.put("message", "修改失败！");
		map.put("code", "-4");
		return map;

	}

	/**
	 * 用户退出
	 */

	@RequestMapping("/thdLogOut")
	@ResponseBody
	public Map<String, Object> loginOut(@RequestParam("token") String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<UMobileLogin> loginList = uMobileLoginService
				.findUserByToken(token);
		if (loginList == null || loginList.size() == 0) {
			map.put("status", "false");
			map.put("message", "身份过期！");
			map.put("code", "-1");
			map.put("data", "");
			return map;
		}
		// /获取最新token ===uuid
		UMobileLogin uMobileLogin = loginList.get(0);
		String uuuid = uMobileLogin.getuUuid();
		String newToken = CommTool.getUUID();
		int count = userService.loginOut(uuuid, newToken);
		if (count == 1) {
			map.put("status", "true");
			map.put("message", "退出成功！");
			map.put("code", "0");
			map.put("data", "");
			return map;
		}
		map.put("status", "false");
		map.put("message", "退出失败！");
		map.put("code", "-2");
		map.put("data", "");
		return map;
	}

}
