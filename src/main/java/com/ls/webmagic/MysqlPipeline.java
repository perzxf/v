package com.ls.webmagic;

import com.ls.entity.monitor.MonitorEvent;
import com.ls.service.monitor.MonitorEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

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
    }
}
