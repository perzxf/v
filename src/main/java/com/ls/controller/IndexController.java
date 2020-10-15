package com.ls.controller;

import com.ls.common.ConstantConfig;
import com.ls.entity.system.SysMenu;
import com.ls.entity.system.SysUser;
import com.ls.service.system.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class IndexController {

    @Autowired
    private SysMenuService menuService;

    @GetMapping("index")
    public String toIndex(HttpSession session, Model model){
        SysUser currentUser = (SysUser) session.getAttribute(ConstantConfig.CURRENT_USER);
        List<SysMenu> sysMenus = menuService.selectMenusByUser(currentUser.getUserId());
        model.addAttribute("menus",sysMenus);
        return "index";
    }



}
