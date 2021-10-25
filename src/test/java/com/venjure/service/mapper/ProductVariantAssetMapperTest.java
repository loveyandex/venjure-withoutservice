package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductVariantAssetMapperTest {

    private ProductVariantAssetMapper productVariantAssetMapper;

    @BeforeEach
    public void setUp() {
        productVariantAssetMapper = new ProductVariantAssetMapperImpl();
    }
}
