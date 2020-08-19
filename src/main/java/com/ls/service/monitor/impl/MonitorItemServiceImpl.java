package com.ls.service.monitor.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysUser;
import com.ls.mapper.monitor.MonitorItemMapper;
import com.ls.service.monitor.MonitorItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class MonitorItemServiceImpl extends ServiceImpl<MonitorItemMapper, MonitorItem> implements MonitorItemService {

    private static final Logger log = LoggerFactory.getLogger(MonitorItemServiceImpl.class);

    @Override
    public void saveMonitorItem(MonitorItem item,HttpSession session) {
        item = createByMonitorItem(item,session);
        baseMapper.insert(item);
        log.info("保存的项目信息：{}",item.toString());
    }

    private MonitorItem createByMonitorItem(MonitorItem item, HttpSession session) {
        //创建时间
        Date date = new Date();
        item.setCreateDate(date);
        //创建用户
        SysUser currentUser = (SysUser) session.getAttribute(ConstantConfig.CURRENT_USER);
        item.setUserId(currentUser.getUserId());
        return item;
    }
}
