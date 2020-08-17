package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuService extends IService<SysRoleMenu> {
    /**
     *
     */
    List<SysRoleMenu> selectListByRoleId(Long roleId);
}
