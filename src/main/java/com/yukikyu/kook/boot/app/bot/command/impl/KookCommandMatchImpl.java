package com.yukikyu.kook.boot.app.bot.command.impl;

import cn.hutool.core.util.StrUtil;
import com.yukikyu.kook.boot.app.bot.command.KookCommandMatch;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import org.springframework.stereotype.Service;

/**
 * @author: yukikyu
 * @date: 2023-07-08 15:00
 */
@Service
public class KookCommandMatchImpl implements KookCommandMatch {

    @Override
    public boolean execute(KookCommandMatchType kookCommandMatchType, String content) {
        final boolean[] flag = { false };
        kookCommandMatchType
            .getCommand()
            .forEach(command -> {
                if (StrUtil.contains(content, command)) {
                    flag[0] = true;
                }
            });
        return flag[0];
    }
}
