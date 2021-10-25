package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JorderMapperTest {

    private JorderMapper jorderMapper;

    @BeforeEach
    public void setUp() {
        jorderMapper = new JorderMapperImpl();
    }
}
