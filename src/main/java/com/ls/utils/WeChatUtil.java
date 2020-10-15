package com.ls.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ls.common.RedisCacheKey;
import com.ls.common.WeChatConfig;
import com.ls.entity.weChat.WeChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信工具类
 */
@Component
public class WeChatUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据appid和appsecret获取access_token
     */
    public String getAccessToken(String appid, String appsecret){
        String access_token = null;
        String accessTokenUrl = WeChatConfig.ACCESS_TOKEN_URL;
        try {
            /**
             * 先从redis中获取，没有在从httpclient获取
             * 因为获取的access_token有一个时间有效期
             */
            String redis_access_token = (String) redisUtil.get(RedisCacheKey.ACCESS_TOKEN.getType());
            if(StringUtil.isEmpty(redis_access_token)){  //为空
                //替换url的参数
                accessTokenUrl = accessTokenUrl.replaceAll("APPID",appid);
                accessTokenUrl = accessTokenUrl.replaceAll("SECRET",appsecret);
                //发送请求
                HttpClient httpClient = new HttpClient(accessTokenUrl);
                //发送GET请求
                httpClient.get();
                //获取请求结果 json格式的字符串   JSON转化map
                Map map = JSON.parseObject(httpClient.getContent(), Map.class);
                //解析Map
                access_token = (String) map.get("access_token");
                Integer expires_in = (Integer) map.get("expires_in");
                //同时把信息缓存到redis中
                redisUtil.set(RedisCacheKey.ACCESS_TOKEN.getType(),access_token,expires_in);
            }else{ //不为空
                access_token = redis_access_token;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 获取微信公众号关注者列表
     */
    public List<Object> getWeChatUsers(String accessToken){
        List<Object> list = new ArrayList<>();
        String users_url = WeChatConfig.GET_USERS;
        if(StringUtil.isNotEmpty(accessToken)){
            //替换url的参数
            users_url = users_url.replaceAll("ACCESS_TOKEN",accessToken);
            try {
                //发送请求
                HttpClient httpClient = new HttpClient(users_url);
                //发送GET请求
                httpClient.get();
                //获取请求结果 json格式的字符串   JSON转化map
                Map map = JSON.parseObject(httpClient.getContent(), Map.class);
                //解析Map
                Map data = (Map) map.get("data");
                List<Object> openids = (List) data.get("openid");
                /**
                 * 先获取redis里有没有openid的集合
                 * 如果有就获取到，然后遍历它，看看最新的openid是否在缓存里，在的话就不缓存， 不在就添加缓存list里
                 * 如果没有获取到openid的集合，就直接缓存这个集合
                 */
                List<Object> redisOpenids = redisUtil.lGet(RedisCacheKey.OPENID.getType(), 0, -1);
                if(redisOpenids!=null && !redisOpenids.isEmpty()){ //不为空
                    redisUtil.del(RedisCacheKey.OPENID.getType()); //先清空缓存，方面后面再重新缓存
                    for(Object openid : openids){
                        if( !redisOpenids.contains(openid)){ //不包含在list里
                            redisOpenids.add(openid);
                        }
                    }
                    redisUtil.lSet(RedisCacheKey.OPENID.getType(),redisOpenids);
                    list = redisOpenids;
                }else{ //为空
                    redisUtil.lSet(RedisCacheKey.OPENID.getType(),openids);
                    list = openids;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 获取用户个人信息 并存入数据库和redis 方便后期查看用户
     */
    public WeChatUserInfo getWChatUser(Object openid,String accessToken){
        WeChatUserInfo weChatUser = null;
        String unionidUrl = WeChatConfig.GET_UNIONID_URL;
        if(StringUtil.isEmpty(accessToken)){
            accessToken = this.getAccessToken(WeChatConfig.APPID, WeChatConfig.SECRET);
        }
        try {
            //替换url的参数
            unionidUrl = unionidUrl.replaceAll("ACCESS_TOKEN",accessToken);
            unionidUrl = unionidUrl.replaceAll("OPENID",String.valueOf(openid));
            //发送请求
            HttpClient httpClient = new HttpClient(unionidUrl);
            //发送GET请求
            httpClient.get();
            //获取请求结果 json格式的字符串   JSON转化对象
            weChatUser = JSON.parseObject(httpClient.getContent(), WeChatUserInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  weChatUser;
    }


    /**
     * 客服接口-发消息
     * 文本消息
     */
    public  Map sendText(String accessToken,String openid,String scene_str){
        Map<String, Object> map = new HashMap<>();

        String send_url = WeChatConfig.CUSTOM_SEND;
        if(StringUtil.isEmpty(accessToken)){
            accessToken = this.getAccessToken(WeChatConfig.APPID, WeChatConfig.SECRET);
        }
        try {
            //替换url的参数
            send_url = send_url.replaceAll("ACCESS_TOKEN",accessToken);
            //整理JSON
            Map<String,Object> strMap = new HashMap<>();
            strMap.put("touser",openid);
            strMap.put("msgtype","text");
            Map<String,Object> mapMap = new HashMap<>();
            mapMap.put("content",scene_str);
            strMap.put("text", mapMap);
            String data = new Gson().toJson(strMap);
            // 发送请求
            map = HttpClient.httpClientPost(send_url, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    /**
     *
     */
    /*public  Map sendAutoReplyText(String accessToken,String openid,String add_friend_str,String message_default_str){
        Map<String, Object> map = new HashMap<>();

        String autoreplyUrl = WeChatConfig.GET_CURRENT_AUTOREPLY_INFO;

        if(StringUtil.isEmpty(accessToken)){
            accessToken = this.getAccessToken(WeChatConfig.APPID, WeChatConfig.SECRET);
        }
        try {
            //替换url的参数
            autoreplyUrl = autoreplyUrl.replaceAll("ACCESS_TOKEN",accessToken);
            //整理JSON

            Map<String,Object> strMap = new HashMap<>();
            strMap.put("is_add_friend_reply_open",1);
            strMap.put("is_autoreply_open",1);

            Map<String,Object> add_friend_map = new HashMap<>();
            add_friend_map.put("type","text");
            add_friend_map.put("content",add_friend_str);
            strMap.put("add_friend_autoreply_info", add_friend_map);

            Map<String,Object> message_default_map = new HashMap<>();
            message_default_map.put("type","text");
            message_default_map.put("content",message_default_str);
            strMap.put("message_default_autoreply_info", message_default_map);

            String data = new Gson().toJson(strMap);

            // 发送请求
//            map = HttpClient.httpClientGet(autoreplyUrl, data);

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }*/

}
