package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetValue.class);
        FacetValue facetValue1 = new FacetValue();
        facetValue1.setId(1L);
        FacetValue facetValue2 = new FacetValue();
        facetValue2.setId(facetValue1.getId());
        assertThat(facetValue1).isEqualTo(facetValue2);
        facetValue2.setId(2L);
        assertThat(facetValue1).isNotEqualTo(facetValue2);
        facetValue1.setId(null);
        assertThat(facetValue1).isNotEqualTo(facetValue2);
    }
}
