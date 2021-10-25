package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOptionGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOptionGroup.class);
        ProductOptionGroup productOptionGroup1 = new ProductOptionGroup();
        productOptionGroup1.setId(1L);
        ProductOptionGroup productOptionGroup2 = new ProductOptionGroup();
        productOptionGroup2.setId(productOptionGroup1.getId());
        assertThat(productOptionGroup1).isEqualTo(productOptionGroup2);
        productOptionGroup2.setId(2L);
        assertThat(productOptionGroup1).isNotEqualTo(productOptionGroup2);
        productOptionGroup1.setId(null);
        assertThat(productOptionGroup1).isNotEqualTo(productOptionGroup2);
    }
}
