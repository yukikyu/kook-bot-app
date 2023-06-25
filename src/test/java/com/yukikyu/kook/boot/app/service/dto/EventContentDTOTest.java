package com.yukikyu.kook.boot.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yukikyu.kook.boot.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventContentDTO.class);
        EventContentDTO eventContentDTO1 = new EventContentDTO();
        eventContentDTO1.setId(1L);
        EventContentDTO eventContentDTO2 = new EventContentDTO();
        assertThat(eventContentDTO1).isNotEqualTo(eventContentDTO2);
        eventContentDTO2.setId(eventContentDTO1.getId());
        assertThat(eventContentDTO1).isEqualTo(eventContentDTO2);
        eventContentDTO2.setId(2L);
        assertThat(eventContentDTO1).isNotEqualTo(eventContentDTO2);
        eventContentDTO1.setId(null);
        assertThat(eventContentDTO1).isNotEqualTo(eventContentDTO2);
    }
}
