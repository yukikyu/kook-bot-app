package com.yukikyu.kook.boot.app.bot.command;

import com.yukikyu.kook.boot.app.constant.KookCommandMatchType;

/**
 * 匹配
 *
 * @author: yukikyu
 * @date: 2023-07-08 14:56
 */
public interface KookCommandMatch {
    /**
     * 执行
     *
     * @param kookCommandMatchType 匹配类型
     * @param content 匹配内容
     * @return 是否匹配
     */
    boolean execute(KookCommandMatchType kookCommandMatchType, String content);
}
