package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MonitorReport {

    /**
     * 报告ID（主键）
     */
    @TableId
    private Long    reportId;

    /**
     * 报告名称
     */
    private String  reportName;



    /**
     * 报告内容
     */
    private String  reportContent;

    /**
     * 项目ID
     */
    private String  monitorId;



    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;


    /**
     * 项目名称
     */
    private transient  String monitorName;

}
