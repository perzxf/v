package com.ls.service.monitor.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import com.ls.entity.monitor.MonitorReport;
import com.ls.mapper.monitor.MonitorReportMapper;
import com.ls.service.monitor.MonitorReportService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MonitorReportServiceImpl extends ServiceImpl<MonitorReportMapper,MonitorReport> implements MonitorReportService {


    @Override
    public List<MonitorReport> selectReportList(Long[] monitorIds, Integer page,Integer rows) {

        //设置分页
        Page<MonitorReport> reportPage = new Page<>();
        reportPage.setCurrent(page);  //当前第几页
        reportPage.setSize(rows); //每页几条数据

        //查询条件
        Wrapper<MonitorReport> wrapper = new EntityWrapper<>();
        wrapper.in("monitor_id",monitorIds); //monitor_id在数组之间的
        wrapper.orderBy("create_date", false); // 按照创建时间倒序排序

        return baseMapper.selectPage(reportPage, wrapper);
    }

    @Override
    public Integer getCount(Long[] monitorIds) {
        Wrapper<MonitorReport> wrapper = new EntityWrapper<>();
        wrapper.in("monitor_id",monitorIds); //monitor_id在数组之间的
        return baseMapper.selectCount(wrapper);
    }
}
