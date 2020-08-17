package com.ls.service.system.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysRoleMenu;
import com.ls.mapper.system.SysRoleMenuMapper;
import com.ls.service.system.SysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Override
    public List<SysRoleMenu> selectListByRoleId(Long roleId) {
        Wrapper<SysRoleMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("role_id", roleId);
        return baseMapper.selectList(wrapper);
    }
}
