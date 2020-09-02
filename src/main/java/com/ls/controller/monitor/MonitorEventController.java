package com.ls.controller.monitor;

import com.ls.entity.monitor.MonitorEvent;
import com.ls.service.monitor.MonitorEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/monitor")
public class MonitorEventController {

    @Autowired
    private MonitorEventService eventService;

    @GetMapping("/event/findAll")
    @ResponseBody
    public Map<String, Object> findAll(Long monitorId,Integer page, Integer rows){
        Map<String, Object> map = new HashMap<>();

        MonitorEvent monitorEvent = new MonitorEvent();
        monitorEvent.setMonitorId(monitorId);

        //当前页数据
        List<MonitorEvent> lists = eventService.selectEventList(monitorEvent,page, rows);
        //总条数
        Long totals = eventService.getCount(monitorEvent).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }


}
