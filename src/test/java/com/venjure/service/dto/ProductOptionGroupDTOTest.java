package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOptionGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOptionGroupDTO.class);
        ProductOptionGroupDTO productOptionGroupDTO1 = new ProductOptionGroupDTO();
        productOptionGroupDTO1.setId(1L);
        ProductOptionGroupDTO productOptionGroupDTO2 = new ProductOptionGroupDTO();
        assertThat(productOptionGroupDTO1).isNotEqualTo(productOptionGroupDTO2);
        productOptionGroupDTO2.setId(productOptionGroupDTO1.getId());
        assertThat(productOptionGroupDTO1).isEqualTo(productOptionGroupDTO2);
        productOptionGroupDTO2.setId(2L);
        assertThat(productOptionGroupDTO1).isNotEqualTo(productOptionGroupDTO2);
        productOptionGroupDTO1.setId(null);
        assertThat(productOptionGroupDTO1).isNotEqualTo(productOptionGroupDTO2);
    }
}
