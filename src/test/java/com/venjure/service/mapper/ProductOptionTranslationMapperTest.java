package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOptionTranslationMapperTest {

    private ProductOptionTranslationMapper productOptionTranslationMapper;

    @BeforeEach
    public void setUp() {
        productOptionTranslationMapper = new ProductOptionTranslationMapperImpl();
    }
}
