package com.yukikyu.kook.boot.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpUserLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpUserLogDTO.class);
        HelpUserLogDTO helpUserLogDTO1 = new HelpUserLogDTO();
        helpUserLogDTO1.setId(1L);
        HelpUserLogDTO helpUserLogDTO2 = new HelpUserLogDTO();
        assertThat(helpUserLogDTO1).isNotEqualTo(helpUserLogDTO2);
        helpUserLogDTO2.setId(helpUserLogDTO1.getId());
        assertThat(helpUserLogDTO1).isEqualTo(helpUserLogDTO2);
        helpUserLogDTO2.setId(2L);
        assertThat(helpUserLogDTO1).isNotEqualTo(helpUserLogDTO2);
        helpUserLogDTO1.setId(null);
        assertThat(helpUserLogDTO1).isNotEqualTo(helpUserLogDTO2);
    }
}
