package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorEvent;

import java.util.List;

public interface MonitorEventService extends IService<MonitorEvent> {
    
    List<MonitorEvent> selectEventList(MonitorEvent monitorEvent, Integer page, Integer rows);

    Integer getCount(MonitorEvent monitorEvent);

    void saveEvent(List<MonitorEvent> events);
}
