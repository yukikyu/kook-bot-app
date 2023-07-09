package com.yukikyu.kook.boot.app.bot.command.impl;

import cn.hutool.core.util.StrUtil;
import com.yukikyu.kook.boot.app.bot.command.Match;
import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;
import org.springframework.stereotype.Service;

/**
 * @author: yukikyu
 * @date: 2023-07-08 15:00
 */
@Service
public class MatchImpl implements Match {

    @Override
    public boolean execute(KookCommandMatchType kookCommandMatchType, String content) {
        if (StrUtil.contains(content, "//")) {
            return true;
        }
        return false;
    }
}
