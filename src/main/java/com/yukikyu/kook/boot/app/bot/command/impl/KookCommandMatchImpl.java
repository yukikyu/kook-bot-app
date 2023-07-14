package com.yukikyu.kook.boot.app.bot.command.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yukikyu.kook.boot.app.bot.command.KookCommandMatch;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import com.yukikyu.kook.boot.app.contexts.LocalContexts;
import com.yukikyu.kook.boot.app.service.CustomCommandService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yukikyu
 * @date: 2023-07-08 15:00
 */
@Service
public class KookCommandMatchImpl implements KookCommandMatch {

    @Autowired
    private CustomCommandService customCommandService;

    @Override
    public boolean execute(KookCommandMatchType kookCommandMatchType, String content) {
        final boolean[] flag = { false };
        // 匹配自定义指令
        List<String> commandList = new ArrayList<>(kookCommandMatchType.getCommand());
        List<String> customCommandList = customCommandService.getCommand(kookCommandMatchType);
        if (customCommandList != null && CollectionUtil.isNotEmpty(customCommandList)) {
            CollectionUtil.addAll(commandList, customCommandList);
        }
        commandList.forEach(command -> {
            if (StrUtil.contains(content, command)) {
                LocalContexts.setCommand(command);
                flag[0] = true;
            }
        });
        return flag[0];
    }
}
