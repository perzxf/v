package com.ls.webmagic.processor;

import cn.hutool.core.collection.CollUtil;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorEvent;
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
import java.util.List;
import java.util.Random;

/**
 * 解析页面
 * 实现PageProcessor,定义页面解析逻辑
 */
@Component
@Slf4j
public class TouTiaoProcessor implements PageProcessor {

    private static final Logger log = LoggerFactory.getLogger(TouTiaoProcessor.class);

    /**
     * 解析页面
     * @param page
     */
    @Override
    public void process(Page page) {
        //解析列表页
        List<Selectable> nodes = page.getHtml().css("div.sections div.articleCard").nodes();

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
        List<Selectable> nodes = html.css("div.sections div.articleCard").nodes();
        for (Selectable node : nodes) {
            String title = node.xpath("//div[@class='title-box']/a/span/allText()").toString();  //标题
//            String content = node.xpath("//div[@class='r-info']/p/allText()").toString(); //内容
            String link = node.xpath("//div[@class='title-box']/a/@href").toString(); //链接(https://www.toutiao.com/group/6625775821661405710/)
//            String time = node.xpath("//div[@class='box-result']/h2/span/allText()").toString(); //发表时间的时间(yyyy-MM-dd HH:mm:ss)

            MonitorEvent event = new MonitorEvent();
            event.setEventTitle(title); //标题
//            event.setEventContent(content); //内容
            link = "https://www.toutiao.com"+link;
            event.setEventUrl(link); //链接
//            int index=time.indexOf(" ");
//            String result=time.substring(index+1);
//            Date date = DateUtil.formatString(result, "yyyy-MM-dd HH:mm:ss");
//            event.setEventDate(date); //事件发生时间

            infoList.add(event);
        }

        //再次根据list中的url去获取文章内容跟文章时间
//        List<MonitorEvent> list = spiderEventUrl(infoList);

        //把结果保存到resultItems,为了持久化
        page.putField("touTiaoInfoList", infoList);
    }

    //随机获取总数为14useragent池中的一个
    private String userAgent = ConstantConfig.userAgents[new Random().nextInt(15)];

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
