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

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date date = sdf.parse("2020-07-21 10:26");
//        Date date = formatString("2020-07-21 10:26", "yyyy-MM-dd HH:mm");
//        System.out.println(date);

//        String timeStamp = "2018年9月26日";
//        String date = timeStamp2Date(timeStamp, "yyyy年MM月dd日");
//        Date date1 = formatString(timeStamp, "yyyy年MM月dd日");
//        System.out.println(date1);

//        String address = new String("知乎专栏 - zhuanlan.zhihu.com/p... - 2019-1-11");
//        int i = address.indexOf("-",address.indexOf("-")+1);
//        String substring = address.substring(31);
//        System.out.println(substring);

//        String[] str = address.split("\\-");
//        for(String s : str){
//            System.out.println(s);
//        }

//        String str ="232ljsfsf.sdfl23.ljsdfsdfsdfss.23423.sdfsdfsfd";
//        //获得第一个点的位置
//        int index=str.indexOf(".");
//        System.out.println(index);
//        //根据第一个点的位置 获得第二个点的位置
//        index=str.indexOf(".", index+1);
//        //根据第二个点的位置，截取 字符串。得到结果 result
//        String result=str.substring(index+1);
//        //输出结果
//        System.out.println(result);

//        Date date = formatStringToDate("知乎专栏 - zhuanlan.zhihu.com/p... - 2019-1-11", "yyyy-MM-dd");
//        System.out.println(date);

//        String str = "ins生活-inszine 2020-08-13 20:50:00";
//        int index=str.indexOf(" ");
//        String result=str.substring(index+1);
//        Date date = formatString(result, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        System.out.println(format);
//        String[] strNow1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
//        System.out.println(strNow1);

    }


    public static Date formatStringToDate(String str,String format) throws ParseException {
        int index=str.indexOf("-");
        index=str.indexOf("-", index+1);
        String result=str.substring(index+1);
        Date date = formatString(result, format);
        return date;
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
