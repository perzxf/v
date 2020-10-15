package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

@Data
public class SysUserRole {

    /**
     * 主键ID
     */
    @TableId
    private Long    urId;

    /**
     * 用户ID（关联用户表）
     */
    private Long    userId;

    /**
     * 角色ID（关联角色表）
     */
    private Long    roleId;


}
