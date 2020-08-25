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

@Component
@Slf4j
public class TieBaProcessor implements PageProcessor {

    private static final Logger log = LoggerFactory.getLogger(TieBaProcessor.class);

    @Override
    public void process(Page page) {
        //解析列表页
        List<Selectable> nodes = page.getHtml().css("div.s_post_list div.s_post").nodes();

        if (CollUtil.isNotEmpty(nodes)) {
            //不为空表示这是数据详情页,解析页面,获取详情信息,保存数据
            try {
                this.saveTieBaInfo(page);
            } catch (Exception e) {
                log.error("解析异常,异常原因:{}", e.getMessage(),e);
            }
        }
    }

    private void saveTieBaInfo(Page page) throws ParseException {
        List<MonitorEvent> tieBaInfoList = new ArrayList<>();
        //解析页面
        Html html = page.getHtml();
        List<Selectable> nodes = html.css("div.s_post_list div.s_post").nodes();
        for (Selectable node : nodes) {
            String title = node.xpath("//div/span[@class='p_title']/a/allText()").toString();  //标题
            String content = node.xpath("//div[@class='p_content']/allText()").toString(); //内容
            String link = node.xpath("//div/span[@class='p_title']/a/@href").toString(); //链接
//            String tieBaName = node.xpath("//div/a[1]/font/allText()").toString();  //贴吧名称
//            String worker = node.xpath("//div/a[2]/font/allText()").toString(); //作者
            String time = node.xpath("//div/font/allText()").toString(); //发表的时间

            MonitorEvent event = new MonitorEvent();
            event.setEventTitle(title); //标题
            event.setEventContent(content); //内容
            event.setEventUrl(link); //链接
            Date date = DateUtil.formatString(time, "yyyy-MM-dd HH:mm");
            event.setEventDate(date); //事件发生时间

            tieBaInfoList.add(event);
        }

        //把结果保存到resultItems,为了持久化
        page.putField("tieBaInfoList", tieBaInfoList);
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
            .setCharset("gbk")
            .setTimeOut(10 * 1000)
            .setRetryTimes(3)
            .setRetrySleepTime(3000);

    @Override
    public Site getSite() {
        return site;
    }
}