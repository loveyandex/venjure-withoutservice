package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAsset.class);
        ProductAsset productAsset1 = new ProductAsset();
        productAsset1.setId(1L);
        ProductAsset productAsset2 = new ProductAsset();
        productAsset2.setId(productAsset1.getId());
        assertThat(productAsset1).isEqualTo(productAsset2);
        productAsset2.setId(2L);
        assertThat(productAsset1).isNotEqualTo(productAsset2);
        productAsset1.setId(null);
        assertThat(productAsset1).isNotEqualTo(productAsset2);
    }
}
