package com.ls.webmagic;

import com.ls.entity.monitor.MonitorEvent;
import com.ls.service.monitor.MonitorEventService;
import com.ls.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实现PipeLine,保存到数据库
 */
@Component
@Slf4j
public class MysqlPipeline implements Pipeline {

    @Autowired
    private MonitorEventService eventService;

    @Override
    public void process(ResultItems resultItems, Task task){
        //获取封装好的数据
        List<MonitorEvent> weChatEvent = resultItems.get("weChatInfoList");
        if (weChatEvent != null) {
            eventService.saveWeChatEvent(weChatEvent);
        }
        List<MonitorEvent> souHuNewsEvent = resultItems.get("souHuNewsInfoList");
        if (souHuNewsEvent != null) {
            eventService.saveSouHuNewsEvent(souHuNewsEvent);
        }
        List<MonitorEvent> zhiHuEvent = resultItems.get("zhiHuInfoList");
        if (zhiHuEvent != null) {
            eventService.saveZhiHuEvent(zhiHuEvent);
        }
        List<MonitorEvent> tieBaEvent = resultItems.get("tieBaInfoList");
        if (tieBaEvent != null) {
            eventService.saveTieBaEvent(tieBaEvent);
        }
        List<MonitorEvent> tianYaEvent = resultItems.get("tianYaInfoList");
        if (tianYaEvent != null) {
            eventService.saveTianYaEvent(tianYaEvent);
        }
        List<MonitorEvent> sinaNewsEvent = resultItems.get("sinaNewsInfoList");
        if (sinaNewsEvent != null) {
            eventService.saveSinaNewsEvent(sinaNewsEvent);
        }
        List<MonitorEvent> touTiaoEvent = resultItems.get("touTiaoInfoList");
        if (touTiaoEvent != null) {
            eventService.saveTouTiaoEvent(touTiaoEvent);
        }

        /**
         * 处理政府留言板的数据，在这里封装成对象
         */
        List<String> tid_list = resultItems.get("tid");  //获取ID，方便组成链接（http://liuyan.people.com.cn/threads/content?tid=8400539）
        List<String> subject_list = resultItems.get("subject"); //标题
        List<String> stateInfo_list = resultItems.get("stateInfo"); //状态
        List<String> content_list = resultItems.get("content");   //内容
        List<String> time_list = resultItems.get("threadsCheckTime");   //时间戳

        List<MonitorEvent> liuYanEvent = new ArrayList<>();

        if (tid_list != null){
            for (int i = 0 ; i<10 ; i++) {
                MonitorEvent event = new MonitorEvent();
                String url = "http://liuyan.people.com.cn/threads/content?tid="+tid_list.get(i);
                event.setEventUrl(url);
                event.setEventTitle(subject_list.get(i));
                try {
                    Date date = DateUtil.formatTimeStampToDate(time_list.get(i), "yyyy-MM-dd HH:mm:ss");
                    event.setEventDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event.setEventContent(content_list.get(i));

                liuYanEvent.add(event);
            }
        }

        if (liuYanEvent != null) {
            eventService.saveLiuYanEvent(liuYanEvent);
        }

    }
}
