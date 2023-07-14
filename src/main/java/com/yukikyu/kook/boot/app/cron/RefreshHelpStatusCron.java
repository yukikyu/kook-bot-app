package com.yukikyu.kook.boot.app.cron;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yukikyu.kook.boot.app.constant.HelpLogStatus;
import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.obj.Channel;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import com.yukikyu.kook.boot.app.service.KookChannelUserService;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 刷新帮助状态
 *
 * @author: yukikyu
 * @date: 2023-07-11 23:19
 */
@Slf4j
@Component
public class RefreshHelpStatusCron {

    @Autowired
    private HelpUserLogRepository helpUserLogRepository;

    @Autowired
    private KookChannelUserService kookChannelUserService;

    @Scheduled(fixedRate = 60000)
    public void run() {
        log.info("开始刷新帮助状态");

        // your code logic here
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findByStatus(HelpLogStatus.STARTING.name()).collectList().block();
        if (CollectionUtil.isNotEmpty(helpUserLogList)) {
            helpUserLogList.forEach(helpUserLog -> {
                List<Channel> joinedChannel = kookChannelUserService.getJoinedChannel(helpUserLog.getGuildId(), helpUserLog.getUserId());
                if (
                    CollectionUtil.isNotEmpty(joinedChannel) && ObjectUtil.equals(joinedChannel.get(0).getId(), helpUserLog.getChannelId())
                ) {
                    helpUserLog.setExitAt(Instant.now());
                } else {
                    helpUserLog.setStatus(HelpLogStatus.ENDED.name());
                    helpUserLog.setExitAt(Instant.now());
                }
                helpUserLogRepository.save(helpUserLog).block();
            });
        }
    }
}
