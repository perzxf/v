package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
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

}
