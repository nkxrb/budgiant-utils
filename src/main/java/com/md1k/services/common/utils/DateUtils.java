package com.md1k.services.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang3.time.DateUtils类
 * 
 * @author vvk
 * @date 2018-08-16
 * @version V 1.0
 */

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd 12:00:00");
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

	private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	public static Long getTimestamp(){
		return new Date().getTime();
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return sdf1.format(date);
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * @param before
	 * @return
	 */
	public static String convertTimeToFormat(Date before) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - before.getTime();

		if (time < 60 && time >= 0) {
			return "刚刚";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟前";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时前";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天前";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月前";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年前";
		} else {
			return "刚刚";
		}
	}
	/**
	 * 获取昨天的时间
	 * @return
	 */
	public static String getYesterday(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Date curDate = new Date();
		calendar.setTime(curDate);
		calendar.add(Calendar.DATE, -1);
		String yesterday = sdf2.format(calendar.getTime());
		return yesterday;
	}
	/**
	 * 获取上一天的时间
	 * @param initDate
	 * @return
	 */
	public static Date getBeforeDate(Date initDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(initDate);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获取相隔N天的时间（负数为前N天，正数为后N天）
	 * @param initDate
	 * @return
	 */
	public static Date getApartDate(Date initDate,int n){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(initDate);
		calendar.add(Calendar.DATE, n);
		return calendar.getTime();
	}

	/**
	 * 获取相隔N小时的时间（负数为前N小时，正数为后N小时）
	 * @param initDate
	 * @return
	 */
	public static Date getApartTime(Date initDate,int n){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(initDate);
		calendar.add(Calendar.HOUR_OF_DAY, n);
		return calendar.getTime();
	}

	/**
	 * 格式化日期字符串
	 * @param dateStr
	 * @param simpleDateFormat
	 * @return
	 */
	public static String formatDateStr(String dateStr,SimpleDateFormat simpleDateFormat){
		if(!EditUtil.isEmptyOrNull(dateStr)){
			try {
				return simpleDateFormat.format(DateUtils.sdf1.parse(dateStr));
			} catch (ParseException e) {
				e.printStackTrace();
				System.err.println("解析日期错误！");
			}
		}
		return dateStr;
	}

	/**
	 * 获取零点时刻的日期时间
	 * @param initDate
	 * @return
	 */
	public static Date getZeroDate(Date initDate){
		try {
			return sdf1.parse(sdf2.format(initDate));
		} catch (ParseException e) {
			e.printStackTrace();
			return initDate;
		}
	}
	/**
	 * 获取零点时刻的日期时间
	 * @param initDate
	 * @return
	 */
	public static String getZeroDate(String initDate){
		return sdf2.format(LocalDateTime.parse(initDate));
	}
	/**
	 * 重新规划导出excel的时间区域
	 * 
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, Object> getDateInterval(Map<String, Object> map) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String startTime = EditUtil.objByNullToValue(map.get("startTime"));
		String endTime = EditUtil.objByNullToValue(map.get("endTime"));
		try {
			if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
				// 开始、结束时间都为空，则显示昨天的数据
				Date curDate = new Date();
				calendar.setTime(curDate);
				calendar.add(Calendar.DATE, -7);
				startTime = sdf2.format(calendar.getTime());
				endTime = sdf2.format(curDate);
			} else if (!StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
				// 结束时间为空，开始时间+2个月
				Date start = sdf1.parse(startTime);
				calendar.setTime(start);
				calendar.add(Calendar.MONTH, 2);
				startTime = sdf2.format(start);
				endTime = sdf2.format(calendar.getTime());
			} else if (StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
				// 开始时间为空，结束时间-2个月
				Date end = sdf1.parse(endTime);
				calendar.setTime(end);
				calendar.add(Calendar.MONTH, -2);
				endTime = sdf2.format(end);
				startTime = sdf2.format(calendar.getTime());
			} else {
				Date start = sdf1.parse(startTime);
				Date end = sdf1.parse(endTime);
				calendar.setTime(start);
				if (start.equals(end)) {
					calendar.add(Calendar.DATE, 1);
					endTime = sdf2.format(calendar.getTime());
				}else{
					calendar.add(Calendar.MONTH, 2);
					if (calendar.getTime().before(end)) {
						endTime = sdf2.format(calendar.getTime());
					}
				}
			}
		} catch (ParseException e) {
			map.put("startTime", "undefind");
			map.put("endTime", "undefind");
			e.printStackTrace();
			return map;
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}
	/**
	 *当前时间往回七天
	 */
	public static List<Map<String,String>> getStartAndEndOfDay() {
		List list= new ArrayList();
		Map<String ,String > map = new HashMap<>();
		Date sta = new Date();
		Calendar endTime  = Calendar.getInstance();
		endTime.setTime(sta);
//		endTime.add(Calendar.DAY_OF_MONTH, -7);//+1今天的时间加一天
//		endTime.set(Calendar.HOUR_OF_DAY, 0);
//		endTime.set(Calendar.MINUTE, 0);
//		endTime.set(Calendar.SECOND, 0);
//		endTime.set(Calendar.MILLISECOND, 0);
		Date   start= endTime.getTime();
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(sta);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		Date time = startTime.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("time",sdf.format(time));
		list.add(map);
		return list;

	}

	public static String createUderCode(Integer order_id, Integer good_id, Byte order_type) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		String dateStr = sdf.format(date) + "";
		String str = dateStr.split(" ")[0];
		StringBuffer card_number = new StringBuffer();
		card_number.append(str.split(":")[0] + str.split(":")[1] + "" + str.split(":")[2]);
		card_number.append((order_id > 9999 ? order_id.toString().substring(0, 4) : String.format("%04d", order_id)));
		card_number.append((good_id > 9999 ? good_id.toString().substring(0, 4) : String.format("%04d", good_id)));
		card_number.append("000" + order_type);
		int dd = new Random().nextInt(10000);
		card_number.append((dd > 9999 ? (dd + "").substring(0, 4) : String.format("%04d", dd)));
		return card_number.toString();
	}
}
