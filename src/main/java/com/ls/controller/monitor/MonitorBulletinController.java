package com.ls.controller.monitor;

import com.ls.entity.monitor.MonitorBulletin;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.entity.monitor.MonitorItem;
import com.ls.service.monitor.MonitorBulletinService;
import com.ls.service.monitor.MonitorEventService;
import com.ls.service.monitor.MonitorItemService;
import com.ls.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

@Controller
public class MonitorBulletinController {

    @Autowired
    private MonitorBulletinService bulletinService;

    @Autowired
    private MonitorEventService eventService;

    @Autowired
    private MonitorItemService itemService;

    @PostMapping("/monitor/bulletin/add")
    @ResponseBody
    public Map<String,Object> bulletinAdd(Long monitorId,Long eventId){
        Map<String, Object> map = new HashMap<>();

        //检查是否有加入简报过
        if(bulletinService.booleanByBulletin(eventId)){
            //加入简报数据
            bulletinService.bulletinAdd( monitorId, eventId);
            //修改数据的处理状态
            eventService.updateEvent(eventId,1);
            map.put("success",true);
            map.put("msg","加入简报成功");
        }else{
            map.put("success",false);
            map.put("msg","之前已经加入过简报了！");
        }

        return map;
    }


    @GetMapping("/monitor/bulletin/findAll")
    @ResponseBody
    public Map<String, Object> findAll(Integer page, Integer rows){
        Map<String, Object> map = new HashMap<>();

        //当前页数据
        List<MonitorBulletin> lists = bulletinService.selectBulletinList(new MonitorBulletin(),page, rows);
        for(MonitorBulletin item:lists){
            MonitorEvent event = eventService.getEventById(item.getEventId());
            item.setEventTitle(event.getEventTitle());
        }
        //总条数
        Long totals = bulletinService.getCount(new MonitorBulletin()).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }

    @PostMapping("/monitor/bulletin/create")
    @ResponseBody
    public File createBulletin(Long monitorId,Long eventId){

        Map<String,Object> data = new HashMap<>();

        //查询数据
        MonitorEvent monitorEvent = eventService.getEventById(eventId);
        //查询项目
        MonitorItem monitorItem = itemService.getMonitorItemById(monitorId);

        data.put("monitorName",monitorItem.getMonitorName());
        data.put("totalCount",1);
        data.put("goodCount",0);
        data.put("badCount",0);
        data.put("otherCount",1);
        List<String[]> cellgrids = new ArrayList<>();
        cellgrids.add(new String[]{monitorEvent.getEventTitle(),monitorEvent.getEventUrl(),DateUtil.formatDate(monitorEvent.getEventDate(),"yyyy-MM-dd")});
        data.put("cellgrids",cellgrids);
        data.put("time", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
        File bulletin = bulletinService.createBulletin(data);

        return bulletin;
    }

}
