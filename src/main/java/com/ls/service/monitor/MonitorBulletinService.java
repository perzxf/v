package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorBulletin;

import java.util.List;

public interface MonitorBulletinService extends IService<MonitorBulletin> {

    void bulletinAdd(Long monitorId, Long eventId);

    List<MonitorBulletin> selectBulletinList(MonitorBulletin monitorBulletin, Integer page, Integer rows);

    Integer getCount(MonitorBulletin monitorBulletin);

    boolean booleanByBulletin(Long eventId);
}
