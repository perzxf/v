package com.ls.webmagic.processor;

import com.alibaba.fastjson.JSON;
import com.ls.common.ConstantConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: TODO
 * @author: zcf
 * @date: 2020/10/15 14:14
 * @version: v1.0
 */
@Component
@Slf4j
public class LiuYanProcessor implements PageProcessor {

    /**
     * 实现爬取一个领导的留言板中的所有留言数据
     * 编码思路：
     * 通过page对象获取到请求的requestBody参数，给requestBody参数
     * 的lastItem替换新值，形成新的Url,添加到待爬取链接，
     * @param page
     */
    @Override
    public void process(Page page) {

        //获取page对象中的json的属性的值并解析responseData的全部数据形成列表
        //List<String> messageJsonList = page.getJson().jsonPath("$.responseData[*]").all();

        //通过json路径获取page对象json属性的responseData的最后一条数据并解析出tid参数的值
        String lastItem = JSON.parseObject(page.getJson().jsonPath("$.responseData[9]").get()).get("tid").toString();

        //通过page对象，获取到请求体的值，该值经过编码是字节数组，原值是fid=1029&lastItem=0所以这里采用转换类型的方式把字节数组转换为原始字符，
        String requestParameter = new String(page.getRequest().getRequestBody().getBody(), StandardCharsets.UTF_8);

        //使用正则把请求参数requestParameter中的fid=1029&lastItem=提取出来，用作拼接下一条请求的请求参数的固定部分
        Matcher m = Pattern.compile("(\\w+)=(\\d+)&(\\w+=)").matcher(requestParameter);

        //如果匹配出的不是空值，取出该值与lastItem拼接成下一条请求的请求参数
        if(m.find(0)){
            requestParameter = m.group(0)+lastItem;

            //把请求参数转换为字节数组，并放到请求体中备用
            page.getRequest().getRequestBody().setBody(requestParameter.getBytes(StandardCharsets.UTF_8));

        }
        List<String> tidList = page.getJson().jsonPath("$.responseData[*].tid").all();

        page.putField("tid", tidList);
        page.putField("subject",page.getJson().jsonPath("$.responseData[*].subject").all());
        page.putField("stateInfo",page.getJson().jsonPath("$.responseData[*].stateInfo").all());
        page.putField("content",this.getContent(tidList));
        //page.putField("sourceName",page.getJson().jsonPath("$.responseData[*].sourceName").all());
        //page.putField("from",page.getJson().jsonPath("$.responseData[*].from").all());
        page.putField("threadsCheckTime",page.getJson().jsonPath("$.responseData[*].threadsCheckTime").all());

        //获取修改后的请求，并加入到请求列表
        page.addTargetRequest(page.getRequest());

    }

    public List<String> getContent(List<String> tidList) {

        //Map<String,Object> map = new HashMap<>();

        List<String> content = new ArrayList<>();

        //创建http客户端对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Page page = new Page();
        //创建httpGet对象
        HttpGet httpGet = new HttpGet();
        for(String tid : tidList){

            try{
                Thread.sleep(1000*2);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = "http://liuyan.people.com.cn/threads/content?tid=" + tid;

            //设置网址
            httpGet.setURI(URI.create(url));
            try {
                //使用http客户端向url发送请求并获取响应，从响应中获取实体，使用实体工具转换为字符串
                String resContent = EntityUtils.toString(httpClient.execute(httpGet).getEntity(),"UTF-8");
                //把文本字符串转换成html，并生成page对象，得到page对象才能使用解析方法
                page.setHtml(Html.create(resContent));
                //解析提取page对象的内容
                content.add(page.getHtml().xpath("//p[@class='zoom content']/text()").get());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
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
