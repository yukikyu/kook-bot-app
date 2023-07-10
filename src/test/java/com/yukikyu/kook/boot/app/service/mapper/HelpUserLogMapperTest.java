package com.yukikyu.kook.boot.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpUserLogMapperTest {

    private HelpUserLogMapper helpUserLogMapper;

    @BeforeEach
    public void setUp() {
        helpUserLogMapper = new HelpUserLogMapperImpl();
    }
}
