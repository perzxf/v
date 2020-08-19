package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorItem;

import javax.servlet.http.HttpSession;

public interface MonitorItemService extends IService<MonitorItem> {

    void saveMonitorItem(MonitorItem item, HttpSession session);
}
