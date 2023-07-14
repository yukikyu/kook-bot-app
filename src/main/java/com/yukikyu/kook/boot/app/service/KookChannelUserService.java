package com.yukikyu.kook.boot.app.service;

import cn.enaium.kookstarter.client.http.ChannelUserService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yukikyu.kook.boot.app.domain.obj.Channel;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yukikyu
 * @date: 2023-07-11 23:40
 */
@Slf4j
@Service
public class KookChannelUserService {

    @Autowired
    private ChannelUserService channelUserService;

    /**
     * 获取加入的频道
     *
     * @param target_id 服务器id
     * @param author_id 用户id
     * @return 所在频道
     */
    public List<Channel> getJoinedChannel(String target_id, String author_id) {
        // {"code":0,"message":"操作成功","data":{"items":[{"id":"5362986112653973","guild_id":"7363287837020870","master_id":"2431192162","parent_id":"8097939466864975","user_id":"2431192162","name":"游戏","topic":"","type":2,"level":200,"slow_mode":0,"last_msg_content":"","last_msg_id":"","has_password":false,"limit_amount":25,"is_category":false,"permission_sync":1,"permission_overwrites":[{"role_id":0,"allow":0,"deny":0}],"permission_users":[]}],"meta":{"page":1,"page_total":1,"page_size":50,"total":1},"sort":{}}}
        String channelUserGetJoinedChannelResponse = channelUserService
            .getChannelUserGetJoinedChannel(Map.of("guild_id", target_id, "user_id", author_id))
            .block();
        log.info("blocked:{}", channelUserGetJoinedChannelResponse);
        final var channelList = JSONUtil.parse(channelUserGetJoinedChannelResponse).getByPath("data.items", JSONArray.class);
        if (ObjectUtil.isEmpty(channelList)) {
            return null;
        }
        return JSONUtil.toList(channelList, Channel.class);
    }
}
