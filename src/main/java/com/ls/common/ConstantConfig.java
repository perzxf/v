package com.ls.common;

/**
 * 常量表
 */
public class ConstantConfig {
    /**
     * 盐
     */
    public final static String SALT = "loushi";

    /**
     * 是否被封禁（1：未封禁，0：已封禁）
     */
    public final static Integer ISOFF = 1;
    public final static Integer NOOFF = 0;

    /**
     * 使用结束时间（新用户有3天使用时间，如需延长找管理员开通）
     */
    public final static Integer DAY = 3;

    /**
     * 当前用户名称
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 重置密码
     * 初始密码
     */
    public static final String RESET_PASSWORD = "000000";


    public final static Integer EVENT_STATE = 0; //未加入简报

    public final static Integer EVENT_TYPE_NEGATIVE = -1; //事件类型 负面
    public final static Integer EVENT_TYPE_NEUTRAL = 0; //事件类型 中性
    public final static Integer EVENT_TYPE_POSITIVE = 1; //事件类型 正面
}
