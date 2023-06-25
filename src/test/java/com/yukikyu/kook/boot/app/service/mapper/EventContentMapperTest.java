package com.yukikyu.kook.boot.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventContentMapperTest {

    private EventContentMapper eventContentMapper;

    @BeforeEach
    public void setUp() {
        eventContentMapper = new EventContentMapperImpl();
    }
}
