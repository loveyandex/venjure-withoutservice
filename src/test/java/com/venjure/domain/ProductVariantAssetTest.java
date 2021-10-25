package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantAssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantAsset.class);
        ProductVariantAsset productVariantAsset1 = new ProductVariantAsset();
        productVariantAsset1.setId(1L);
        ProductVariantAsset productVariantAsset2 = new ProductVariantAsset();
        productVariantAsset2.setId(productVariantAsset1.getId());
        assertThat(productVariantAsset1).isEqualTo(productVariantAsset2);
        productVariantAsset2.setId(2L);
        assertThat(productVariantAsset1).isNotEqualTo(productVariantAsset2);
        productVariantAsset1.setId(null);
        assertThat(productVariantAsset1).isNotEqualTo(productVariantAsset2);
    }
}
