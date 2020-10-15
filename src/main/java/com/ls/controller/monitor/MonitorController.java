package com.ls.controller.monitor;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ls.common.ConstantConfig;
import com.ls.common.WeChatConfig;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysUser;
import com.ls.entity.weChat.WeChatUserInfo;
import com.ls.service.monitor.MailService;
import com.ls.service.monitor.MonitorBulletinService;
import com.ls.service.monitor.MonitorEventService;
import com.ls.service.monitor.MonitorItemService;
import com.ls.service.system.SysUserService;
import com.ls.service.weChat.WeChatUserInfoService;
import com.ls.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class MonitorController {

//    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);

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

    @Autowired
    private WeChatUtil weChatUtil;

    /**
     * 定时发送邮件
     * 循环获取项目数据表
     * 判断：处理状态未处理，事件类型负面
     * 发送邮件  同时把处理状态改成已处理，并把当前数据添加到简报中
     */
//    @Scheduled(cron = "0 27 10,14,17,21 * * ?")
    @Scheduled(cron = "0 0 10,14,18,21 * * ?")  //每天上午10点，下午2点，6点,晚上9点
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

                    //发送微信到公众号里面
                    String format_str = String.format(ConstantConfig.TEXT_STR,event.getEventUrl(),event.getEventTitle() );
                    weChatUtil.sendText("",user.getOpenId(),format_str);

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

    
    @Autowired
    private WeChatUserInfoService weChatService;

    /**
     * 定时更新微信公众号人员列表
     */
    @Scheduled(cron = "0 0 0/1 * * ?")   //每1小时更新一次
    public void updateWeChatInfo(){
        log.info("开始执行定时更新微信公众号人员任务: "+new Date().toLocaleString());
        //根据appid和appsecret获取access_token
        String accessToken = weChatUtil.getAccessToken(WeChatConfig.APPID, WeChatConfig.SECRET);
        //获取微信公众号关注者 openid 列表
        List<Object> openids = weChatUtil.getWeChatUsers(accessToken);
        for (Object openid : openids) {
            //根据 openid 和 accessToken 获取用户个人信息
            WeChatUserInfo wChatUser = weChatUtil.getWChatUser(openid, accessToken);
            //把用户个人信息存入数据库中
            weChatService.addWeChatUser(wChatUser);
        }
    }


    /**
     * test测试微信公众号发信息
     */
    /*@Scheduled(cron = "0/10 * * * * ?")
    public void testWeChatSendText(){
        log.info("开始测试微信公众号自动发送消息给某个关注人员: "+new Date().toLocaleString());

        //发送微信到公众号里面

        String testUrl = "https://mp.weixin.qq.com/s/4xwA-xN3pOuyjYGOQozsBQ";
        String testTitle ="测试----州来皖投 天下·明珠 | 巅峰之上 明珠熠熠";

        String format_str = String.format(ConstantConfig.TEXT_STR,testUrl,testTitle);

        //测试发送给某一个人
//        String openid = "o8_Rss1ym4rQGnUhOVaPWKtKJqWA";
        String openid = "o8_Rss0MVmmLuvZFp6-TgMeMtMyg";

        weChatUtil.sendText(null,openid,format_str);

    }*/


}
