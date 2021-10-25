package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantPriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantPriceDTO.class);
        ProductVariantPriceDTO productVariantPriceDTO1 = new ProductVariantPriceDTO();
        productVariantPriceDTO1.setId(1L);
        ProductVariantPriceDTO productVariantPriceDTO2 = new ProductVariantPriceDTO();
        assertThat(productVariantPriceDTO1).isNotEqualTo(productVariantPriceDTO2);
        productVariantPriceDTO2.setId(productVariantPriceDTO1.getId());
        assertThat(productVariantPriceDTO1).isEqualTo(productVariantPriceDTO2);
        productVariantPriceDTO2.setId(2L);
        assertThat(productVariantPriceDTO1).isNotEqualTo(productVariantPriceDTO2);
        productVariantPriceDTO1.setId(null);
        assertThat(productVariantPriceDTO1).isNotEqualTo(productVariantPriceDTO2);
    }
}
