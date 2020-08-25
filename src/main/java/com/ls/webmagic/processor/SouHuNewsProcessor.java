package com.ls.webmagic.processor;

import cn.hutool.core.collection.CollUtil;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 解析页面
 * 实现PageProcessor,定义页面解析逻辑
 */
@Component
@Slf4j
public class SouHuNewsProcessor implements PageProcessor {

    private static final Logger log = LoggerFactory.getLogger(SouHuNewsProcessor.class);

    /**
     * 解析页面
     * @param page
     */
    @Override
    public void process(Page page) {
        //解析列表页
        List<Selectable> nodes = page.getHtml().css("div.results").nodes();

        if (CollUtil.isNotEmpty(nodes)) {
            //不为空表示这是数据详情页,解析页面,获取详情信息,保存数据
            try {
                this.saveNewsInfo(page);
            } catch (Exception e) {
                log.error("解析异常,异常原因:{}", e.getMessage(),e);
            }
        }
    }

    /**
     * 解析详情页
     * @param page
     */
    private void saveNewsInfo(Page page) throws ParseException {
        List<MonitorEvent> infoList = new ArrayList<>();
        //解析页面
        Html html = page.getHtml();
        List<Selectable> nodes = html.css("div.results div.vrwrap").nodes();
        for (Selectable node : nodes) {
            String title = node.xpath("//div[@class='news200616']/h3/a/allText()").toString();  //标题
            String content = node.xpath("//p[@class='star-wiki']/allText()").toString(); //内容
            String link = node.xpath("//div[@class='news200616']/h3/a/@href").toString(); //链接
            String worker = node.xpath("//p[@class='news-from']/span[1]/allText()").toString(); //作者
            String time = node.xpath("//p[@class='news-from']/span[2]/allText()").toString(); //发表时间的时间(2018年9月26日)

            MonitorEvent event = new MonitorEvent();
            event.setEventTitle(title); //标题
            event.setEventContent(content); //内容
            event.setEventUrl(link); //链接
            Date date = DateUtil.formatString(time, "yyyy年MM月dd日");
            event.setEventDate(date); //事件发生时间

            infoList.add(event);
        }

        //把结果保存到resultItems,为了持久化
        page.putField("souHuNewsInfoList", infoList);
    }

    //user_Agent池
    private static String[] userAgents = {
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

    //随机获取总数为14useragent池中的一个
    private String userAgent = userAgents[new Random().nextInt(15)];

    //配置爬虫信息
    private Site site = Site.me()
            .setUserAgent(this.userAgent)
            .setCharset("utf8")
            .setTimeOut(10 * 1000)
            .setRetryTimes(3)
            .setRetrySleepTime(3000);

    @Override
    public Site getSite() {
        return site;
    }

}
