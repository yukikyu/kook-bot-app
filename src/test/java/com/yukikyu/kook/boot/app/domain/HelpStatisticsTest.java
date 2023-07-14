package com.yukikyu.kook.boot.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpStatistics.class);
        HelpStatistics helpStatistics1 = new HelpStatistics();
        helpStatistics1.setId(1L);
        HelpStatistics helpStatistics2 = new HelpStatistics();
        helpStatistics2.setId(helpStatistics1.getId());
        assertThat(helpStatistics1).isEqualTo(helpStatistics2);
        helpStatistics2.setId(2L);
        assertThat(helpStatistics1).isNotEqualTo(helpStatistics2);
        helpStatistics1.setId(null);
        assertThat(helpStatistics1).isNotEqualTo(helpStatistics2);
    }
}
