package com.ls.service.system.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysMenu;
import com.ls.entity.system.SysRole;
import com.ls.entity.system.SysRoleMenu;
import com.ls.entity.system.SysUserRole;
import com.ls.mapper.system.SysMenuMapper;
import com.ls.service.system.SysMenuService;
import com.ls.service.system.SysRoleMenuService;
import com.ls.service.system.SysRoleService;
import com.ls.service.system.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

//    private static final Logger log = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @Override
    public SysMenu selectByMenuId(Long menuId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuId(menuId);
        return baseMapper.selectOne(sysMenu);
    }

    @Override
    public List<SysMenu> selectMenusByUser(Long userId) {
        List<SysMenu> menus = new LinkedList<>();
        // 根据用户ID显示对应的菜单信息
        List<SysRole> roles = getRoles(userId);
        for (SysRole role : roles){
            // 添加权限
            menus = getPermission(role.getRoleId());
        }
        return getChildPerms(menus, 0);
    }

    private List<SysRole> getRoles(Long userId) {
        List<SysUserRole> userRoles = userRoleService.selectListByUserId(userId);
        List<SysRole> list = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleService.selectByRoleId(userRole.getRoleId());
            list.add(role);
        }
        return list;
    }

    private List<SysMenu> getPermission(Long roleId) {
        List<SysRoleMenu> roleMenus = roleMenuService.selectListByRoleId(roleId);
        List<SysMenu> list = new ArrayList<>();
        for (SysRoleMenu roleMenu : roleMenus) {
            SysMenu menu = selectByMenuId(roleMenu.getMenuId());
            list.add(menu);
        }
        return list;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    private List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (StringUtils.equals(t.getParentId().toString(),String.valueOf(parentId))) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (StringUtils.equals(n.getParentId().toString() , t.getMenuId().toString())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
