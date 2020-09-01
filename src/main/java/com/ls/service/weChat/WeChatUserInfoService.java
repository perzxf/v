package com.ls.service.weChat;

import com.baomidou.mybatisplus.service.IService;
import com.ls.entity.weChat.WeChatUserInfo;

public interface WeChatUserInfoService extends IService<WeChatUserInfo> {
    /**
     * 添加微信用户
     * @param wChatUser
     */
    void addWeChatUser(WeChatUserInfo wChatUser);
}
