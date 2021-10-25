package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductAssetMapperTest {

    private ProductAssetMapper productAssetMapper;

    @BeforeEach
    public void setUp() {
        productAssetMapper = new ProductAssetMapperImpl();
    }
}
