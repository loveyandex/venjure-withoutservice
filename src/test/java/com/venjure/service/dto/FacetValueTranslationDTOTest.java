package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetValueTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetValueTranslationDTO.class);
        FacetValueTranslationDTO facetValueTranslationDTO1 = new FacetValueTranslationDTO();
        facetValueTranslationDTO1.setId(1L);
        FacetValueTranslationDTO facetValueTranslationDTO2 = new FacetValueTranslationDTO();
        assertThat(facetValueTranslationDTO1).isNotEqualTo(facetValueTranslationDTO2);
        facetValueTranslationDTO2.setId(facetValueTranslationDTO1.getId());
        assertThat(facetValueTranslationDTO1).isEqualTo(facetValueTranslationDTO2);
        facetValueTranslationDTO2.setId(2L);
        assertThat(facetValueTranslationDTO1).isNotEqualTo(facetValueTranslationDTO2);
        facetValueTranslationDTO1.setId(null);
        assertThat(facetValueTranslationDTO1).isNotEqualTo(facetValueTranslationDTO2);
    }
}
