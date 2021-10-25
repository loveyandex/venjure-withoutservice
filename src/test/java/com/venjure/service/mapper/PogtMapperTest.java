package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PogtMapperTest {

    private PogtMapper pogtMapper;

    @BeforeEach
    public void setUp() {
        pogtMapper = new PogtMapperImpl();
    }
}
