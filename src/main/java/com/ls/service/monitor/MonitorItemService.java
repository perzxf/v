package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorItem;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MonitorItemService extends IService<MonitorItem> {

    void saveMonitorItem(MonitorItem item, HttpSession session);

    List<MonitorItem> selectUserList(MonitorItem item, Integer page, Integer rows);

    Integer getCount(MonitorItem item);

    MonitorItem getMonitorItemById(Long monitorId);
}
