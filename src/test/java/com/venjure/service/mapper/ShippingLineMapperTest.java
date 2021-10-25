package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingLineMapperTest {

    private ShippingLineMapper shippingLineMapper;

    @BeforeEach
    public void setUp() {
        shippingLineMapper = new ShippingLineMapperImpl();
    }
}
