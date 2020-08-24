package com.ls.service.monitor.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.mapper.monitor.MonitorEventMapper;
import com.ls.service.monitor.MonitorEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MonitorEventServiceImpl extends ServiceImpl<MonitorEventMapper, MonitorEvent> implements MonitorEventService {

    private static final Logger log = LoggerFactory.getLogger(MonitorEventServiceImpl.class);

    @Override
    public List<MonitorEvent> selectEventList(MonitorEvent item, Integer page, Integer rows) {
        //设置分页
        Page<MonitorEvent> userPage = new Page<>();
        userPage.setCurrent(page);  //当前第几页
        userPage.setSize(rows); //每页几条数据

        //查询条件
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        wrapper.ge("event_id",1); //userId 大于等于1
        if(item != null){
            wrapper.eq("monitor_id", item.getMonitorId()); // 精确查询
        }
        wrapper.orderBy("create_date", false); // 按照创建时间倒序排序

        List<MonitorEvent> items = baseMapper.selectPage(userPage, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return items;
    }

    @Override
    public Integer getCount(MonitorEvent item) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(item != null){
            wrapper.eq("monitor_id", item.getMonitorId()); // 精确查询
        }
        Integer count = baseMapper.selectCount(wrapper);
        log.info("查询到用户信息一共{}条",count);
        return count;
    }

    @Override
    public void saveEvent(List<MonitorEvent> events) {
        for (MonitorEvent event:events){
            String link = "https://weixin.sogou.com"+event.getEventUrl();
            //判断记录是否已存在
            List<MonitorEvent> weChatList = selectWeChatInfo(event);
            if (CollUtil.isEmpty(weChatList)) {
                //把url放到任务队列中
                event.setEventUrl(link);
                event.setCreateDate(new Date());
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    private List<MonitorEvent> selectWeChatInfo(MonitorEvent event) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(event.getEventTitle())){
            wrapper.eq("event_title",event.getEventTitle());
        }
        if(event.getEventDate() != null){
            wrapper.eq("event_date",event.getEventDate());
        }
        return baseMapper.selectList(wrapper);
    }
}
