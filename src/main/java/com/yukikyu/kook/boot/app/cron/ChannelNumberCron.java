package com.yukikyu.kook.boot.app.cron;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: yukikyu
 * @date: 2023-07-21 10:40
 */
@Slf4j
@Component
public class ChannelNumberCron {

    // @Scheduled(cron = "0 0 12 * * ?")
    public void refresh() {
        // 获取服务器
    }
}
