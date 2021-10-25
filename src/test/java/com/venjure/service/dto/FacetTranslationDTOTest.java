package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetTranslationDTO.class);
        FacetTranslationDTO facetTranslationDTO1 = new FacetTranslationDTO();
        facetTranslationDTO1.setId(1L);
        FacetTranslationDTO facetTranslationDTO2 = new FacetTranslationDTO();
        assertThat(facetTranslationDTO1).isNotEqualTo(facetTranslationDTO2);
        facetTranslationDTO2.setId(facetTranslationDTO1.getId());
        assertThat(facetTranslationDTO1).isEqualTo(facetTranslationDTO2);
        facetTranslationDTO2.setId(2L);
        assertThat(facetTranslationDTO1).isNotEqualTo(facetTranslationDTO2);
        facetTranslationDTO1.setId(null);
        assertThat(facetTranslationDTO1).isNotEqualTo(facetTranslationDTO2);
    }
}
