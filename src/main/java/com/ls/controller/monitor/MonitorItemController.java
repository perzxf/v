package com.ls.controller.monitor;

import com.ls.entity.monitor.MonitorItem;
import com.ls.service.monitor.MonitorItemService;
import com.ls.utils.StringUtil;
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

    /**
     * 项目列表查询
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/item/findAll")
    @ResponseBody
    public Map<String, Object> findAll(Integer page, Integer rows){
        Map<String, Object> map = new HashMap<>();

        //当前页数据
        List<MonitorItem> lists = itemService.selectUserList(new MonitorItem(),page, rows);
        //总条数
        Long totals = itemService.getCount(new MonitorItem()).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }
}
