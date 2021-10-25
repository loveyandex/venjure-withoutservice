package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantTranslation.class);
        ProductVariantTranslation productVariantTranslation1 = new ProductVariantTranslation();
        productVariantTranslation1.setId(1L);
        ProductVariantTranslation productVariantTranslation2 = new ProductVariantTranslation();
        productVariantTranslation2.setId(productVariantTranslation1.getId());
        assertThat(productVariantTranslation1).isEqualTo(productVariantTranslation2);
        productVariantTranslation2.setId(2L);
        assertThat(productVariantTranslation1).isNotEqualTo(productVariantTranslation2);
        productVariantTranslation1.setId(null);
        assertThat(productVariantTranslation1).isNotEqualTo(productVariantTranslation2);
    }
}
