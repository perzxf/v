package com.ls.service.monitor.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.ls.common.RedisCacheKey;
import com.ls.common.SiteTypeEnum;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.mapper.monitor.MonitorEventMapper;
import com.ls.service.monitor.MonitorEventService;
import com.ls.utils.RedisUtil;
import com.ls.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Slf4j
public class MonitorEventServiceImpl extends ServiceImpl<MonitorEventMapper, MonitorEvent> implements MonitorEventService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public MonitorEvent getEventById(Long eventId) {
        MonitorEvent event = new MonitorEvent();
        event.setEventId(eventId);
        return baseMapper.selectOne(event);
    }

    @Override
    public void updateEvent(Long eventId, int state) {
        MonitorEvent event = new MonitorEvent();
        event.setEventId(eventId);
        event.setState(state);
        baseMapper.updateById(event);
    }

    @Override
    public List<MonitorEvent> getEventList(Long monitorId) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(monitorId != null){
            wrapper.eq("monitor_id", monitorId);

            //时间搜索  当天时间往前推15天
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DAY_OF_WEEK, -15);
            wrapper.ge("event_date",calendar.getTime());
            wrapper.le("event_date",date);

        }

        wrapper.orderBy("event_date", true); // 按照创建时间排序

        return baseMapper.selectList(wrapper);
    }

    @Override
    public Integer getEventCount(String eventDate) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        wrapper.like("event_date", eventDate); // 模糊查询
        Integer count = baseMapper.selectCount(wrapper);
        return count;
    }

    @Override
    public Integer getPieEventCount(Long monitorId, Long siteTypeId) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(monitorId != null){
            wrapper.eq("monitor_id", monitorId);
        }
        if(siteTypeId != null){
            wrapper.eq("site_type_id", siteTypeId);
        }
        Integer count = baseMapper.selectCount(wrapper);
        return count;
    }

    /**
     * 处理政府留言板数据到数据库
     * @param liuYanEvent
     */
    @Override
    public void saveLiuYanEvent(List<MonitorEvent> liuYanEvent) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:liuYanEvent){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.OTHER.getExpire()); //网站数据类型

            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);   //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

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
            //时间搜索  当天时间往前推15天
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DAY_OF_WEEK, -15);
            wrapper.ge("event_date",calendar.getTime());
            wrapper.le("event_date",date);

            //不展示无用信息,只展示有用信息
            wrapper.eq("is_use",1);

        }
        wrapper.orderBy("event_date", false); // 按照时间倒序排序

        List<MonitorEvent> items = baseMapper.selectPage(userPage, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return items;
    }

    @Override
    public Integer getCount(MonitorEvent item) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(item != null){
            wrapper.eq("monitor_id", item.getMonitorId()); // 精确查询
            //时间搜索  当天时间往前推15天
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DAY_OF_WEEK, -15);
            wrapper.ge("event_date",calendar.getTime());
            wrapper.le("event_date",date);
        }
        Integer count = baseMapper.selectCount(wrapper);
        log.info("查询到用户信息一共{}条",count);
        return count;
    }

    private Long getRedisMonitor(){
        String redisKey = RedisCacheKey.MONITOR.getType();
        Integer id = (Integer) redisUtil.get(redisKey);
        return id.longValue();
    }

    /**
     * 微信数据添加
     * @param events
     */
    @Override
    public void saveWeChatEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.WEI_XIN.getExpire()); //网站数据类型
//            String link = "https://weixin.sogou.com"+event.getEventUrl();
            String link = "https://weixin.sogou.com/weixin?type=2&ie=utf8&query="+event.getEventTitle();  //存链接的方式

            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
                event.setEventUrl(link);
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);   //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 搜狗新闻数据添加
     * @param events
     */
    @Override
    public void saveSouHuNewsEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.NEWS.getExpire()); //网站数据类型

            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
                if(!StringUtil.getResult(event.getEventUrl(), "http")){
                    String link = "https://www.sogou.com"+event.getEventUrl();
                    event.setEventUrl(link);
                }
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);  //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 知乎数据添加
     * @param events
     */
    @Override
    public void saveZhiHuEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.ZHI_HU.getExpire()); //网站数据类型
//            String link = "https://www.sogou.com"+event.getEventUrl();
            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
                if(!StringUtil.getResult(event.getEventUrl(), "http")){
                    String link = "https://www.sogou.com"+event.getEventUrl();
                    event.setEventUrl(link);
                }
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);   //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 百度贴吧数据添加
     * @param events
     */
    @Override
    public void saveTieBaEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.TIE_BA.getExpire()); //网站数据类型
            String link = "https://tieba.baidu.com"+event.getEventUrl();
            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
                event.setEventUrl(link);
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);  //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 天涯论坛数据添加
     * @param events
     */
    @Override
    public void saveTianYaEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.LUN_TAN.getExpire()); //网站数据类型
//            String link = "https://tieba.baidu.com"+event.getEventUrl();
            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
//                event.setEventUrl(link);
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);  //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 新浪新闻数据添加
     * @param events
     */
    @Override
    public void saveSinaNewsEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.NEWS.getExpire()); //网站数据类型
//            String link = "https://tieba.baidu.com"+event.getEventUrl();
            //判断记录是否已存在
            List<MonitorEvent> list = selectInfo(event);
            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
//                event.setEventUrl(link);
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);   //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
            } else {
                log.info("记录已存在,记录title:{}",event.getEventTitle());
            }
        }
    }

    /**
     * 今日头条数据添加
     * @param events
     */
    @Override
    public void saveTouTiaoEvent(List<MonitorEvent> events) {
        Long redisMonitorId = getRedisMonitor();
        for (MonitorEvent event:events){
            event.setMonitorId(redisMonitorId);
            event.setSiteTypeId(SiteTypeEnum.TOU_TIAO.getExpire()); //网站数据类型
//            String link = "https://tieba.baidu.com"+event.getEventUrl();
            //判断记录是否已存在
//            List<MonitorEvent> list = selectInfo(event);
//            if (CollUtil.isEmpty(list)) {
                //把url放到任务队列中
//                event.setEventUrl(link);
                event.setCreateDate(new Date());
                event.setState(0);
//                event.setEventType(RandomUtils.randomItem(ConstantConfig.INTS));
                event.setEventType(0);   //事件类型    暂定默认中性
                event.setIsUse(1);  //默认数据信息有用
                baseMapper.insert(event);
                log.info("保存的信息：{}",event.toString());
//            } else {
//                log.info("记录已存在,记录title:{}",event.getEventTitle());
//            }
        }
    }


    private List<MonitorEvent> selectInfo(MonitorEvent event) {
        Wrapper<MonitorEvent> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(event.getEventTitle())){
            wrapper.eq("event_title",event.getEventTitle());
        }
        if(event.getSiteTypeId() != null){
            wrapper.eq("site_type_id",event.getSiteTypeId());
        }
        if(event.getEventDate() != null){
            wrapper.eq("event_date",event.getEventDate());
        }
        return baseMapper.selectList(wrapper);
    }
}
