package com.ls.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.system.SysSite;

import java.util.List;

public interface SysSiteService extends IService<SysSite> {

    List<SysSite> selectUserList(SysSite sysSite, Integer page, Integer rows);

    Integer getCount(SysSite sysSite);

}
