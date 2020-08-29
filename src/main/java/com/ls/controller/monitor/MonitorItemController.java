package com.ls.controller.monitor;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysUser;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.system.SysUserService;
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
    @Autowired
    private SysUserService userService;

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
    public Map<String, Object> findAll(Integer page, Integer rows,HttpSession session){
        Map<String, Object> map = new HashMap<>();

        MonitorItem item = new MonitorItem();
        //获取session用户
        SysUser user = (SysUser) session.getAttribute(ConstantConfig.CURRENT_USER);
        Long userId = user.getUserId();
        if(userId != 1l){
            item.setUserId(userId);
        }

        //当前页数据
        List<MonitorItem> lists = itemService.selectUserList(item,page, rows);
        for(MonitorItem monitorItem:lists){
            SysUser byUserId = userService.getByUserId(monitorItem.getUserId());
            monitorItem.setUserName(byUserId.getNickName());
        }
        //总条数
        Long totals = itemService.getCount(item).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }

    /**
     * 数据项目选择
     * @return
     */
    @GetMapping("/item/listpark")
    @ResponseBody
    public Map<String, Object> listPark(HttpSession session){
        Map<String, Object> map = new HashMap<>();

        //获取session用户
        SysUser user = (SysUser) session.getAttribute(ConstantConfig.CURRENT_USER);
        Long userId = user.getUserId();

        Wrapper<MonitorItem> wrapper = new EntityWrapper<>();
        if(userId != null){
            if(userId != 1l){
                wrapper.eq("user_id",userId);
            }
        }
        List<MonitorItem> items = itemService.selectList(wrapper);
        map.put("data", items);
        map.put("success", true);
        return map;
    }
}
