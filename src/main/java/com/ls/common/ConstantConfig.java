package com.ls.common;

/**
 * 常量表
 */
public class ConstantConfig {
    /**
     * 盐
     */
    public final static String SALT = "loushi";

    /**
     * 是否被封禁（1：未封禁，0：已封禁）
     */
    public final static Integer ISOFF = 1;
    public final static Integer NOOFF = 0;

    /**
     * 使用结束时间（新用户有3天使用时间，如需延长找管理员开通）
     */
    public final static Integer DAY = 3;

    /**
     * 当前用户名称
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 重置密码
     * 初始密码
     */
    public static final String RESET_PASSWORD = "000000";


    public final static Integer EVENT_STATE = 0; //未加入简报

    public final static Integer EVENT_TYPE_NEGATIVE = -1; //事件类型 负面
    public final static Integer EVENT_TYPE_NEUTRAL = 0; //事件类型 中性
    public final static Integer EVENT_TYPE_POSITIVE = 1; //事件类型 正面


    /**
     * 微信公众号发消息模板
     */
    public static final String TEXT_STR = "V楼市监测一条预警信息。【点击查看】：<a href='%s'>%s</a>";


    public static final Integer [] INTS = {-1,0,1};



    //user_Agent池
    public final static String[] userAgents = {
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.9.168 Version/11.50",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; Tablet PC 2.0; .NET4.0E)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; ) AppleWebKit/534.12 (KHTML, like Gecko) Maxthon/3.0 Safari/534.12",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.33 Safari/534.3 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
    };
}
