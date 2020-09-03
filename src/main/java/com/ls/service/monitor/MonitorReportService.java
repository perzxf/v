package com.ls.service.monitor;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.monitor.MonitorReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MonitorReportService extends IService<MonitorReport> {

    List<MonitorReport> selectReportList(Long[] monitorIds,  Integer page, Integer rows);

    Integer getCount(Long[] monitorIds);
}
