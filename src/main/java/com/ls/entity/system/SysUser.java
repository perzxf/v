package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SysUser {
    /**
     * 用户ID（主键）
     */
    @TableId
    private Long    userId;

    /**
     * 微信用户唯一标识（后期微信登录）
     */
    private String  openId;

    /**
     * 昵称
     */
    private String  nickName;

    /**
     * 用户名
     */
    private String  userName;

    /**
     * 密码
     */
    private String  password;

    /**
     * 用户头像
     */
    private String  headPortrait;

    /**
     * 手机号
     */
    private String  cellPhone;

    /**
     * 邮箱
     */
    private String  email;

    /**
     * 公司名称
     */
    private String  company;

    /**
     * 是否被封禁（1：未封禁，0：已封禁）
     */
    private Integer  isOff;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 使用开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    startDate;

    /**
     * 使用结束时间（新用户有3天使用时间，如需延长找管理员开通）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    endDate;

    /**
     * 用户备注
     */
    private String  remark;


    private transient String roleId;

}
