package com.jyss.yqy.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取网络IP 1、获取本地的IP地址 2、获取本机的服务器名称 3、获取远程服务器的主机IP地址 4、获取远程服务器的所有主机IP
 * 
 * 网络编程： InetAddress 是用来封装IP地址相关信息的类 getLocalHost() 为本地主机创建IP对象 getByName(host)
 * 根据域名获取服务器主机地址对象 getAllByName(host) 为指定的一组同名的主机创建一个InetAddress对象数组
 * 
 * Created by dsp on 2/25/15.
 */
public class InternetInfo {

	/*
	 * 获取本地主机IP地址
	 */
	public static Map<String, String> getLocalIP() {
		Map<String, String> m = new HashMap<String, String>();
		// 创建本地主机IP地址对象
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String hostAddr = addr.getHostAddress(); // 获取IP地址
			String hostName = addr.getHostName(); // 获取本地机器名
			m.put("IP", hostAddr);
			m.put("HOSTNAME", hostName);
			System.out.println("本地IP地址：" + hostAddr);
			System.out.println("本地的机器名称：" + hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * 根据域名获取主机服务器的IP
	 * 
	 * @param hostName
	 *            域名
	 */
	public static String getIPByName(String hostName) {
		// String hostName = "www.baidu.com";
		String hostAddr = "";
		try {
			// 根据域名创建主机地址对象
			InetAddress addr = InetAddress.getByName(hostName);
			// 获取主机IP
			hostAddr = addr.getHostAddress();
			System.out.println("域名为：" + hostName + "的主机IP地址：" + hostAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostAddr;
	}

	/**
	 * 根据域名获得主机所有的IP地址
	 * 
	 * @param hostName
	 *            域名
	 */
	public static InetAddress[] getAllIPByName(String hostName) {
		// String hostName = "www.baidu.com";
		InetAddress[] addrs = null;
		try {
			addrs = InetAddress.getAllByName(hostName);
			String[] ips = new String[addrs.length];
			System.out.println("域名为：" + hostName + "的主机所有的IP地址为：");
			for (int i = 0; i < addrs.length; ++i) {
				ips[i] = addrs[i].getHostAddress();
				System.out.println(ips[i]);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return addrs;
	}

	// 测试
	public static void main(String[] args) {
		String hostName = "www.baidu.com";
		getLocalIP();
		getIPByName(hostName);
		getAllIPByName(hostName);
	}

}