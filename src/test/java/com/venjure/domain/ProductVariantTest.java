package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariant.class);
        ProductVariant productVariant1 = new ProductVariant();
        productVariant1.setId(1L);
        ProductVariant productVariant2 = new ProductVariant();
        productVariant2.setId(productVariant1.getId());
        assertThat(productVariant1).isEqualTo(productVariant2);
        productVariant2.setId(2L);
        assertThat(productVariant1).isNotEqualTo(productVariant2);
        productVariant1.setId(null);
        assertThat(productVariant1).isNotEqualTo(productVariant2);
    }
}
