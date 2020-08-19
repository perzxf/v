package com.ls.service.monitor.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.monitor.MonitorEvent;
import com.ls.mapper.monitor.MonitorEventMapper;
import com.ls.service.monitor.MonitorEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonitorEventServiceImpl extends ServiceImpl<MonitorEventMapper, MonitorEvent> implements MonitorEventService {

    private static final Logger log = LoggerFactory.getLogger(MonitorEventServiceImpl.class);

}
