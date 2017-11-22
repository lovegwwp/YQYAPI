package com.jyss.yqy.utils;

import java.security.Security;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	/**
	 * @param to
	 *            邮件接收着地址
	 * 
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件正文
	 * @return true发送成功|false发送失败
	 */
	public static boolean send(String from, String pwd, String to,
			String subject, String msg) {
		boolean isSucc = false;
		final Properties props = new Properties();
		// SSL
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// qgcltwueuhjgcaaf
		// 邮件传输的协议
		props.put("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		// 连接的邮件服务器
		// props.put("mail.host", "smtp.163.com");
		// props.put("mail.host", "smtp.qq.com");
		props.put("mail.host", "smtp.zoho.com.cn");
		// 发送人
		// props.put("mail.from", "lovegwwp@163.com");
		// props.put("mail.from", "435217379@qq.com");
		// 端口
		// props.put("mail.smtp.port", "587");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		// 用户名
		props.put("mail.user", from);
		// 密码qgcltwueuhjgcaaf
		props.put("mail.password", pwd);
		// props.put("mail.smtp.ssl.enable", "true");
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 第一步：创建Session
		Session session = Session.getInstance(props, authenticator);
		session.setDebug(true);
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		try {
			// 设置发件人
			InternetAddress fromAddress = new InternetAddress(
					props.getProperty("mail.user"));
			message.setFrom(fromAddress);

			// 设置收件人的邮箱
			InternetAddress toAddress = new InternetAddress(to);
			message.setRecipient(RecipientType.TO, toAddress);
			// 设置邮件标题
			message.setSubject(subject);

			// 设置邮件的内容体
			message.setContent(msg, "text/html;charset=UTF-8");
			// 最后当然就是发送邮件啦
			Transport.send(message);
			isSucc = true;
			System.out.println("SUCCESS!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSucc;

	}

	public static void main(String[] args) {

		// send("mstadmin@zoho.com.cn", "suzhou99", "435217379@qq.com", "明斯特",
		// " 浪淘沙" + "帘外雨潺潺春意阑珊罗衾不耐五更寒梦里不知身是客一晌贪欢独自莫凭阑无限江山别时容易见时难流水落花春去也天上人间");
		/*
		 * System.out.println(PasswordUtil.generate("666666",
		 * "59582404").equals( "5b37c1a5b61599a8d2c4063bf8e0f665"));
		 */
		// System.out.println("我爱罗".length());
	}

}
