package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOptionTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOptionTranslation.class);
        ProductOptionTranslation productOptionTranslation1 = new ProductOptionTranslation();
        productOptionTranslation1.setId(1L);
        ProductOptionTranslation productOptionTranslation2 = new ProductOptionTranslation();
        productOptionTranslation2.setId(productOptionTranslation1.getId());
        assertThat(productOptionTranslation1).isEqualTo(productOptionTranslation2);
        productOptionTranslation2.setId(2L);
        assertThat(productOptionTranslation1).isNotEqualTo(productOptionTranslation2);
        productOptionTranslation1.setId(null);
        assertThat(productOptionTranslation1).isNotEqualTo(productOptionTranslation2);
    }
}
