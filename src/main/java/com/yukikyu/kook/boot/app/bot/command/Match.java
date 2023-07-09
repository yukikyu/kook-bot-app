package com.yukikyu.kook.boot.app.bot.command;

import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;

/**
 * 匹配
 *
 * @author: yukikyu
 * @date: 2023-07-08 14:56
 */
public interface Match {
    boolean execute(KookCommandMatchType kookCommandMatchType, String content);
}
