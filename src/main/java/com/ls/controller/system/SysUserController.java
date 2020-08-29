package com.ls.controller.system;

import com.ls.common.ConstantConfig;
import com.ls.entity.system.SysUser;
import com.ls.service.system.SysUserService;
import com.ls.utils.Md5Util;
import com.ls.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param session
     * @return
     */
    @PostMapping("/modifyPassword")
    @ResponseBody
    public Map<String,Object> modifyPassword(String oldPassword, String newPassword, HttpSession session){
        Map<String, Object> map = new HashMap<>();
        SysUser user = (SysUser) session.getAttribute(ConstantConfig.CURRENT_USER);
        if(!StringUtils.equals(user.getPassword(), Md5Util.md5(oldPassword,ConstantConfig.SALT))){
            map.put("success", false);
            map.put("msg", "原密码错误！");
        }else{
            SysUser oldUser = userService.getByUserId(user.getUserId());
            oldUser.setPassword(Md5Util.md5(newPassword,ConstantConfig.SALT));
            userService.updateByUser(oldUser);
            map.put("success", true);
            map.put("msg", "密码修改成功");
        }
        return map;
    }



    /**
     * 用户列表查询
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/findAll")
    @ResponseBody
    public Map<String, Object> findAll(Integer page, Integer rows){
        Map<String, Object> map = new HashMap<>();

        //当前页数据
        List<SysUser> lists = userService.selectUserList(new SysUser(),page, rows);
        //总条数
        Long totals = userService.getCount(new SysUser()).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }

    /**
     * 删除用户   逻辑删除 == 删除数据库
     * @param userId
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public Map<String, Object> userDel(Long userId){
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> delMap = new HashMap<>();
        delMap.put("user_id",userId);
        boolean bool = userService.deleteByMap(delMap);
        map.put("success",bool);
        return map;
    }

    /**
     * 用户添加
     * @param user
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> userAdd(SysUser user){
        Map<String, Object> map = new HashMap<>();
        if(user == null){
            map.put("success",false);
            map.put("msg","用户信息为空！");
        }else{
            if(StringUtil.isEmpty(user.getUserName()) || StringUtil.isEmpty(user.getPassword()) ){
                map.put("success",false);
                map.put("msg","用户名或密码为空！");
            }else{
                userService.saveUser(user);
                map.put("success", true);
                map.put("msg", "用户新增成功！");
            }
        }
        return map;
    }

}
