package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SysSite {
    /**
     * 网站ID（主键）
     */
    @TableId
    private Long    siteId;

    /**
     * 网站名称
     */
    private String  siteName;

    /**
     * 网站网址
     */
    private String  siteUrl;

    /**
     * 网站类型ID
     */
    private Long   siteTypeId;

    /**
     * 网站备注信息
     */
    private String  remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getSiteTypeId() {
        return siteTypeId;
    }

    public void setSiteTypeId(Long siteTypeId) {
        this.siteTypeId = siteTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
