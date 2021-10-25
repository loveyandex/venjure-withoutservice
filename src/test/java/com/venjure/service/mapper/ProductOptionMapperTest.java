package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOptionMapperTest {

    private ProductOptionMapper productOptionMapper;

    @BeforeEach
    public void setUp() {
        productOptionMapper = new ProductOptionMapperImpl();
    }
}
