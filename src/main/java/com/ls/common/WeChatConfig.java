package com.ls.common;

/**
 * 微信配置信息
 */
public class WeChatConfig {
    /**
     * 第三方用户唯一凭证，即appid
     */
//    public static final String APPID = "wx65d348d36f0c0468";    //此信息为测试账户
    public static final String APPID = "wxc37f53825d6cce5d";    //此信息为正式账户

    /**
     * 第三方用户唯一凭证密钥，即appsecret
     */
//    public static final String SECRET = "f8e9afe2a3988c19903f6897c070f826";   //此信息为测试信息
    public static final String SECRET = "26e09374798401242271927100acc2c6";   //此信息为正式信息

    /**
     * 获取access_token
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

    /**
     * 公众号可通过本接口来获取帐号的关注者列表
     */
    public static final String GET_USERS = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";

    /**
     * 此接口用于获取用户个人信息 UnionID机制
     */
    public static final String GET_UNIONID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 客服接口-发消息
     */
    public static final String CUSTOM_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    /**
     * 获取公众号的自动回复规则
     */
    public static final String GET_CURRENT_AUTOREPLY_INFO = "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=ACCESS_TOKEN";
}
