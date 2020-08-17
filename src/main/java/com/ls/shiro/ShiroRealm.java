package com.ls.shiro;

import com.ls.entity.system.*;
import com.ls.service.system.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ShiroRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysMenuService menuService;


    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // 获取用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 添加角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取用户信息
        SysUser user = userService.findByUserName(userName);

        List<SysRole> roles = getRoles(user.getUserId());
        for (SysRole role : roles){
            // 添加角色
            info.addRole(role.getRoleName());

            // 添加权限
            List<SysMenu> menus = getPermission(role.getRoleId());
            for (SysMenu menu : menus) {
                info.addStringPermission(menu.getMenuName());
            }
        }
        return info;
    }

    /**
     * 权限认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        // 获取用户名
        String userName = authenticationToken.getPrincipal().toString();
        //验证数据库里的用户名
        SysUser user = userService.findByUserName(userName);

        if (user == null) {
            throw new IncorrectCredentialsException("登录失败，用户名或密码错误");
        }else{
            //验证 authenticationToken 和 simpleAuthenticationInfo 的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(), getName());
            // 返回一个身份信息
            return simpleAuthenticationInfo;
        }
    }


    private List<SysRole> getRoles(Long userId){
        List<SysUserRole> userRoles = userRoleService.selectListByUserId(userId);
        List<SysRole> list = new ArrayList<>();
        for (SysUserRole userRole : userRoles){
            SysRole role = roleService.selectByRoleId(userRole.getRoleId());
            list.add(role);
        }
        return list;
    }

    private List<SysMenu> getPermission(Long roleId){
        List<SysRoleMenu> roleMenus = roleMenuService.selectListByRoleId(roleId);
        List<SysMenu> list = new ArrayList<>();
        for (SysRoleMenu roleMenu : roleMenus){
            SysMenu menu = menuService.selectByMenuId(roleMenu.getMenuId());
            list.add(menu);
        }
        return list;
    }
}
