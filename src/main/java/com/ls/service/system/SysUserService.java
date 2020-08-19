package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查找用户
     */
    SysUser findByUserName(String userName);

    /**
     * 根据邮箱查找用户
     */
    SysUser findByEmail(String email);

    /**
     * 根据id获取用户信息
     */
    SysUser getByUserId(Long userId);

    /**
     * 新增用户
     */
    void saveUser(SysUser user);

    /**
     * 查询用户列表 带查询 带分页
     */
    List<SysUser> selectUserList(SysUser user, int page, int pageSize);

    /**
     * 查询用户总条数
     */
    Integer getCount(SysUser user);


    void updateByUser(SysUser oldUser);
}
