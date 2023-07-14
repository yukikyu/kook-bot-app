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

    @Getter
    @Setter
    private static String guildId;
}
