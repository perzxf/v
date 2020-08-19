package com.ls.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemController {

    @GetMapping("/system/userList")
    public String toUserList(){
        return "system/userList";
    }

    @GetMapping("/system/website")
    public String toWebsiteList(){
        return "system/webList";
    }

    @GetMapping("/system/personalUser")
    public String toPersonalUser(){
        return "system/personalUser";
    }

    @GetMapping("/system/modifyPassword")
    public String toModifyPassword(){
        return "system/modifyPassword";
    }
}
