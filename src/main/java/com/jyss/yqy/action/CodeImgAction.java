package com.jyss.yqy.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jyss.yqy.utils.ValidateCode;

@Controller
@RequestMapping("/code")
public class CodeImgAction {

	@RequestMapping("/shiro-getCode")
	public void getCode(HttpServletResponse response, HttpSession session) {
		ValidateCode vCode = new ValidateCode(100, 30, 4, 100);
		session.removeAttribute("codeImg");
		try {
			vCode.write(response.getOutputStream());
			session.setAttribute("codeImg", vCode.getCode());
			System.out.println(vCode.getCode());
			// session.setAttribute("username", "00");// 防过滤，图片可以加载
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
