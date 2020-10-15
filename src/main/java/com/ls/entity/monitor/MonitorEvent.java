package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MonitorEvent {
    /**
     * 记录数据ID（主键）
     */
    @TableId
    private Long    eventId;

    /**
     * 事件标题
     */
    private String  eventTitle;

    /**
     * 事件链接（原文链接）
     */
    private String  eventUrl;

    /**
     * 事件数据来源类型ID
     */
    private Long siteTypeId;


    /**
     * 事件发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date   eventDate;

    /**
     * 事件内容
     */
    private String  eventContent;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 处理状态（0：未加入简报，1：已经加入简报 ）
     */
    private Integer state;

    /**
     * 事件类型（-1：负面，0：中性，1：正面）
     */
    private Integer eventType;

    /**
     * 项目ID
     */
    private Long monitorId;

    /**
     * 数据是否有用
     */
    private Integer isUse;


}
