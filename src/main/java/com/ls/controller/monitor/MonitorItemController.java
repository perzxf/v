package com.ls.controller.monitor;

import com.ls.entity.monitor.MonitorItem;
import com.ls.service.monitor.MonitorItemService;
import com.ls.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/monitor")
public class MonitorItemController {

    @Autowired
    private MonitorItemService itemService;


    @PostMapping("/item/add")
    @ResponseBody
    public Map<String,Object> register(MonitorItem item, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        if(item == null){
            map.put("success",false);
            map.put("msg","项目信息为空！");
        }else{
            if(StringUtil.isEmpty(item.getMonitorName()) || StringUtil.isEmpty(item.getKeywords()) ){
                map.put("success",false);
                map.put("msg","项目信息为空！");
            }else{
                itemService.saveMonitorItem(item,session);
                map.put("success", true);
                map.put("msg", "项目创建成功！");
            }
        }
        return map;
    }

}
