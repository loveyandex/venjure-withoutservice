package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductVariantAssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantAssetDTO.class);
        ProductVariantAssetDTO productVariantAssetDTO1 = new ProductVariantAssetDTO();
        productVariantAssetDTO1.setId(1L);
        ProductVariantAssetDTO productVariantAssetDTO2 = new ProductVariantAssetDTO();
        assertThat(productVariantAssetDTO1).isNotEqualTo(productVariantAssetDTO2);
        productVariantAssetDTO2.setId(productVariantAssetDTO1.getId());
        assertThat(productVariantAssetDTO1).isEqualTo(productVariantAssetDTO2);
        productVariantAssetDTO2.setId(2L);
        assertThat(productVariantAssetDTO1).isNotEqualTo(productVariantAssetDTO2);
        productVariantAssetDTO1.setId(null);
        assertThat(productVariantAssetDTO1).isNotEqualTo(productVariantAssetDTO2);
    }
}
