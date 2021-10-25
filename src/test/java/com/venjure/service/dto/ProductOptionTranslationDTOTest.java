package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOptionTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOptionTranslationDTO.class);
        ProductOptionTranslationDTO productOptionTranslationDTO1 = new ProductOptionTranslationDTO();
        productOptionTranslationDTO1.setId(1L);
        ProductOptionTranslationDTO productOptionTranslationDTO2 = new ProductOptionTranslationDTO();
        assertThat(productOptionTranslationDTO1).isNotEqualTo(productOptionTranslationDTO2);
        productOptionTranslationDTO2.setId(productOptionTranslationDTO1.getId());
        assertThat(productOptionTranslationDTO1).isEqualTo(productOptionTranslationDTO2);
        productOptionTranslationDTO2.setId(2L);
        assertThat(productOptionTranslationDTO1).isNotEqualTo(productOptionTranslationDTO2);
        productOptionTranslationDTO1.setId(null);
        assertThat(productOptionTranslationDTO1).isNotEqualTo(productOptionTranslationDTO2);
    }
}
