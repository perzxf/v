package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;

public class MonitorBulletin {

    /**
     * 简报ID
     */
    @TableId
    private Long bulletinId;

    /**
     * 项目ID
     */
    private Long monitorId;

    /**
     * 项目数据ID
     */
    private Long eventId;

    private transient  String  eventTitle;

    public Long getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Long bulletinId) {
        this.bulletinId = bulletinId;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
