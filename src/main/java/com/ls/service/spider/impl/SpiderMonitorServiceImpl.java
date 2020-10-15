package com.ls.service.spider.impl;

import com.ls.service.spider.SpiderMonitorService;
import com.ls.webmagic.MysqlPipeline;
import com.ls.webmagic.processor.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpiderMonitorServiceImpl implements SpiderMonitorService {

    /**
     *  根据网站类型ID,去区别应该去哪个类里面数据爬取
     *  网站类型（[1,'新闻网站', 2,'微博', 3,'贴吧', 4,'论坛', 5,'今日头条',6,'知乎',7,'微信公众号',8,'其他']）
     */

    @Override
    public void spiderMonitor(Long siteTypeId, String siteUrl, List<String> keyList) {

        int  siteType = siteTypeId.intValue();

        for (String key:keyList){

            switch (siteType) {
                case 1:
                    log.info("新闻网站类型");
                    getSinaNewsInfo(siteUrl,key);  //新浪新闻
                    getSouHuNewsInfo(siteUrl,key); //搜狐新闻
                    break;
                case 2:
                    log.info("微博类型");
                    break;
                case 3:
                    log.info("贴吧类型");
                    getTieBaInfo(siteUrl,key);
                    break;
                case 4:
                    log.info("论坛类型");
                    getTianYaInfo(siteUrl,key);
                    break;
                case 5:
                    log.info("今日头条类型");
                    getTouTiaoInfo(siteUrl,key);
                    break;
                case 6:
                    log.info("知乎类型");
                    getZhiHuInfo(siteUrl,key);
                    break;
                case 7:
                    log.info("微信公众号类型");
                    getWeChatInfo(siteUrl,key);
                    break;
                case 8:
                    log.info("其他类型");
                    break;
            }

        }

    }


    @Autowired
    private MysqlPipeline mysqlPipeline;

    @Autowired
    private LiuYanProcessor liuYanProcessor;

    @Override
    public void getLiuYanInfo() {
        Map<String,Object> map = new HashMap<>();
        map.put("fid",1029);
        map.put("lastItem",0);
        Request request = new Request();
        request.setMethod(HttpConstant.Method.POST);
        request.addHeader("Referer","http://liuyan.people.com.cn/threads/list?fid=1029");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //构造map用于第一条请求的请求体
        request.setRequestBody(HttpRequestBody.form(map,"utf-8"));
        request.setUrl("http://liuyan.people.com.cn/threads/queryThreadsList");
        log.info("开始爬取政府留言板数据");
        Spider.create(liuYanProcessor)
                .addRequest(request)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(50)
                .addPipeline(mysqlPipeline)
                .run();
    }

    @Autowired
    private WeChatProcessor weChatProcessor;

    /**
     * 搜狐微信公众号
     * @param siteUrl
     * @param key
     */
    private void getWeChatInfo(String siteUrl, String key) {
        //开始爬取的url
        siteUrl = "https://weixin.sogou.com/weixin?type=2&query="+key;
        for (int i = 1; i <= 10; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&page=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取微信公众号数据,{}",url);
            //设置爬虫配置
            Spider.create(weChatProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private SouHuNewsProcessor souHuNewsProcessor;

    /**
     * 搜狐新闻
     * @param siteUrl
     * @param key
     */
    private void getSouHuNewsInfo(String siteUrl, String key){
        siteUrl = "https://www.sogou.com/sogou?ie=utf8&p=40230447&interation=1728053249&interV=&pid=sogou-wsse-8f646834ef1adefa&query="+key;
        for (int i = 1; i <= 10; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&page=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取搜狐新闻数据,{}",url);
            //设置爬虫配置
            Spider.create(souHuNewsProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private ZhiHuProcessor zhiHuProcessor;

    /**
     * 搜狐知乎
     * @param siteUrl
     * @param key
     */
    private void getZhiHuInfo(String siteUrl, String key) {
        siteUrl = "https://www.sogou.com/sogou?insite=zhihu.com&ie=utf8&query="+key;
        for (int i = 1; i <= 10; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&page=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取知乎数据,{}",url);
            //设置爬虫配置
            Spider.create(zhiHuProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private TieBaProcessor tieBaProcessor;

    /**
     * 百度贴吧
     * @param siteUrl
     * @param key
     */
    private void getTieBaInfo(String siteUrl, String key) {
        siteUrl = "https://tieba.baidu.com/f/search/res?ie=utf-8&qw="+key;
        for (int i = 1; i <= 10; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&pn=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取百度贴吧数据,{}",url);
            //设置爬虫配置
            Spider.create(tieBaProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private TianYaProcessor tianYaProcessor;

    /**
     * 天涯论坛
     * @param siteUrl
     * @param key
     */
    private void getTianYaInfo(String siteUrl, String key) {
        siteUrl = "https://search.tianya.cn/bbs?s=4&f=0&q="+key;
        for (int i = 1; i <= 9; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&pn=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取天涯论坛数据,{}",url);
            //设置爬虫配置
            Spider.create(tianYaProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private SinaNewsProcessor sinaNewsProcessor;

    /**
     * 新浪新闻
     * @param siteUrl
     * @param key
     */
    private void getSinaNewsInfo(String siteUrl, String key) {
        siteUrl = "https://search.sina.com.cn/?c=news&q="+key;
        for (int i = 1; i <= 10; i++) {
            StringBuilder builder = new StringBuilder(siteUrl);
            builder.append("&page=");
            builder.append(i);
            String url = builder.toString();

            log.info("开始爬取新浪新闻数据,{}",url);
            //设置爬虫配置
            Spider.create(sinaNewsProcessor)
                    .addUrl(url) //设置初始爬取的url
                    //使用布隆过滤器过滤重复url,需要引入guava包
                    .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                    .thread(50) //设置线程数
                    .addPipeline(mysqlPipeline) //设置持久化
                    .run();

        }
    }

    @Autowired
    private TouTiaoProcessor touTiaoProcessor;

    /**
     * 今日头条
     * @param siteUrl
     * @param key
     */
    private void getTouTiaoInfo(String siteUrl, String key) {
        siteUrl = "https://www.toutiao.com/search/?keyword="+key;
        log.info("开始爬取今日头条数据,{}",siteUrl);
        //设置爬虫配置
        Spider.create(touTiaoProcessor)
                .addUrl(siteUrl) //设置初始爬取的url
                //使用布隆过滤器过滤重复url,需要引入guava包
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(50) //设置线程数
                .addPipeline(mysqlPipeline) //设置持久化
                .run();
    }
}
