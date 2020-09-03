package com.ls.spider;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ls.common.ConstantConfig;
import com.ls.common.RedisCacheKey;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysSite;
import com.ls.entity.system.SysUser;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.spider.SpiderMonitorService;
import com.ls.service.system.SysSiteService;
import com.ls.service.system.SysUserService;
import com.ls.utils.RedisUtil;
import com.ls.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/spider")
public class SpiderController {

    private static final Logger log = LoggerFactory.getLogger(SpiderController.class);

    /**
     * 页面有一个开启按钮，进来之后携带（项目ID）
     * 首先，关键词拆分,根据规定是逗号隔开,在代码中根据逗号分割，每一个关键词和项目名称都组成一个keys,
     * 接着遍历网站url,每一个URL都会拼接一个keys ,先统一存放在一个list里面或者存放在缓存中,
     * 然后，开始遍历爬取每一个网址带keys的URL,根据网站类型不一样,走不同的爬取方法,
     * 目前所有网站爬取的东西,只存放标题名、原文链接URL、原文发生时间、数据抓取时间、目前事件类型默认为0（情感分析暂时中性）、
     * 原文可存放的内容(存放不了的，存放标题)、处理状态(默认为0)、项目ID、网站类型
     */

    @Autowired
    private SysUserService userService;

    @Autowired
    private SpiderMonitorService spiderMonitorService;

    @Autowired
    private MonitorItemService itemService;

    @Autowired
    private SysSiteService siteService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/monitor")
    @ResponseBody
    public Map<String,Object> spiderMonitor(Long monitorId){
        Map<String, Object> map = new HashMap<>();

        String redisKey = RedisCacheKey.MONITOR.getType();
        redisUtil.set(redisKey,monitorId,30*60);

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
                map.put("success", true);
                map.put("msg", "开启监测成功，请刷新页面");
            }
        }else{
            map.put("success", false);
            map.put("msg", "网站为空");
        }

        return map;
    }


    /**
     * 定时开启监测   0 0 0/1 * * ?   每小时执行一次
     * 先获取项目list  查看每一个项目的创建人
     * 查看创建人是否在规则时间内 在时间内的可以操作项目
     *
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void sendCronStartMonitor(){
        log.info("开始执行定时监测任务: "+new Date().toLocaleString());
        //获取项目
        Wrapper<MonitorItem> wrapper = new EntityWrapper<>();
        List<MonitorItem> items = itemService.selectList(wrapper);
        for (MonitorItem item:items){
            //查看用户
            SysUser user = userService.getByUserId(item.getUserId());
            //判断用户的可实用性
            if (user.getIsOff() == ConstantConfig.ISOFF){  //没有被封禁
                if(user.getEndDate().getTime() >= new Date().getTime()){  //使用的最后时间要大于当前时间

                    String redisKey = RedisCacheKey.MONITOR.getType();
                    redisUtil.set(redisKey,item.getMonitorId(),30*60);

                    //遍历网站列表
                    List<SysSite> siteList = siteService.getSiteList();
                    if(siteList!=null && !siteList.isEmpty()){
                        for(SysSite site:siteList){
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
                            spiderMonitorService.spiderMonitor(site.getSiteTypeId(),site.getSiteUrl(),keyList);
                            log.info("{}:开启监测成功",item.getMonitorName());
                        }
                    }else {
                        log.info("网站信息为空");
                    }
                }else{
                    log.info("该用户服务时间已到期");
                    SysUser sysUser = new SysUser();
                    sysUser.setUserId(user.getUserId());
                    sysUser.setIsOff(ConstantConfig.NOOFF);
                    userService.updateById(sysUser);
                }
            }else{
                log.info("用户被封禁");
            }
        }
    }
}
