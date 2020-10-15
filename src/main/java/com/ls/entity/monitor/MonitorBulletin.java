package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

@Data
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

}
