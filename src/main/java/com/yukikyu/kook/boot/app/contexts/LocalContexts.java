package com.yukikyu.kook.boot.app.contexts;

import com.yukikyu.kook.boot.app.domain.obj.MessageRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author: yukikyu
 * @date: 2023-07-13 20:20
 */
public class LocalContexts {

    public static final ThreadLocal<Contexts> contextsThreadLocal = ThreadLocal.withInitial(Contexts::new);

    public static class Contexts {

        /**
         * 当前服务器ID
         */
        @Getter
        @Setter
        private String guildId;

        /**
         * 频道ID
         */
        @Getter
        @Setter
        private String chatChannelId;

        /**
         * 当前指令
         */
        @Getter
        @Setter
        private String command;

        /**
         * 消息请求
         */
        @Getter
        @Setter
        private List<MessageRequest> messageRequestList = new ArrayList<>();
    }
}
