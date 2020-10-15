package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

@Data
public class SysRoleMenu {

    /**
     * 主键ID
     */
    @TableId
    private Long    rmId;

    /**
     * 角色ID（关联角色表）
     */
    private Long    roleId;

    /**
     * 菜单ID（关联菜单权限表）
     */
    private Long    menuId;

}
