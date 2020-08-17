package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysRole;

public interface SysRoleService extends IService<SysRole> {

    /**
     *
     */
    SysRole selectByRoleId(Long roleId);

}
