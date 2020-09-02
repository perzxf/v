package com.ls.service.weChat.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ls.entity.weChat.WeChatUserInfo;
import com.ls.mapper.weChat.WeChatUserInfoMapper;
import com.ls.service.weChat.WeChatUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeChatUserInfoServiceImpl extends ServiceImpl<WeChatUserInfoMapper, WeChatUserInfo> implements WeChatUserInfoService {

    private static final Logger log = LoggerFactory.getLogger(WeChatUserInfoServiceImpl.class);

    @Override
    public void addWeChatUser(WeChatUserInfo wChatUser) {
        String openid = wChatUser.getOpenid();

        Wrapper<WeChatUserInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("openid", openid); // 精确查询
        Integer count = baseMapper.selectCount(wrapper);
        if(count < 1){ //如果小于1 说明不存在
            baseMapper.insert(wChatUser);
            log.info("添加一条数据:{}",wChatUser);
        }else{
            log.info("已经存在数据:{}",wChatUser);
        }
    }
}
