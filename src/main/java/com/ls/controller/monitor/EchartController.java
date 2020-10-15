package com.ls.controller.monitor;

import com.ls.common.SiteTypeEnum;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.entity.system.SysSite;
import com.ls.service.monitor.MonitorEventService;
import com.ls.service.system.SysSiteService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 图表分析
 * @author: zcf
 * @date: 2020/8/26 21:35
 * @version: v1.0
 */
@Controller
@Slf4j
public class EchartController {

//    private static final Logger log = LoggerFactory.getLogger(EchartController.class);

    /**
     * 折线图与柱状图：x轴 时间  ； y轴 数量    事件   正负面
     * 饼状图   数据来源分布
     * 云词    关键词，项目名，数据来源名
     */

    @Autowired
    private MonitorEventService eventService;

    @Autowired
    private SysSiteService siteService;

    /**
     * 获取柱状图的日期数据和每一个日期对应的数量
     */
     @GetMapping("/chart/histogram")
     @ResponseBody
     public Map<String,Object> histogramEcharts(Long monitorId){

          Map<String,Object> map = new HashMap<>();
//          Long monitorId = 2l;
          List<MonitorEvent> events = eventService.getEventList(monitorId);
         if(events!=null && !events.isEmpty()){
             List<String>  dateList  = new ArrayList<>();
             List<Integer> countList = new ArrayList<>();
             for(MonitorEvent event:events){
                 Date eventDate = event.getEventDate();
                 if(eventDate != null){
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                     dateList.add(sdf.format(eventDate));
                     Integer count = eventService.getEventCount(sdf.format(eventDate));
                     countList.add(count);
                 }
             }

             map.put("success",true);
             map.put("dateList",dateList);   //时间
             map.put("countList",countList); //时间的事件个数
             map.put("msg","获取数据成功");
         }else{
             map.put("success",false);
             map.put("msg","获取数据失败");
         }

          return map;
     }

    /**
     * 饼状图
     */
    @GetMapping("/chart/pie")
    @ResponseBody
    public Map<String,Object> pieEcharts(){
        Map<String,Object> map = new HashMap<>();
        Long monitorId = null;
        List<MonitorEvent> events = eventService.getEventList(monitorId);
        if(events!=null && !events.isEmpty()){
            List<Map<String, Object>>  countList  = new ArrayList<>();
            List<SysSite> siteList = siteService.getSiteListUniq();
            for(SysSite site:siteList){
                Map<String,Object> countMap = new HashMap<>();
                Integer count = eventService.getPieEventCount(monitorId,site.getSiteTypeId());
                String siteExpire = SiteTypeEnum.getSiteExpire(site.getSiteTypeId());
                log.info("网站类型={},数量={}",site.getSiteName(),count);
                countMap.put(siteExpire,count);
                countList.add(countMap);
            }

            map.put("success",true);
            map.put("countList",countList);
            map.put("msg","加入简报成功");
        }else{
            map.put("success",false);
            map.put("msg","获取数据失败");
        }
        return map;
    }


}
