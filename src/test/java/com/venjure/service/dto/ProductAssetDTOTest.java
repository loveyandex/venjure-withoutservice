package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAssetDTO.class);
        ProductAssetDTO productAssetDTO1 = new ProductAssetDTO();
        productAssetDTO1.setId(1L);
        ProductAssetDTO productAssetDTO2 = new ProductAssetDTO();
        assertThat(productAssetDTO1).isNotEqualTo(productAssetDTO2);
        productAssetDTO2.setId(productAssetDTO1.getId());
        assertThat(productAssetDTO1).isEqualTo(productAssetDTO2);
        productAssetDTO2.setId(2L);
        assertThat(productAssetDTO1).isNotEqualTo(productAssetDTO2);
        productAssetDTO1.setId(null);
        assertThat(productAssetDTO1).isNotEqualTo(productAssetDTO2);
    }
}
