package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;

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

    public Long getRmId() {
        return rmId;
    }

    public void setRmId(Long rmId) {
        this.rmId = rmId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
