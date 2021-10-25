package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExampleEntityMapperTest {

    private ExampleEntityMapper exampleEntityMapper;

    @BeforeEach
    public void setUp() {
        exampleEntityMapper = new ExampleEntityMapperImpl();
    }
}
