package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetValueDTO.class);
        FacetValueDTO facetValueDTO1 = new FacetValueDTO();
        facetValueDTO1.setId(1L);
        FacetValueDTO facetValueDTO2 = new FacetValueDTO();
        assertThat(facetValueDTO1).isNotEqualTo(facetValueDTO2);
        facetValueDTO2.setId(facetValueDTO1.getId());
        assertThat(facetValueDTO1).isEqualTo(facetValueDTO2);
        facetValueDTO2.setId(2L);
        assertThat(facetValueDTO1).isNotEqualTo(facetValueDTO2);
        facetValueDTO1.setId(null);
        assertThat(facetValueDTO1).isNotEqualTo(facetValueDTO2);
    }
}
