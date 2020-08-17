package com.ls.service.system.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysUserRole;
import com.ls.mapper.system.SysUserRoleMapper;
import com.ls.service.system.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Override
    public List<SysUserRole> selectListByUserId(Long userId) {
        Wrapper<SysUserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }
}
