package com.yukikyu.kook.boot.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpUserLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpUserLog.class);
        HelpUserLog helpUserLog1 = new HelpUserLog();
        helpUserLog1.setId(1L);
        HelpUserLog helpUserLog2 = new HelpUserLog();
        helpUserLog2.setId(helpUserLog1.getId());
        assertThat(helpUserLog1).isEqualTo(helpUserLog2);
        helpUserLog2.setId(2L);
        assertThat(helpUserLog1).isNotEqualTo(helpUserLog2);
        helpUserLog1.setId(null);
        assertThat(helpUserLog1).isNotEqualTo(helpUserLog2);
    }
}
