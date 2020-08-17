package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;

public class SysRole {

    /**
     * 角色ID（主键）
     */
    @TableId
    private Long    roleId;

    /**
     * 角色名称
     */
    private String  roleName;

    /**
     * 角色备注
     */
    private String  remark;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
