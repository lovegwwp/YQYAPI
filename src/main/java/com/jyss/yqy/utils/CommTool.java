package com.jyss.yqy.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CommTool {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String formatDate(Date date) throws ParseException {
		if (date != null) {
			return sdf.format(date);
		}
		return sdf.format(new Date());
	}

	public static String formatTimeStamp(Timestamp ts) throws ParseException {
		if (ts != null) {
			return sdf.format(ts);
		}
		return sdf.format(getNowTimestamp());
	}

	public static Date parseDate(String strDate) throws ParseException {

		return sdf.parse(strDate);
	}

	public static Date DateGsh(Date date) throws ParseException {
		if (date != null) {
			sdf.parse(formatDate(date));
		}
		return new Date();
	}

	public static Timestamp getNowTimestamp() {
		Date date = new Date();// 获得系统时间.
		String nowTime = sdf.format(date);// 将时间格式转换成符合Timestamp要求的格式.
		Timestamp times = Timestamp.valueOf(nowTime);// 把时间转换
		return times;
	}

	// 随机生成八位数字
	public static String getSalt() {
		StringBuilder str = new StringBuilder();// 定义变长字符串
		Random random = new Random();
		// 随机生成数字，并添加到字符串
		for (int i = 0; i < 8; i++) {
			str.append(random.nextInt(10));
		}
		// 将字符串转换为String
		return str.toString();

	}

	// 随机生成指定长度字符串+数字组合
	public static String getNonceStr(int n) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
			if ("char".equalsIgnoreCase(str)) { // 产生字母
				int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
				// System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
				val += (char) (nextInt + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(str)) { // 产生数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	// 随机生成6位验证码
	public static String getYzm() {
		StringBuilder sb = new StringBuilder();// 定义变长字符串
		Random random = new Random();
		// 随机生成数字，并添加到字符串
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(10));
		}
		// 将字符串转换为String
		return sb.toString();

	}

	// 随机生成6位验证码
	public static String getOrderSn(String id) {
		long sjc = new Date().getTime() / 1000;
		String orderSn = "";
		orderSn = sjc + "" + id + getYzm();
		return orderSn;

	}

	// 随机生成uuid
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;

	}

	// 随机生成uuid
	public static String getMyUUID() {
		String str = new Date().getTime() + "";
		String uuidStr = str + new Integer(4);
		return uuidStr;

	}

	// 规则生成设备号
	public static String getMyMac(String oldMac) {
		// 原字符创
		System.out.println(oldMac);
		// 生成加密
		String md5Str = PasswordUtil.generate(oldMac, "JYCS");
		System.out.println("md5Str========>" + md5Str);
		// 获取奇数位
		String OddStr = getOddString(md5Str);
		System.out.println("OddStr========>" + OddStr);
		// 拼接分隔符“-”
		String pjStr = getPjString(OddStr);
		System.out.println("pjStr========>" + pjStr.toUpperCase());
		return pjStr.toUpperCase();

	}

	// 获取字符串奇数位 拼接一起
	public static String getOddString(String oldStr) {
		StringBuffer sb = new StringBuffer();
		if (oldStr == null) {
			// throw new NullPointerException("空字符串");
			return "";
		}
		int n = oldStr.length();
		for (int i = 0; i < n; i++) {
			// 因为索引是从0开始，所以索引为偶数的是奇数位字符
			if (i % 2 == 0) {
				sb.append(oldStr.charAt(i));
			}
		}
		return sb.toString();

	}

	// 获取字符 4位“-” 拼接一起
	public static String getPjString(String oldStr) {
		StringBuffer strb = new StringBuffer();
		if (oldStr == null) {
			// throw new NullPointerException("空字符串");
			return "";
		}
		String temp = "";
		int startIndex = 0;
		int n = oldStr.length();
		for (int i = 1; i <= n; i++) {
			if (i % 4 == 0) {
				temp = oldStr.substring(startIndex, i);
				startIndex = i;
				strb.append(temp + "-");
			}
		}
		String re = strb.toString();
		return re.substring(0, re.length() - 1);

	}

	// 规则生成设备号
	public static Map<String, String> getMyMap(String weekDayTimeStr) {
		Map<String, String> m = new HashMap<String, String>();
		String week = "0";
		String day = "0";
		String time = "0";
		if (weekDayTimeStr != null && !weekDayTimeStr.equals("")) {
			week = weekDayTimeStr.substring(1, 2);
			day = weekDayTimeStr.substring(4, 5);
			time = weekDayTimeStr.substring(7, 8);
			m.put("week", week);
			m.put("day", day);
			m.put("time", time);
		}
		System.out.println(week + day + time);
		return m;

	}

	// 判断文件是否存在
	public static void judeFileExists(File file) {

		if (file.exists()) {
			System.out.println("file exists");
		} else {
			System.out.println("file not exists, create it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 判断文件夹是否存在
	public static void judeDirExists(File file) {

		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("dir exists");
			} else {
				System.out
						.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("dir not exists, create it ...");
			file.mkdir();
		}

	}

	public static void main(String[] args) throws IOException {
		// getMyMac("4197-B93E-A4B4-6FC9");
		// getMyMap("第1周第1天第1次");
		/*
		 * System.out.println(getNonceStr(30));
		 * System.out.println(getNonceStr(30).length());
		 */
		/*
		 * Date date = new Date();// 获得系统时间. System.out.println(date.getTime());
		 * System.out.println(getNowTimestamp());
		 */
		// System.out.println("dddddd");
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("cfmeogvo");
		// System.out.println(getOrderSn("28"));
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(getUUID());
		System.out.println(getUUID().length());
		System.out.println(getSalt());

	}

}
