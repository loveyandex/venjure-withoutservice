package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTranslationDTO.class);
        ProductTranslationDTO productTranslationDTO1 = new ProductTranslationDTO();
        productTranslationDTO1.setId(1L);
        ProductTranslationDTO productTranslationDTO2 = new ProductTranslationDTO();
        assertThat(productTranslationDTO1).isNotEqualTo(productTranslationDTO2);
        productTranslationDTO2.setId(productTranslationDTO1.getId());
        assertThat(productTranslationDTO1).isEqualTo(productTranslationDTO2);
        productTranslationDTO2.setId(2L);
        assertThat(productTranslationDTO1).isNotEqualTo(productTranslationDTO2);
        productTranslationDTO1.setId(null);
        assertThat(productTranslationDTO1).isNotEqualTo(productTranslationDTO2);
    }
}
