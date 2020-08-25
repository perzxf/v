package com.ls.service.spider.impl;

import com.ls.service.spider.SpiderMonitorService;
import com.ls.webmagic.MysqlPipeline;
import com.ls.webmagic.processor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.List;

@Service
public class SpiderMonitorServiceImpl implements SpiderMonitorService {

    private static final Logger log = LoggerFactory.getLogger(SpiderMonitorServiceImpl.class);

    /**
     *  根据网站类型ID,去区别应该去哪个类里面数据爬取
     *  网站类型（1：微博，2：贴吧，3：论坛，4：新闻，5：公众号，6：博客，7：自媒体，8：其他）
     */

    @Override
    public void spiderMonitor(Long siteTypeId, String siteUrl, List<String> keyList) {

        int  siteType = siteTypeId.intValue();
        for (String key:keyList){

            switch (siteType) {
                case 1:
                    System.out.println("微博");
                    break;
                case 2:
                    System.out.println("百度贴吧");
                    getTieBaInfo(siteUrl,key);
                    break;
                case 3:
                    System.out.println("天涯论坛");
                    getTianYaInfo(siteUrl,key);
                    break;
                case 4:
                    System.out.println("新浪新闻");
                    getSinaNewsInfo(siteUrl,key);
                    break;
                case 5:
                    System.out.println("搜狗微信公众号");
                    getWeChatInfo(siteUrl,key);
                    break;
                case 6:
                    System.out.println("博客");
                    break;
                case 7:
                    System.out.println("搜狗知乎");
                    getZhiHuInfo(siteUrl,key);
                    break;
                case 8:
                    System.out.println("搜狗新闻");
                    getSouHuNewsInfo(siteUrl,key);
                    break;
            }

        }

    }



    @Autowired
    private MysqlPipeline mysqlPipeline;

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

            log.info("开始爬取数据,{}",url);
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

            log.info("开始爬取数据,{}",url);
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

            log.info("开始爬取数据,{}",url);
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

            log.info("开始爬取数据,{}",url);
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

            log.info("开始爬取数据,{}",url);
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

            log.info("开始爬取数据,{}",url);
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
}
