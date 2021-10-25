package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantPrice.class);
        ProductVariantPrice productVariantPrice1 = new ProductVariantPrice();
        productVariantPrice1.setId(1L);
        ProductVariantPrice productVariantPrice2 = new ProductVariantPrice();
        productVariantPrice2.setId(productVariantPrice1.getId());
        assertThat(productVariantPrice1).isEqualTo(productVariantPrice2);
        productVariantPrice2.setId(2L);
        assertThat(productVariantPrice1).isNotEqualTo(productVariantPrice2);
        productVariantPrice1.setId(null);
        assertThat(productVariantPrice1).isNotEqualTo(productVariantPrice2);
    }
}
