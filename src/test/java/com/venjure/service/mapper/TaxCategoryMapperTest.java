package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaxCategoryMapperTest {

    private TaxCategoryMapper taxCategoryMapper;

    @BeforeEach
    public void setUp() {
        taxCategoryMapper = new TaxCategoryMapperImpl();
    }
}
