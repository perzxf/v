package com.ls.controller.system;

import com.ls.entity.system.SysSite;
import com.ls.service.system.SysSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/site")
public class SysSiteController {

    @Autowired
    private SysSiteService siteService;

    /**
     * 网站列表查询
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/findAll")
    @ResponseBody
    public Map<String, Object> findAll(Integer page, Integer rows){
        Map<String, Object> map = new HashMap<>();

        //当前页数据
        List<SysSite> lists = siteService.selectUserList(new SysSite(),page, rows);
        //总条数
        Long totals = siteService.getCount(new SysSite()).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }
}
