package com.yukikyu.kook.boot.app.contexts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author: yukikyu
 * @date: 2023-07-13 20:20
 */
@Component
public class LocalContexts {

    /**
     * 当前服务器ID
     */
    @Getter
    @Setter
    private static String guildId;

    /**
     * 当前指令
     */
    @Getter
    @Setter
    private static String command;
}
