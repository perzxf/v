package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MonitorItem {
    /**
     * 项目ID（主键）
     */
    @TableId
    private Long    monitorId;

    /**
     * 项目名称
     */
    private String  monitorName;

    /**
     * 关键词
     */
    private String  keywords;

    /**
     * 用户ID
     */
    private Long   userId;

    /**
     * 备注信息
     */
    private String  remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;


    private transient String userName;

}
