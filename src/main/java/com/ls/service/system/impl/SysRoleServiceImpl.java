package com.ls.service.system.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysRole;
import com.ls.mapper.system.SysRoleMapper;
import com.ls.service.system.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public SysRole selectByRoleId(Long roleId) {
        SysRole sysRole= new SysRole();
        sysRole.setRoleId(roleId);
        return baseMapper.selectOne(sysRole);
    }
}
