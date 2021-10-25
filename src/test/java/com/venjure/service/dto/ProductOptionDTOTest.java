package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOptionDTO.class);
        ProductOptionDTO productOptionDTO1 = new ProductOptionDTO();
        productOptionDTO1.setId(1L);
        ProductOptionDTO productOptionDTO2 = new ProductOptionDTO();
        assertThat(productOptionDTO1).isNotEqualTo(productOptionDTO2);
        productOptionDTO2.setId(productOptionDTO1.getId());
        assertThat(productOptionDTO1).isEqualTo(productOptionDTO2);
        productOptionDTO2.setId(2L);
        assertThat(productOptionDTO1).isNotEqualTo(productOptionDTO2);
        productOptionDTO1.setId(null);
        assertThat(productOptionDTO1).isNotEqualTo(productOptionDTO2);
    }
}
