package com.ls.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 字符串转日期对象
     * @param str
     * @param format
     * @return
     */
    public static Date formatString(String str, String format) throws ParseException {
        if(StringUtil.isEmpty(str)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    public static void main(String[] args) throws ParseException {
        String timeStamp = "1495710137";
        String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
        Date date1 = formatString(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println(date1);
    }


    public static Date formatTimeStampToDate(String timeStamp,String format) throws ParseException {
        String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
        return formatString(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 日期对象转字符串
     */
    public static String formatDate(Date date, String format){
        String result="";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if(date!=null){
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * 获取当前时间字符串
     */
    public static String getCurrentDateStr(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }
}
