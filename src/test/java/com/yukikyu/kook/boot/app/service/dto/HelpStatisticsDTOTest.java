package com.yukikyu.kook.boot.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpStatisticsDTO.class);
        HelpStatisticsDTO helpStatisticsDTO1 = new HelpStatisticsDTO();
        helpStatisticsDTO1.setId(1L);
        HelpStatisticsDTO helpStatisticsDTO2 = new HelpStatisticsDTO();
        assertThat(helpStatisticsDTO1).isNotEqualTo(helpStatisticsDTO2);
        helpStatisticsDTO2.setId(helpStatisticsDTO1.getId());
        assertThat(helpStatisticsDTO1).isEqualTo(helpStatisticsDTO2);
        helpStatisticsDTO2.setId(2L);
        assertThat(helpStatisticsDTO1).isNotEqualTo(helpStatisticsDTO2);
        helpStatisticsDTO1.setId(null);
        assertThat(helpStatisticsDTO1).isNotEqualTo(helpStatisticsDTO2);
    }
}
