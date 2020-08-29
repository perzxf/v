package com.ls.controller;

import com.ls.common.ConstantConfig;
import com.ls.entity.system.SysUser;
import com.ls.service.system.SysUserService;
import com.ls.utils.Md5Util;
import com.ls.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private SysUserService userService;

    /**
     * 进入登录页面
     * @return
     */
    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/")
    public String login(){
        return "login";
    }

    /**
     * 接收登录信息，验证登录信息，返回结果
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Map<String,Object> login(String username,String password, HttpSession session){
        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isEmpty(username)){
            map.put("success",false);
            map.put("msg","请输入用户名！");
        }else if(StringUtil.isEmpty(password)){
            map.put("success",false);
            map.put("msg","请输入密码！");
        }else{
            Subject subject = SecurityUtils.getSubject();
            //加密后的密码
            String pwd = Md5Util.md5(password, ConstantConfig.SALT);
            UsernamePasswordToken token = new UsernamePasswordToken(username,pwd);
            try {
                subject.login(token);       //登录验证
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                SysUser currentUser = userService.findByUserName(userName);
                if (currentUser.getIsOff() == ConstantConfig.ISOFF) {
                    Date endDate = currentUser.getEndDate();
                    if(endDate.getTime() >= new Date().getTime()){
                        session.setAttribute(ConstantConfig.CURRENT_USER,currentUser);
                        map.put("success", true);
                        map.put("msg", "登陆成功");
                    }else{
                        SysUser sysUser = new SysUser();
                        sysUser.setUserId(currentUser.getUserId());
                        sysUser.setIsOff(ConstantConfig.NOOFF);
                        userService.updateById(sysUser);
                        map.put("success", false);
                        map.put("msg", "该用户服务时间已到期，如有需要请联系管理员！");
                        subject.logout();
                    }
                } else {
                    map.put("success", false);
                    map.put("msg", "该用户已封禁，请联系管理员！");
                    subject.logout();
                }
            }catch (Exception e){
                e.printStackTrace();
                map.put("success", false);
                map.put("msg", "用户名或密码错误！");
            }
        }
        return map;
    }


    /**
     * 安全退出
     * @return
     */
    @GetMapping("/exitLogin")
    public String exitLogin(){
        SecurityUtils.getSubject().logout();
        return "redirect:login.html";
    }
}
