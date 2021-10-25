package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FulfillmentMapperTest {

    private FulfillmentMapper fulfillmentMapper;

    @BeforeEach
    public void setUp() {
        fulfillmentMapper = new FulfillmentMapperImpl();
    }
}
