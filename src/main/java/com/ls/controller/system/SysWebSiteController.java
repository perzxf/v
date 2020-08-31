package com.ls.controller.system;

import com.ls.entity.system.SysSite;
import com.ls.service.system.SysWebSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SysWebSiteController {

    @Autowired
    private SysWebSiteService webSiteService;

    @PostMapping("/website/add")
    @ResponseBody
    public Map<String,Object> addSite(SysSite sysSite){
        Map<String,Object> map = new HashMap<>();

        Integer integer = webSiteService.addSite(sysSite);
        if(integer == 1){
            map.put("msg","网站添加成功");
        }else {
            map.put("msg", "网站添加失败");
        }
        return map;
    }

    @GetMapping("/website/del")
    @ResponseBody
    public Map<String,Object> delSite(Long siteId){
        Map<String,Object> map = new HashMap<>();
        Integer integer =webSiteService.delSite(siteId);
        if(integer == 1){
            map.put("msg","成功删除");
        }else {
            map.put("msg","删除失败,请检查后重试");
        }
        return map;

    }

}
