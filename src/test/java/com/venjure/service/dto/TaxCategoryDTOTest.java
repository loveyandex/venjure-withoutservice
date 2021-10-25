package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxCategoryDTO.class);
        TaxCategoryDTO taxCategoryDTO1 = new TaxCategoryDTO();
        taxCategoryDTO1.setId(1L);
        TaxCategoryDTO taxCategoryDTO2 = new TaxCategoryDTO();
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
        taxCategoryDTO2.setId(taxCategoryDTO1.getId());
        assertThat(taxCategoryDTO1).isEqualTo(taxCategoryDTO2);
        taxCategoryDTO2.setId(2L);
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
        taxCategoryDTO1.setId(null);
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
    }
}
