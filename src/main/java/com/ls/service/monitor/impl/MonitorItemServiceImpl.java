package com.ls.service.monitor.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.common.ConstantConfig;
import com.ls.entity.monitor.MonitorItem;
import com.ls.entity.system.SysUser;
import com.ls.mapper.monitor.MonitorItemMapper;
import com.ls.service.monitor.MonitorItemService;
import com.ls.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class MonitorItemServiceImpl extends ServiceImpl<MonitorItemMapper, MonitorItem> implements MonitorItemService {

    private static final Logger log = LoggerFactory.getLogger(MonitorItemServiceImpl.class);

    @Override
    public void saveMonitorItem(MonitorItem item,HttpSession session) {
        item = createByMonitorItem(item,session);
        baseMapper.insert(item);
        log.info("保存的项目信息：{}",item.toString());
    }

    @Override
    public List<MonitorItem> selectUserList(MonitorItem item, Integer page, Integer rows) {
        //设置分页
        Page<MonitorItem> userPage = new Page<>();
        userPage.setCurrent(page);  //当前第几页
        userPage.setSize(rows); //每页几条数据

        //查询条件
        Wrapper<MonitorItem> wrapper = new EntityWrapper<>();
        wrapper.ge("monitor_id",1); //userId 大于等于1
        if(StringUtil.isNotEmpty(item.getMonitorName())){
            wrapper.like("monitor_name", item.getMonitorName()); // 模糊查询
        }
        if(item.getUserId() != null){
            wrapper.eq("user_id",item.getUserId());
        }
        wrapper.orderBy("create_date", false); // 按照创建时间倒序排序

        List<MonitorItem> items = baseMapper.selectPage(userPage, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return items;
    }

    @Override
    public Integer getCount(MonitorItem item) {
        Wrapper<MonitorItem> wrapper = new EntityWrapper<>();
        if(item.getUserId() != null){
            wrapper.eq("user_id",item.getUserId());
        }
        Integer count = baseMapper.selectCount(wrapper);
        log.info("查询到用户信息一共{}条",count);
        return count;
    }

    @Override
    public MonitorItem getMonitorItemById(Long monitorId) {
        MonitorItem item = new MonitorItem();
        item.setMonitorId(monitorId);
        MonitorItem monitorItem = baseMapper.selectOne(item);
        return monitorItem;
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
