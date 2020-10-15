package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

@Data
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


}
