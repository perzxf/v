package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorBulletin;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

@Service
public interface MonitorBulletinService extends IService<MonitorBulletin> {

    void bulletinAdd(Long monitorId, Long eventId);

    List<MonitorBulletin> selectBulletinList(MonitorBulletin monitorBulletin, Integer page, Integer rows);

    Integer getCount(MonitorBulletin monitorBulletin);

    boolean booleanByBulletin(Long eventId);

    MonitorBulletin getbulletinById(long parseLong);

    File createBulletin(Map data ,Long monitorId);

}
