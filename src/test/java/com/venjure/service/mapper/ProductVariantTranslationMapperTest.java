package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductVariantTranslationMapperTest {

    private ProductVariantTranslationMapper productVariantTranslationMapper;

    @BeforeEach
    public void setUp() {
        productVariantTranslationMapper = new ProductVariantTranslationMapperImpl();
    }
}
