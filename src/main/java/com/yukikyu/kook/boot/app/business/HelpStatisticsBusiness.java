package com.yukikyu.kook.boot.app.business;

import cn.hutool.core.collection.CollectionUtil;
import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
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
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.InstantFilter;

/**
 * @author: yukikyu
 * @date: 2023-07-17 17:31
 */
@Slf4j
@Service
public class HelpStatisticsBusiness {

    @Autowired
    private HelpUserLogRepository helpUserLogRepository;

    @Autowired
    private HelpStatisticsRepository helpStatisticsRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 刷新某个月的统计数据数据
     *
     * @param localDate
     */
    public void refresh(LocalDate localDate) {
        if (localDate == null) {
            localDate = LocalDate.now(); // 获取当前日期
        }
        LocalDate firstDayOfLastMonth = localDate.withDayOfMonth(1); // 获取上个月的第一天
        Instant firstInstant = firstDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
        LocalDate lastDayOfLastMonth = localDate.withDayOfMonth(localDate.lengthOfMonth()); // 获取上个月的最后一天
        Instant lastinstant = lastDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant(); // 转换为Instant
        HelpUserLogCriteria helpUserLogCriteria = new HelpUserLogCriteria();
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
        // 存在数据
        if (helpUserLogList != null) {
            // 删除原有上月数据
            HelpStatisticsCriteria helpStatisticsCriteria = new HelpStatisticsCriteria();
            InstantFilter month = new InstantFilter();
            month.setGreaterThan(firstInstant);
            month.setLessThan(lastinstant);
            helpStatisticsCriteria.setMonth(month);
            List<HelpStatistics> helpStatisticsList = helpStatisticsRepository
                .findByCriteria(helpStatisticsCriteria, null)
                .collectList()
                .block();
            if (CollectionUtil.isNotEmpty(helpStatisticsList)) {
                helpStatisticsRepository.deleteAllById(helpStatisticsList.stream().map(HelpStatistics::getId).toList()).block();
            }

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
