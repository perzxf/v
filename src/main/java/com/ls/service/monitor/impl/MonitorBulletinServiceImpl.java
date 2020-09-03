package com.ls.service.monitor.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.monitor.MonitorBulletin;
import com.ls.entity.monitor.MonitorReport;
import com.ls.entity.system.SysUser;
import com.ls.mapper.monitor.MonitorBulletinMapper;
import com.ls.service.monitor.MonitorBulletinService;

import com.ls.utils.DateUtil;
import com.ls.service.monitor.MonitorReportService;
import com.ls.utils.PdfUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class MonitorBulletinServiceImpl extends ServiceImpl<MonitorBulletinMapper, MonitorBulletin> implements MonitorBulletinService {

    private static final Logger log = LoggerFactory.getLogger(MonitorBulletinServiceImpl.class);
    private static Configuration cfg = new Configuration(Configuration.getVersion());

    @Autowired
    private MonitorReportService monitorReportService;


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
        if(monitorBulletin.getMonitorId() != null){
            wrapper.eq("monitor_id",monitorBulletin.getMonitorId());
        }
        List<MonitorBulletin> users = baseMapper.selectPage(pageBulletin, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return users;
    }

    @Override
    public Integer getCount(MonitorBulletin monitorBulletin) {
        Wrapper<MonitorBulletin> wrapper = new EntityWrapper<>();
        if(monitorBulletin.getMonitorId() != null){
            wrapper.eq("monitor_id",monitorBulletin.getMonitorId());
        }
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

    @Override
    public File createBulletin(Map data,Long monitorId) {
        MonitorReport monitorReport = new MonitorReport();
        Writer out = new StringWriter();
        File file = new File(System.getProperty("user.dir")+"/src/main/resources/static/assets/pdfjs/web/"+ DateUtil.getCurrentDateStr() +".pdf");

        cfg.setClassForTemplateLoading(this.getClass(),"/templates/ftl");
        cfg.setEncoding(Locale.CHINESE,"utf-8");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        try {
            Template template = cfg.getTemplate("vlousmuban3.ftl");

//            out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            /**
             * @data 是输入
             * @out 是输出
             */
            template.process(data,out);
            monitorReport.setCreateDate(new Date());
            monitorReport.setMonitorId(monitorId.toString());
            monitorReport.setUpdateDate(new Date());
            monitorReport.setReportContent(out.toString());
            monitorReport.setReportName(file.getName());
            PdfUtil.createPdf(monitorReport.getReportContent(),file);

            monitorReportService.insert(monitorReport);

            out.flush();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return file;

    }

    @Override
    public MonitorBulletin getbulletinById(long bulletinId) {
        MonitorBulletin bulletin = new MonitorBulletin();
        bulletin.setBulletinId(bulletinId);
        return baseMapper.selectOne(bulletin);
    }

}
