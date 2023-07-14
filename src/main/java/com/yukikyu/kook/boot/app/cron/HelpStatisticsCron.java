package com.yukikyu.kook.boot.app.cron;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.criteria.HelpUserLogCriteria;
import com.yukikyu.kook.boot.app.repository.HelpStatisticsRepository;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: yukikyu
 * @date: 2023-07-12 2:01
 */
@Slf4j
@Component
public class HelpStatisticsCron {

    @Autowired
    private HelpUserLogRepository helpUserLogRepository;

    @Autowired
    private HelpStatisticsRepository helpStatisticsRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每天
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void day() {}

    /**
     * 每个月
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void month() {
        HelpUserLogCriteria helpUserLogCriteria = new HelpUserLogCriteria();
        LocalDate today = LocalDate.now(); // 获取当前日期
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1); // 获取上个月的第一天
        Instant firstInstant = firstDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth()); // 获取上个月的最后一天
        Instant lastinstant = lastDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant

        helpUserLogCriteria.joinAt().setGreaterThan(firstInstant);
        helpUserLogCriteria.joinAt().setLessThan(lastinstant);

        // 查询上个月数据
        List<HelpUserLog> helpUserLogList = helpUserLogRepository
            .findByCriteria(helpUserLogCriteria, null)
            .collectList()
            .doOnSuccess(helpUserLogs -> {
                helpUserLogs.forEach(helpUserLog -> log.info(helpUserLog.toString()));
            })
            .block();
        if (helpUserLogList != null) {
            // 服务器分组
            Map<String, List<HelpUserLog>> guildIdGroupBy = helpUserLogList
                .stream()
                .collect(Collectors.groupingBy(HelpUserLog::getGuildId));
            guildIdGroupBy.forEach((guildId, helpUL) -> {
                // 人员分组
                Map<String, List<HelpUserLog>> helpUserLogGroupByHelpUserId = helpUL
                    .stream()
                    .collect(Collectors.groupingBy(HelpUserLog::getHelpUserId));
                helpUserLogGroupByHelpUserId.forEach((helpUserId, hulList) -> {
                    int durationSeconds = hulList
                        .stream()
                        .map(item -> {
                            Duration duration = Duration.between(item.getJoinAt(), item.getExitAt()); // 计算时间差
                            return duration.getSeconds();
                        })
                        .mapToInt(Long::intValue)
                        .sum();
                    HelpStatistics entity = new HelpStatistics();
                    entity.setHelpUserId(helpUserId);
                    entity.setMonth(firstInstant);
                    entity.setGuildId(guildId);
                    entity.setDuration(durationSeconds);
                    helpStatisticsRepository.save(entity).block();
                });
            });
        } else {
            HelpStatistics entity = new HelpStatistics();
            entity.setHelpUserId("0");
            entity.setGuildId("0");
            entity.setDuration(0);
            entity.setMonth(firstInstant);
            helpStatisticsRepository.save(entity).block();
        }
    }
}
