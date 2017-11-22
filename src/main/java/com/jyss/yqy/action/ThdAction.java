package com.jyss.yqy.action;

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

import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.entity.ThOrders;
import com.jyss.yqy.entity.Thd;
import com.jyss.yqy.filter.MySessionContext;
import com.jyss.yqy.service.ThdService;
import com.jyss.yqy.utils.CommTool;
import com.jyss.yqy.utils.HttpClientUtil;
import com.jyss.yqy.utils.PasswordUtil;

@Controller
@RequestMapping("/thd")
public class ThdAction {
	@Autowired
	private ThdService thdService;

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
	public Map sendCode(@RequestParam("tel") String tel,
			HttpServletRequest request) {
		Map map = new HashMap();
		if (tel != null && !"".equals(tel)) {
			List<Thd> list = thdService.findThdByTel(tel);
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
						+ ",请尽快在10分钟内完成验证");
				if (msgDo.equals("1")) {
					map.put("status", "true");
					map.put("message", "操作成功！");
					map.put("sessionId", sessionId);
					return map;
				}
				map.put("status", "false");
				map.put("message", "操作失败！");
				map.put("sessionId", "");
				return map;
			}
		}
		map.put("status", "false");
		map.put("message", "请输入正确的手机号");
		map.put("sessionId", "");
		return map;
	}

	/**
	 * 根据验证码修改密码
	 */
	@RequestMapping("/upThdPwd")
	@ResponseBody
	public ResponseEntity upHtPwd(@RequestParam("tel") String tel,
			@RequestParam("code") String code,
			@RequestParam("password") String password,
			HttpServletRequest request,
			@RequestParam("sessionId") String sessionId) {
		if (StringUtils.isEmpty(password)) {
			return new ResponseEntity("false", "密码不能为空");
		}
		List<Thd> list = thdService.findThdByTel(tel);
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
					return new ResponseEntity("true", "密码修改成功");
				}
			}

		}
		return new ResponseEntity("false", "请重新修改");
	}

	/**
	 * 修改密码
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	public ResponseEntity updatePwd(@RequestParam("tel") String tel,
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("password") String password) {
		if (StringUtils.isEmpty(password)) {
			return new ResponseEntity("false", "新密码不能为空");
		}
		List<Thd> list = thdService.findThdByTel(tel);
		if (list != null && list.size() > 0) {
			Thd thd = list.get(0);
			if (PasswordUtil.generate(oldPwd, thd.getSalt()).equals(
					thd.getPassword())) {
				String salt = CommTool.getSalt();
				String pwd = PasswordUtil.generate(password, salt);
				thdService.updatePwd(tel, pwd, salt);
				return new ResponseEntity("true", "密码修改成功");
			}
			return new ResponseEntity("false", "旧密码不正确");
		}

		return new ResponseEntity("false", "请重新设置");
	}

	// 查询个人订单
	@RequestMapping("/getThdOrders")
	@ResponseBody
	public Map<String, List<ThOrders>> getThdOrders(
			@RequestParam("thId") String thId) {
		// TODO Auto-generated method stub
		Map<String, List<ThOrders>> m = new HashMap<String, List<ThOrders>>();
		List<ThOrders> obList = new ArrayList<ThOrders>();
		if (thId == null || thId.equals("")) {
			m.put("orderList", obList);
			return m;
		}
		obList = thdService.getThdOrdersBy(thId);
		m.put("orderList", obList);
		return m;

	}

}
