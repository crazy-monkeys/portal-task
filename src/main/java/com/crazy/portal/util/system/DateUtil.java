package com.crazy.portal.util.system;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * <p>
 * 时间处理工具类
 * </p>
 */
public class DateUtil {
	private final static Logger log = LoggerFactory.getLogger(DateUtil.class);

	public final static long ONE_DAY_SECONDS = 86400;

	/*
	 * private static DateFormat dateFormat = null; private static DateFormat
	 * longDateFormat = null; private static DateFormat dateWebFormat = null;
	 */
	public final static String shortFormat = "yyyyMMdd";
	public final static String longFormat = "yyyyMMddHHmmss";
	public final static String webFormat = "yyyy-MM-dd";
	public final static String timeFormat = "HHmmss";
	public final static String monthFormat = "yyyyMM";
	public final static String chineseDtFormat = "yyyy年MM月dd日";
	public final static String newFormat = "yyyy-MM-dd HH:mm:ss";
	public final static String noSecondFormat = "yyyy-MM-dd HH:mm";
	public final static long ONE_DAY_MILL_SECONDS = 86400000;

	public final static String START_HOUR_MIN_SEC = "00:00:00";
	public final static String END_HOUR_MIN_SEC = "23:59:59";

	public static DateFormat getNewDateFormat(String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);

