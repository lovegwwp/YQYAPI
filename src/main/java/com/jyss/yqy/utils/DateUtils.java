package com.jyss.yqy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by youngyeah on 2017/9/25.
 */
public class DateUtils {

	private static String[] weeks = { "日", "一", "二", "三", "四", "五", "六" };

	/**
	 * 日期格式化 日期是今天：早上，中午，下午，晚上 日期是昨天： 本周几： 上周几： 本月xx日： 上个月xx日： 返回日期：
	 */

	public static void main(String[] args) throws ParseException {
		String s = "2017-09-21 13:37";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = sdf.parse(s);
		System.out.println(formatDate(date));
	}

	public static String formatDate(Date date) {

		int dayDiff = dayDiff(date);
		int weekDiff = weekDiff(date);
		int monthDiff = monthDiff(date);

		String datePrefix = "";
		if (dayDiff == 0) {
			datePrefix = "今天";
		} else if (dayDiff == 1) {
			datePrefix = "昨天";
		} else if (dayDiff == 2) {
			datePrefix = "前天";
		} else if (weekDiff == 0) {
			datePrefix = dayOfWeek(date);
		} else if (weekDiff == 1) {
			datePrefix = "上" + dayOfWeek(date);
		} else if (monthDiff == 0) {
			datePrefix = "本月" + dayOfMonth(date) + "日";
		} else if (monthDiff == 1) {
			datePrefix = "上月" + dayOfMonth(date) + "日";
		} else {
			datePrefix = format(date, "yyyy-MM-dd");
		}

		String dateSuffix = "";
		dateSuffix = getPeriodOfDay(date) + format(date, "hh:mm");// 返回12小时制
		return datePrefix + dateSuffix;
	}

	/**
	 * 一天的时间段
	 * 
	 * @param date
	 * @return
	 */
	public static String getPeriodOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String periodOfDay = "";
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour < 11) {
			periodOfDay = "早上";
		} else if (hour < 13) {
			periodOfDay = "中午";
		} else if (hour < 18) {
			periodOfDay = "下午";
		} else if (hour < 24) {
			periodOfDay = "晚上";
		}
		return periodOfDay;
	}

	/**
	 * 相差周
	 * 
	 * @param date
	 * @return
	 */
	public static int weekDiff(Date date) {
		Calendar calendar = Calendar.getInstance();
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTime(date);
		int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		return currentWeek - paramWeek;
	}

	/**
	 * 相差天
	 * 
	 * @param date
	 * @return
	 */
	public static int dayDiff(Date date) {
		Calendar calendar = Calendar.getInstance();
		int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTime(date);
		int paramDay = calendar.get(Calendar.DAY_OF_YEAR);
		return currentDay - paramDay;
	}

	/**
	 * 相差月
	 * 
	 * @param date
	 * @return
	 */
	public static int monthDiff(Date date) {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int currenMonth = calendar.get(Calendar.MONTH);
		calendar.setTime(date);
		int paramYear = calendar.get(Calendar.YEAR);
		int paramMonth = calendar.get(Calendar.MONTH);
		return (currentYear - paramYear) * 12 + (currenMonth - paramMonth);
	}

	/**
	 * 一周的第几天
	 * 
	 * @param date
	 * @return
	 */
	public static String dayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return "周" + weeks[day - 1];
	}

	/**
	 * 一个月的第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int dayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
}
