package com.yukikyu.kook.boot.app.service;

import cn.hutool.core.collection.CollectionUtil;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import com.yukikyu.kook.boot.app.contexts.LocalContexts;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 自定义指令
 *
 * @author: yukikyu
 * @date: 2023-07-13 20:29
 */
@Service
public class CustomCommandService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取命令
     *
     * @param kookCommandMatchType 命令类型
     * @return
     */
    public List<String> getCommand(KookCommandMatchType kookCommandMatchType) {
        Set<Object> commandSet = redisTemplate
            .opsForSet()
            .members(LocalContexts.contextsThreadLocal.get().getGuildId() + "::COMMAND::" + kookCommandMatchType.name());
        if (commandSet == null || CollectionUtil.isEmpty(commandSet)) {
            return null;
        }
        return commandSet.stream().map(String::valueOf).toList();
    }

    /**
     * 保存命令
     *
     * @param kookCommandMatchType 命令类型
     * @param commands 命令
     * @return
     */
    public void setCommand(KookCommandMatchType kookCommandMatchType, String... commands) {
        redisTemplate
            .opsForSet()
            .add(LocalContexts.contextsThreadLocal.get().getGuildId() + "::COMMAND::" + kookCommandMatchType.name(), commands);
    }
}
