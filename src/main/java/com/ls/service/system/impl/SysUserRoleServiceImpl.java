package com.ls.service.system.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysUserRole;
import com.ls.mapper.system.SysUserRoleMapper;
import com.ls.service.system.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Override
    public List<SysUserRole> selectListByUserId(Long userId) {
        Wrapper<SysUserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void saveUserRole(Long userId, String roleId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(Long.valueOf(roleId));
        baseMapper.insert(userRole);
        log.info("保存的用户角色信息：{}",userRole.toString());
    }
}
