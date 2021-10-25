package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantTranslationDTO.class);
        ProductVariantTranslationDTO productVariantTranslationDTO1 = new ProductVariantTranslationDTO();
        productVariantTranslationDTO1.setId(1L);
        ProductVariantTranslationDTO productVariantTranslationDTO2 = new ProductVariantTranslationDTO();
        assertThat(productVariantTranslationDTO1).isNotEqualTo(productVariantTranslationDTO2);
        productVariantTranslationDTO2.setId(productVariantTranslationDTO1.getId());
        assertThat(productVariantTranslationDTO1).isEqualTo(productVariantTranslationDTO2);
        productVariantTranslationDTO2.setId(2L);
        assertThat(productVariantTranslationDTO1).isNotEqualTo(productVariantTranslationDTO2);
        productVariantTranslationDTO1.setId(null);
        assertThat(productVariantTranslationDTO1).isNotEqualTo(productVariantTranslationDTO2);
    }
}
