package com.yukikyu.kook.boot.app.cron;

import com.yukikyu.kook.boot.app.business.HelpStatisticsBusiness;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    HelpStatisticsBusiness helpStatisticsBusiness;

    /**
     * 每天
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void day() {
        // 刷新当月数据
        helpStatisticsBusiness.refresh(LocalDate.now());
    }

    /**
     * 每个月
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void month() {
        // 刷新上个月数据
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 刷新
        helpStatisticsBusiness.refresh(localDate);
    }
}
