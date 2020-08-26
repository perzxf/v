package com.ls.service.monitor.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.monitor.MonitorBulletin;
import com.ls.entity.system.SysUser;
import com.ls.mapper.monitor.MonitorBulletinMapper;
import com.ls.service.monitor.MonitorBulletinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorBulletinServiceImpl extends ServiceImpl<MonitorBulletinMapper, MonitorBulletin> implements MonitorBulletinService {

    private static final Logger log = LoggerFactory.getLogger(MonitorBulletinServiceImpl.class);

    @Override
    public void bulletinAdd(Long monitorId, Long eventId) {
        MonitorBulletin bulletin = new MonitorBulletin();
        bulletin.setEventId(eventId);
        bulletin.setMonitorId(monitorId);
        baseMapper.insert(bulletin);
    }

    @Override
    public List<MonitorBulletin> selectBulletinList(MonitorBulletin monitorBulletin, Integer page, Integer rows) {
        //设置分页
        Page<SysUser> pageBulletin = new Page<>();
        pageBulletin.setCurrent(page);  //当前第几页
        pageBulletin.setSize(rows); //每页几条数据

        //查询条件
        Wrapper<MonitorBulletin> wrapper = new EntityWrapper<>();
        wrapper.ge("bulletin_id",1); //userId 大于等于1

        List<MonitorBulletin> users = baseMapper.selectPage(pageBulletin, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return users;
    }

    @Override
    public Integer getCount(MonitorBulletin monitorBulletin) {
        Wrapper<MonitorBulletin> wrapper = new EntityWrapper<>();
        Integer count = baseMapper.selectCount(wrapper);
        log.info("查询到用户信息一共{}条",count);
        return count;
    }

    @Override
    public boolean booleanByBulletin(Long eventId) {
        MonitorBulletin bulletin = new MonitorBulletin();
        bulletin.setEventId(eventId);
        MonitorBulletin item = baseMapper.selectOne(bulletin);
        if(item == null){
            return true;
        }else{
            return false;
        }
    }

}
