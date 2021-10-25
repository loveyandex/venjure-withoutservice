package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingMethodTranslationMapperTest {

    private ShippingMethodTranslationMapper shippingMethodTranslationMapper;

    @BeforeEach
    public void setUp() {
        shippingMethodTranslationMapper = new ShippingMethodTranslationMapperImpl();
    }
}
