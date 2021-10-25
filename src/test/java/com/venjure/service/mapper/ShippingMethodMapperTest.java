package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingMethodMapperTest {

    private ShippingMethodMapper shippingMethodMapper;

    @BeforeEach
    public void setUp() {
        shippingMethodMapper = new ShippingMethodMapperImpl();
    }
}
