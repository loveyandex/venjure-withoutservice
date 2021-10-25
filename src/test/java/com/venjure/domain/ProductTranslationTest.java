package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTranslation.class);
        ProductTranslation productTranslation1 = new ProductTranslation();
        productTranslation1.setId(1L);
        ProductTranslation productTranslation2 = new ProductTranslation();
        productTranslation2.setId(productTranslation1.getId());
        assertThat(productTranslation1).isEqualTo(productTranslation2);
        productTranslation2.setId(2L);
        assertThat(productTranslation1).isNotEqualTo(productTranslation2);
        productTranslation1.setId(null);
        assertThat(productTranslation1).isNotEqualTo(productTranslation2);
    }
}
