package com.ls.entity.monitor;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
     * 报告内容
     */
    private String reportContent;

    /**
     * 项目名称
     */
    private transient  String monitorName;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    @Override
    public String toString() {
        return "MonitorReport{" +
                "reportId=" + reportId +
                ", reportName='" + reportName + '\'' +
                ", monitorId='" + monitorId + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", reportContent='" + reportContent + '\'' +
                ", monitorName='" + monitorName + '\'' +
                '}';
    }
}
