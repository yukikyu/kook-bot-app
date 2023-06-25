package com.yukikyu.kook.boot.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventContent.class);
        EventContent eventContent1 = new EventContent();
        eventContent1.setId(1L);
        EventContent eventContent2 = new EventContent();
        eventContent2.setId(eventContent1.getId());
        assertThat(eventContent1).isEqualTo(eventContent2);
        eventContent2.setId(2L);
        assertThat(eventContent1).isNotEqualTo(eventContent2);
        eventContent1.setId(null);
        assertThat(eventContent1).isNotEqualTo(eventContent2);
    }
}
