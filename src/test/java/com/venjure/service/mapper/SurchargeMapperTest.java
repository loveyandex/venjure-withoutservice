package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SurchargeMapperTest {

    private SurchargeMapper surchargeMapper;

    @BeforeEach
    public void setUp() {
        surchargeMapper = new SurchargeMapperImpl();
    }
}