		df.setLenient(false);
		return df;
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}

		return new SimpleDateFormat(format).format(date);
	}

	public static Date parseDateNoTime(String sDate) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(shortFormat);

		if ((sDate == null) || (sDate.length() < shortFormat.length())) {
			throw new ParseException("length too little", 0);
		}

		if (!StringUtils.isNumeric(sDate)) {
			throw new ParseException("not all digit", 0);
		}

		return dateFormat.parse(sDate);
	}

	public static Date parseDateNoTime(String sDate, String format) throws ParseException {
		if (StringUtils.isBlank(format)) {
			throw new ParseException("Null format. ", 0);
		}

		DateFormat dateFormat = new SimpleDateFormat(format);

		if ((sDate == null) || (sDate.length() < format.length())) {
			throw new ParseException("length too little", 0);
		}

		return dateFormat.parse(sDate);
	}

	public static Date parseDate(String sDate, String format) throws ParseException {
		if (StringUtils.isBlank(format)) {
			throw new ParseException("Null format. ", 0);
		}

		DateFormat dateFormat = new SimpleDateFormat(format);

		if (sDate == null) {
			throw new ParseException("sDate is null", 0);
		}

		return dateFormat.parse(sDate);
	}

	public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException {
		sDate = sDate.replaceAll(delimit, "");

		DateFormat dateFormat = new SimpleDateFormat(shortFormat);

		if ((sDate == null) || (sDate.length() != shortFormat.length())) {
			throw new ParseException("length not match", 0);
		}

		return dateFormat.parse(sDate);
	}

	public static Date parseDateLongFormat(String sDate) {
		DateFormat dateFormat = new SimpleDateFormat(longFormat);
		Date d = null;

		if ((sDate != null) && (sDate.length() == longFormat.length())) {
			try {
				d = dateFormat.parse(sDate);
			} catch (ParseException ex) {
				return null;
			}
		}

		return d;
	}

	public static Date parseDateNewFormat(String sDate) {
		DateFormat dateFormat = new SimpleDateFormat(newFormat);
		Date d = null;
		if ((sDate != null) && (sDate.length() == newFormat.length())) {
			try {
				d = dateFormat.parse(sDate);
			} catch (ParseException ex) {
				return null;
			}
		}
		return d;
	}

	/**
	 * 计算当前时间几小时之后的时间
	 * 
	 * @param date
	 * @param hours
	 * 
	 * @return
	 */
	public static Date addHours(Date date, long hours) {
		return addMinutes(date, hours * 60);
	}

	/**
	 * 计算当前时间几分钟之后的时间
	 * 
	 * @param date
	 * @param minutes
	 * 
	 * @return
	 */
	public static Date addMinutes(Date date, long minutes) {
		return addSeconds(date, minutes * 60);
	}

	/**
	 * @param date1
	 * @param secs
	 * 
	 * @return
	 */

	public static Date addSeconds(Date date1, long secs) {
		return new Date(date1.getTime() + (secs * 1000));
	}

	/**
	 * 判断输入的字符串是否为合法的小时
	 * 
	 * @param hourStr
	 * 
	 * @return true/false
	 */
	public static boolean isValidHour(String hourStr) {
		if (!StringUtils.isEmpty(hourStr) && StringUtils.isNumeric(hourStr)) {
			int hour = new Integer(hourStr).intValue();

			if ((hour >= 0) && (hour <= 23)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断输入的字符串是否为合法的分或秒
	 * 
	 * @param minuteStr
	 * 
	 * @return true/false
	 */
	public static boolean isValidMinuteOrSecond(String str) {
		if (!StringUtils.isEmpty(str) && StringUtils.isNumeric(str)) {
			int hour = new Integer(str).intValue();

			if ((hour >= 0) && (hour <= 59)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 取得新的日期
	 * 
	 * @param date1
	 *            日期
	 * @param days
	 *            天数
	 * 
	 * @return 新的日期
	 */
	public static Date addDays(Date date1, long days) {
		return addSeconds(date1, days * ONE_DAY_SECONDS);
	}

	public static String getTomorrowDateString(String sDate) throws ParseException {
		Date aDate = parseDateNoTime(sDate);

		aDate = addSeconds(aDate, ONE_DAY_SECONDS);

		return getDateString(aDate);
	}

	public static String getTomorrowWebDate(String sDate) throws ParseException {
		String tomorrowShortDate = getTomorrowDateString(convertWeb2ShortFormat(sDate));

		return convert2WebFormat(tomorrowShortDate);
	}

	public static String getLongDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(longFormat);

		return getDateString(date, dateFormat);
	}

	public static String getNewFormatDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(newFormat);
		return getDateString(date, dateFormat);
	}

	public static String getDateString(Date date, DateFormat dateFormat) {
		if (date == null || dateFormat == null) {
			return null;
		}

		return dateFormat.format(date);
	}

	public static String getYesterDayDateString(String sDate) throws ParseException {
		Date aDate = parseDateNoTime(sDate);

		aDate = addSeconds(aDate, -ONE_DAY_SECONDS);

		return getDateString(aDate);
	}

	/**
	 * @return 当天的时间格式化为"yyyyMMdd"
	 */
	public static String getDateString(Date date) {
		DateFormat df = getNewDateFormat(shortFormat);

		return df.format(date);
	}

	public static String getWebDateString(Date date) {
		DateFormat dateFormat = getNewDateFormat(webFormat);

		return getDateString(date, dateFormat);
	}

	public static String getNoSecondDateString(Date date) {
		DateFormat dateFormat = getNewDateFormat(noSecondFormat);

		return getDateString(date, dateFormat);
	}

	/**
	 * 取得“X年X月X日”的日期格式
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String getChineseDateString(Date date) {
		DateFormat dateFormat = getNewDateFormat(chineseDtFormat);

		return getDateString(date, dateFormat);
	}

	public static String getTodayString() {
		DateFormat dateFormat = getNewDateFormat(shortFormat);

		return getDateString(new Date(), dateFormat);
	}

	public static String getTimeString(Date date) {
		DateFormat dateFormat = getNewDateFormat(timeFormat);

		return getDateString(date, dateFormat);
	}

	public static String getBeforeDayString(int days) {
		Date date = new Date(System.currentTimeMillis() - (ONE_DAY_MILL_SECONDS * days));
		DateFormat dateFormat = getNewDateFormat(shortFormat);

		return getDateString(date, dateFormat);
	}

	/**
	 * 取得两个日期间隔秒数（日期1-日期2）
	 * 
	 * @param one
	 *            日期1
	 * @param two
	 *            日期2
	 * 
	 * @return 间隔秒数
	 */
	public static long getDiffSeconds(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
	}

	public static long getDiffMinutes(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
	}

	/**
	 * 取得两个日期的间隔天数
	 * 
	 * @param one
	 * @param two
	 * 
	 * @return 间隔天数
	 */
	public static long getDiffDays(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
	}

	public static String getBeforeDayString(String dateString, int days) {
		Date date;
		DateFormat df = getNewDateFormat(shortFormat);

		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			date = new Date();
		}

		date = new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));

		return df.format(date);
	}

	public static boolean isValidShortDateFormat(String strDate) {
		if (strDate.length() != shortFormat.length()) {
			return false;
		}

		try {
			Integer.parseInt(strDate); // ---- 避免日期中输入非数字 ----
		} catch (Exception NumberFormatException) {
			return false;
		}

		DateFormat df = getNewDateFormat(shortFormat);

		try {
			df.parse(strDate);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	public static boolean isValidShortDateFormat(String strDate, String delimiter) {
		String temp = strDate.replaceAll(delimiter, "");

		return isValidShortDateFormat(temp);
	}

	/**
	 * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
	 * 
	 * @param strDate
	 * @return
	 */
	public static boolean isValidLongDateFormat(String strDate) {
		if (strDate.length() != longFormat.length()) {
			return false;
		}

		try {
			Long.parseLong(strDate); // ---- 避免日期中输入非数字 ----
		} catch (Exception NumberFormatException) {
			return false;
		}

		DateFormat df = getNewDateFormat(longFormat);

		try {
			df.parse(strDate);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	/**
	 * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
	 * 
	 * @param strDate
	 * @param delimiter
	 * @return
	 */
	public static boolean isValidLongDateFormat(String strDate, String delimiter) {
		String temp = strDate.replaceAll(delimiter, "");

		return isValidLongDateFormat(temp);
	}

	public static String getShortDateString(String strDate) {
		return getShortDateString(strDate, "-|/");
	}

	public static String getShortDateString(String strDate, String delimiter) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}

		String temp = strDate.replaceAll(delimiter, "");

		if (isValidShortDateFormat(temp)) {
			return temp;
		}

		return null;
	}

	public static String getShortFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();

		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		DateFormat df = getNewDateFormat(shortFormat);

		return df.format(cal.getTime());
	}

	public static String getWebTodayString() {
		DateFormat df = getNewDateFormat(webFormat);

		return df.format(new Date());
	}

	public static String getWebFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();

		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		DateFormat df = getNewDateFormat(webFormat);

		return df.format(cal.getTime());
	}

	public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
		try {
			Date date = formatIn.parse(dateString);

			return formatOut.format(date);
		} catch (ParseException e) {
			log.warn("convert() --- orign date error: " + dateString);
			return "";
		}
	}

	public static String convertWeb2ShortFormat(String dateString) {
		DateFormat df1 = getNewDateFormat(webFormat);
		DateFormat df2 = getNewDateFormat(shortFormat);

		return convert(dateString, df1, df2);
	}

	public static String convert2WebFormat(String dateString) {
		dateString = (dateString == null) ? "" : dateString;
		DateFormat df1 = getNewDateFormat(shortFormat);
		DateFormat df2 = getNewDateFormat(webFormat);

		return convert(dateString, df1, df2);
	}

	public static String convert2ChineseDtFormat(String dateString) {
		DateFormat df1 = getNewDateFormat(shortFormat);
		DateFormat df2 = getNewDateFormat(chineseDtFormat);

		return convert(dateString, df1, df2);
	}

	public static String convertFromWebFormat(String dateString) {
		DateFormat df1 = getNewDateFormat(shortFormat);
		DateFormat df2 = getNewDateFormat(webFormat);

		return convert(dateString, df2, df1);
	}

	public static boolean webDateNotLessThan(String date1, String date2) {
		DateFormat df = getNewDateFormat(webFormat);

		return dateNotLessThan(date1, date2, df);
	}

	/**
	 * @param date1
	 * @param date2
	 * @param dateWebFormat2
	 * 
	 * @return
	 */
	public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
		try {
			Date d1 = format.parse(date1);
			Date d2 = format.parse(date2);

			if (d1.before(d2)) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			log.debug("dateNotLessThan() --- ParseException(" + date1 + ", " + date2 + ")");
			return false;
		}
	}

	public static String getEmailDate(Date today) {
		String todayStr;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

		todayStr = sdf.format(today);
		return todayStr;
	}

	public static String getSmsDate(Date today) {
		String todayStr;
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

		todayStr = sdf.format(today);
		return todayStr;
	}

	public static String formatTimeRange(Date startDate, Date endDate, String format) {
		if ((endDate == null) || (startDate == null)) {
			return null;
		}

		String rt = null;
		long range = endDate.getTime() - startDate.getTime();
		long day = range / DateUtils.MILLIS_PER_DAY;
		long hour = (range % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR;
		long minute = (range % DateUtils.MILLIS_PER_HOUR) / DateUtils.MILLIS_PER_MINUTE;

		if (range < 0) {
			day = 0;
			hour = 0;
			minute = 0;
		}

		rt = format.replaceAll("dd", String.valueOf(day));
		rt = rt.replaceAll("hh", String.valueOf(hour));
		rt = rt.replaceAll("mm", String.valueOf(minute));

		return rt;
	}

	public static String formatMonth(Date date) {
		if (date == null) {
			return null;
		}

		return new SimpleDateFormat(monthFormat).format(date);
	}

	/**
	 * 获取系统日期的前一天日期，返回Date
	 * 
	 * @return
	 */
	public static Date getBeforeDate() {
		Date date = new Date();

		return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
	}

	/**
	 * 获得指定时间当天起点时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayBegin(Date date) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		df.setLenient(false);

		String dateString = df.format(date);

		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * 判断参date上min分钟后，是否小于当前时间
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static boolean dateLessThanNowAddMin(Date date, long min) {
		return addMinutes(date, min).before(new Date());

	}

	/**
	 * 是否在现在时间之前
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeNow(Date date) {
		if (date == null)
			return false;
		return date.compareTo(new Date()) < 0;
	}

	/**
	 * 是否有效
	 * 
	 * @param requestTime
	 *            请求时间
	 * @param effectTime
	 *            生效时间
	 * @param expiredTime
	 *            失效时间
	 * @return
	 */
	public static boolean isValidate(Date requestTime, Date effectTime, Date expiredTime) {
		if (requestTime == null || effectTime == null || expiredTime == null) {
			return false;
		}
		return effectTime.compareTo(requestTime) <= 0 && expiredTime.compareTo(requestTime) >= 0;
	}

	public static Date parseNoSecondFormat(String sDate) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(noSecondFormat);

		if ((sDate == null) || (sDate.length() < noSecondFormat.length())) {
			throw new ParseException("length too little", 0);
		}

		if (!StringUtils.isNumeric(sDate)) {
			throw new ParseException("not all digit", 0);
		}

		return dateFormat.parse(sDate);
	}

	/**
	 * 判断两个日期是否是同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(webFormat);
		return sdf.format(date1).equals(sdf.format(date2));
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentTS() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public static Date getWeekBefore() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 获取当前时间的前几天或者后几天的日期
	 * 
	 * @param days
	 * @return
	 */
	public static Date getDateNearCurrent(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		Date date = calendar.getTime();
		return date;

	}

	/**
	 * 计算两个时间的差（相差的天、小时、分钟、秒）
	 * 
	 * @param start
	 * @param end
	 * @param type
	 *            (可以是：day、hour、min、sec)
	 * @return
	 */
	public static Long getDiffSecondByTwoDate(Date start, Date end, String type) {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		diff = end.getTime() - start.getTime();
		long day = diff / nd;// 计算差多少天
		long hour = diff % nd / nh;// 计算差多少小时
		long min = diff % nd % nh / nm;// 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒

		if (type.equals("day")) {
			return diff / day;
		}
		if (type.equals("hour")) {
			return diff / hour;
		}
		if (type.equals("min")) {
			return diff / min;
		}
		if (type.equals("sec")) {
			return diff / sec;
		} else {
			return null;
		}
	}

	/**
	 * 指定时间推移前(后)几天
	 * 
	 * @param date
	 *            时间
	 * @param amount
	 *            几天
	 * @return
	 */
	public static Date computeWithDay(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return cal.getTime();
	}

	public static Date getCurrYearLastDate(){
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, currentYear);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}
	
	public static Timestamp getTimeStrFromDate(Timestamp time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strTime = sdf.format(new Date(time.getTime()));
		Date date = sdf.parse(strTime);
		return new Timestamp(date.getTime());
	}
	
	public static String getTimeStr(Timestamp time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time.getTime()));
	}
	
	/**
	 * 数据导入日期转换
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date dateHelp(String str) throws ParseException{
	    	Date returnDate = new Date();
	    	if(str.length()==18 && str.indexOf("-")>0 && str.indexOf(":")>0){
	    		returnDate = DateUtil.parseDate(str, "yyyy-MM-ddHH:mm:ss");
	    	}else if(str.indexOf("-")>0 && str.indexOf(":")>0){
	    		returnDate = DateUtil.parseDate(str, "yyyy-MM-dd HH:mm:ss");
			}else if(str.length()==10 && str.indexOf("-")>0 && str.indexOf(":")<0){
				returnDate = DateUtil.parseDate(str, "yyyy-MM-dd");
			}else if(str.indexOf("/")>0 && str.indexOf(":")>0){
				returnDate = DateUtil.parseDate(str, "yyyy/MM/dd HH:mm:ss");
			}else if(str.length()==10 && str.indexOf("/")>0 && str.indexOf(":")<0){
				returnDate = DateUtil.parseDate(str, "yyyy/MM/dd");
			}else if(str.length()==9&& str.indexOf("/")>0 && str.indexOf(":")<0){
				//存在  月份为一位，日期为两位的
				String[] arr = str.split("/");
				if(arr[1].length()==1){
					str=arr[0]+"/0"+arr[1]+"/"+arr[2];
				}else{
					str=arr[0]+"/"+arr[1]+"/0"+arr[2];	
				}
				returnDate = DateUtil.parseDate(str, "yyyy/MM/dd");
			}else if(str.length()==8&& str.indexOf("/")>0 && str.indexOf(":")<0){
				String[] arr = str.split("/");
				str=arr[0]+"/0"+arr[1]+"/0"+arr[2];
				returnDate = DateUtil.parseDate(str, "yyyy/MM/dd");
			}else{
				returnDate = null;
			}
	    	return returnDate;
	    }
	
}
