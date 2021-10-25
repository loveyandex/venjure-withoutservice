package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderModificationMapperTest {

    private OrderModificationMapper orderModificationMapper;

    @BeforeEach
    public void setUp() {
        orderModificationMapper = new OrderModificationMapperImpl();
    }
}
