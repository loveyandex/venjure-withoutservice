package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductVariantPriceMapperTest {

    private ProductVariantPriceMapper productVariantPriceMapper;

    @BeforeEach
    public void setUp() {
        productVariantPriceMapper = new ProductVariantPriceMapperImpl();
    }
}
