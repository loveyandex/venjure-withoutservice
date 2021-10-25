package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOptionGroupMapperTest {

    private ProductOptionGroupMapper productOptionGroupMapper;

    @BeforeEach
    public void setUp() {
        productOptionGroupMapper = new ProductOptionGroupMapperImpl();
    }
}
