package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetDTO.class);
        FacetDTO facetDTO1 = new FacetDTO();
        facetDTO1.setId(1L);
        FacetDTO facetDTO2 = new FacetDTO();
        assertThat(facetDTO1).isNotEqualTo(facetDTO2);
        facetDTO2.setId(facetDTO1.getId());
        assertThat(facetDTO1).isEqualTo(facetDTO2);
        facetDTO2.setId(2L);
        assertThat(facetDTO1).isNotEqualTo(facetDTO2);
        facetDTO1.setId(null);
        assertThat(facetDTO1).isNotEqualTo(facetDTO2);
    }
}
