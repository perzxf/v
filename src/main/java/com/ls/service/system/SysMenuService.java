package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     *
     */
    SysMenu selectByMenuId(Long menuId);

    /**
     *
     */
    List<SysMenu> selectMenusByUser(Long userId);


}
