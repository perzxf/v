package com.ls.controller.monitor;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysUser;
import com.ls.service.monitor.MailService;
import com.ls.service.monitor.MonitorBulletinService;
import com.ls.service.monitor.MonitorEventService;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.system.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MonitorController {

    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);

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

    @Autowired
    private MailService mailService;

    /**
     * 邮件预警
     * @param accepter
     * @param subject
     * @param content
     * @return
     */
    @PostMapping("/email/html")
    @ResponseBody
    public Map<String,Object> sendMail(String accepter,String subject,String content){
        Map<String,Object> map = new HashMap<>();
        mailService.sendMail(accepter,subject,content);
        map.put("success",true);
        return map;
    }

    @Autowired
    private MonitorEventService eventService;

    @Autowired
    private MonitorItemService itemService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private MonitorBulletinService bulletinService;

    /**
     * 定时发送邮件
     * 循环获取项目数据表
     * 判断：处理状态未处理，事件类型负面
     * 发送邮件  同时把处理状态改成已处理，并把当前数据添加到简报中
     */
    @Scheduled(cron = "0 0 10,14,18 * * ?")  //每天上午10点，下午2点，6点
//    @Scheduled(cron = "0 0 0 */1 * ?")   //每天凌晨0点执行一次
    public void sendCronMail() {
        log.info("开始执行定时任务: "+new Date().toLocaleString());
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        List<MonitorEvent> monitorEvents = eventService.selectList(wrapper);
        for(MonitorEvent event:monitorEvents){
            Integer state = event.getState();
            Integer eventType = event.getEventType();
            if(state == ConstantConfig.EVENT_STATE &&
                    eventType == ConstantConfig.EVENT_TYPE_NEGATIVE){
                MonitorItem item = itemService.getMonitorItemById(event.getMonitorId());
                if(item != null){
                    SysUser user = userService.getByUserId(item.getUserId());
                    String content = "<html>\n" +
                                        "<body>\n" +
                                        "<div>\n"+
                                        "<p><h3>预警事件:</h3></p>\n" +
                                        "<div style='color: red'>\n" +event.getEventTitle()+
                                        "</div>\n"+
                                        "<p><a href="+event.getEventUrl()+" >点击查看</a></p>\n"+
                                        "</div>\n"+
                                        "</body>\n" +
                                    "</html>";
                    //发送邮件
                    mailService.sendMail(user.getEmail(),"舆情监测预警提醒",content);
                    //当前数据添加到简报中
                    if(bulletinService.booleanByBulletin(event.getEventId())){
                        //加入简报数据
                        bulletinService.bulletinAdd( event.getMonitorId(), event.getEventId());
                        //修改数据的处理状态
                        eventService.updateEvent(event.getEventId(),1);
                    }
                }
            }
        }
    }
}
