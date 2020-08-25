package com.ls.spider;

import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysSite;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.spider.SpiderMonitorService;
import com.ls.service.system.SysSiteService;
import com.ls.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spider")
public class SpiderController {

    /**
     * 页面有一个开启按钮，进来之后携带（项目ID）
     * 首先，关键词拆分,根据规定是逗号隔开,在代码中根据逗号分割，每一个关键词和项目名称都组成一个keys,
     * 接着遍历网站url,每一个URL都会拼接一个keys ,先统一存放在一个list里面或者存放在缓存中,
     * 然后，开始遍历爬取每一个网址带keys的URL,根据网站类型不一样,走不同的爬取方法,
     * 目前所有网站爬取的东西,只存放标题名、原文链接URL、原文发生时间、数据抓取时间、目前事件类型默认为0（情感分析暂时中性）、
     * 原文可存放的内容(存放不了的，存放标题)、处理状态(默认为0)、项目ID、网站类型
     */

    @Autowired
    private SpiderMonitorService spiderMonitorService;

    @Autowired
    private MonitorItemService itemService;

    @Autowired
    private SysSiteService siteService;

    @PostMapping("/monitor")
    @ResponseBody
    public Map<String,Object> spiderMonitor(Long monitorId){
        Map<String, Object> map = new HashMap<>();

        //根据项目ID读取项目数据
        MonitorItem item = itemService.getMonitorItemById(monitorId);
        //遍历网站列表
        List<SysSite> siteList = siteService.getSiteList();
        if(siteList!=null && !siteList.isEmpty()){ //不为空
            for(SysSite site:siteList){
                Long siteTypeId = site.getSiteTypeId();
                String siteUrl = site.getSiteUrl();
                //拆分关键词
                List<String> keyList = new ArrayList<>();
                if(StringUtil.isNotEmpty(item.getKeywords())){
                    String[] keys = item.getKeywords().split(",");
                    keyList.add(item.getMonitorName());
                    for(String word:keys){
                        //项目名和关键词的组合
                        String key = item.getMonitorName() + word;
                        keyList.add(key);
                    }
                }
                //根据网站和关键词监测项目
                spiderMonitorService.spiderMonitor(siteTypeId,siteUrl,keyList);
            }
        }

        return map;
    }

}
