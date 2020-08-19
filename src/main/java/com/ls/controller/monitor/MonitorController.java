package com.ls.controller.monitor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonitorController {

    @GetMapping("/monitor/add")
    public String toMonitorAdd(){
        return "monitor/add";
    }

    @GetMapping("/monitor/list")
    public String toMonitorList(){
        return "monitor/list";
    }

    @GetMapping("/monitor/chart")
    public String toMonitorChart(){
        return "monitor/charts";
    }

    @GetMapping("/monitor/bulletin")
    public String toMonitorBulletin(){
        return "monitor/bulletin";
    }

    @GetMapping("/monitor/report")
    public String toMonitorReport(){
        return "monitor/report";
    }

}
