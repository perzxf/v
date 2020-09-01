package com.ls.service.system.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.system.SysSite;
import com.ls.mapper.system.SysSiteMapper;
import com.ls.service.system.SysSiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysSiteServiceImpl extends ServiceImpl<SysSiteMapper, SysSite> implements SysSiteService {

    private static final Logger log = LoggerFactory.getLogger(SysSiteServiceImpl.class);

    @Override
    public List<SysSite> selectUserList(SysSite sysSite, Integer page, Integer rows) {
        //设置分页
        Page<SysSite> userPage = new Page<>();
        userPage.setCurrent(page);  //当前第几页
        userPage.setSize(rows); //每页几条数据

        //查询条件
        Wrapper<SysSite> wrapper = new EntityWrapper<>();
        wrapper.ge("site_id",1); //userId 大于等于1
        if(sysSite != null){
            wrapper.like("site_name", sysSite.getSiteName()); // 模糊查询
        }
        wrapper.orderBy("create_date", false); // 按照创建时间倒序排序

        List<SysSite> users = baseMapper.selectPage(userPage, wrapper);
        log.info("当前第{}页,每页展示{}条数据",page,rows);
        return users;
    }

    @Override
    public Integer getCount(SysSite sysSite) {
        Wrapper<SysSite> wrapper = new EntityWrapper<>();
        Integer count = baseMapper.selectCount(wrapper);
        log.info("查询到用户信息一共{}条",count);
        return count;
    }

    @Override
    public List<SysSite> getSiteList() {
        Wrapper<SysSite> wrapper = new EntityWrapper<>();
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<SysSite> getSiteListUniq() {
        Wrapper<SysSite> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("DISTINCT site_type_id");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Integer addSite(SysSite sysSite) {
        return null;
    }

    @Override
    public Integer delSite(Long siteId) {
        return null;
    }

}
