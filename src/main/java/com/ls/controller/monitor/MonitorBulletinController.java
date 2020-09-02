package com.ls.controller.monitor;

import com.ls.common.ConstantConfig;
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
import org.springframework.web.bind.annotation.RequestBody;
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
    public Map<String, Object> findAll(Integer page, Integer rows,Long monitorId){
        Map<String, Object> map = new HashMap<>();

        MonitorBulletin bulletin = new MonitorBulletin();
        bulletin.setMonitorId(monitorId);
        //当前页数据
        List<MonitorBulletin> lists = bulletinService.selectBulletinList(bulletin,page, rows);
        for(MonitorBulletin item:lists){
            MonitorEvent event = eventService.getEventById(item.getEventId());
            item.setEventTitle(event.getEventTitle());
        }
        //总条数
        Long totals = bulletinService.getCount(bulletin).longValue();
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
    public Map<String,Object> createBulletin(@RequestBody List<String> bulletinIds){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> data = new HashMap<>();

        String monitorName = "";

        int goodCount = 0;
        int badCount = 0;
        int otherCount = 0;

        List<String[]> cellgrids = new ArrayList<>();

        for(String bulletinId:bulletinIds){
            String[] cells = new String[3];

            MonitorBulletin bulletin = bulletinService.getbulletinById(Long.parseLong(bulletinId));
            //查询数据
            MonitorEvent monitorEvent = eventService.getEventById(bulletin.getEventId());
            Integer eventType = monitorEvent.getEventType();
            if(eventType == ConstantConfig.EVENT_TYPE_NEGATIVE){
                badCount ++;
            }else if(eventType == ConstantConfig.EVENT_TYPE_NEUTRAL){
                otherCount ++;
            }else if(eventType == ConstantConfig.EVENT_TYPE_POSITIVE){
                goodCount ++;
            }
            cells[1] = monitorEvent.getEventTitle();
            cells[2] = monitorEvent.getEventUrl();
            cells[3] = DateUtil.formatDate(monitorEvent.getEventDate(),"yyyy-MM-dd");
            cellgrids.add(cells);
            //查询项目
            MonitorItem monitorItem = itemService.getMonitorItemById(monitorEvent.getMonitorId());
            if(!monitorName.contains(monitorItem.getMonitorName())){
                monitorName.concat(monitorItem.getMonitorName());
            }
        }
        data.put("monitorName",monitorName);
        data.put("totalCount",bulletinIds.size());
        data.put("goodCount",goodCount);
        data.put("badCount",badCount);
        data.put("otherCount",otherCount);
        data.put("cellgrids",cellgrids);
        data.put("time", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
        File bulletin = bulletinService.createBulletin(data);

        map.put("success",true);
        map.put("msg","生成了");
        return map;
    }


    /**
     * 删除用户   逻辑删除 == 删除数据库
     * @param bulletinId
     * @return
     */
    @PostMapping("/monitor/bulletin/del")
    @ResponseBody
    public Map<String, Object> bulletinIdDel(Long bulletinId){
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> delMap = new HashMap<>();
        delMap.put("bulletin_id",bulletinId);
        boolean bool = bulletinService.deleteByMap(delMap);
        map.put("success",bool);
        return map;
    }

}
