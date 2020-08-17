package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    /**
     *
     */
    List<SysUserRole> selectListByUserId(Long userId);
}
