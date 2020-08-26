package com.ls.controller.monitor;

import com.ls.entity.monitor.MonitorBulletin;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.service.monitor.MonitorBulletinService;
import com.ls.service.monitor.MonitorEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MonitorBulletinController {

    @Autowired
    private MonitorBulletinService bulletinService;

    @Autowired
    private MonitorEventService eventService;

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

}
