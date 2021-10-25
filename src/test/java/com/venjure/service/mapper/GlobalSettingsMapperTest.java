package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlobalSettingsMapperTest {

    private GlobalSettingsMapper globalSettingsMapper;

    @BeforeEach
    public void setUp() {
        globalSettingsMapper = new GlobalSettingsMapperImpl();
    }
}
