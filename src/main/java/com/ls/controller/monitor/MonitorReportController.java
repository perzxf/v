package com.ls.controller.monitor;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorBulletin;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.monitor.MonitorReport;
import com.ls.entity.system.SysUser;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.monitor.MonitorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MonitorReportController {

    @Autowired
    private MonitorReportService reportService;
    @Autowired
    private MonitorItemService itemService;

    @GetMapping("/monitor/report/find")
    @ResponseBody
    public Map<String, Object> find( Integer page, Integer rows, Long[] monitorIds){
        Map<String, Object> map = new HashMap<>();

        //当前页数据
        List<MonitorReport> lists = reportService.selectReportList(monitorIds,page, rows);
        for(MonitorReport monitorReport:lists){
            String monitorId = monitorReport.getMonitorId();
            MonitorItem monitorItem = itemService.getMonitorItemById(Long.valueOf(monitorId));
            monitorReport.setMonitorName(monitorItem.getMonitorName());
        }
        //总条数
        Long totals = reportService.getCount(monitorIds).longValue();
        //总页数
        Long totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;

        map.put("page", page);
        map.put("rows", lists);
        map.put("total", totalPage);
        map.put("records", totals);
        return map;
    }


    @PostMapping("/monitor/report/del")
    @ResponseBody
    public Map<String, Object> delReport( Long reportId){
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> delMap = new HashMap<>();
        delMap.put("report_id",reportId);
        boolean bool = reportService.deleteByMap(delMap);

        map.put("success",bool);
        map.put("msg","删除成功！");
        return map;
    }

    /**
     * 通过输出流的形式  把数据库中的 内容  输出到页面上
     * @param reportId
     * @param response
     */
    @GetMapping("/monitor/report/look")
    public void look( String reportId ,HttpServletResponse response){
        Wrapper<MonitorReport> wrapper = new EntityWrapper<>();
        wrapper.eq("report_id",reportId);
        MonitorReport report = reportService.selectOne(wrapper);
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(report.getReportContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
