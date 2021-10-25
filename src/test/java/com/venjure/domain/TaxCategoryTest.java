package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxCategory.class);
        TaxCategory taxCategory1 = new TaxCategory();
        taxCategory1.setId(1L);
        TaxCategory taxCategory2 = new TaxCategory();
        taxCategory2.setId(taxCategory1.getId());
        assertThat(taxCategory1).isEqualTo(taxCategory2);
        taxCategory2.setId(2L);
        assertThat(taxCategory1).isNotEqualTo(taxCategory2);
        taxCategory1.setId(null);
        assertThat(taxCategory1).isNotEqualTo(taxCategory2);
    }
}
