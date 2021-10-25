package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetTranslation.class);
        FacetTranslation facetTranslation1 = new FacetTranslation();
        facetTranslation1.setId(1L);
        FacetTranslation facetTranslation2 = new FacetTranslation();
        facetTranslation2.setId(facetTranslation1.getId());
        assertThat(facetTranslation1).isEqualTo(facetTranslation2);
        facetTranslation2.setId(2L);
        assertThat(facetTranslation1).isNotEqualTo(facetTranslation2);
        facetTranslation1.setId(null);
        assertThat(facetTranslation1).isNotEqualTo(facetTranslation2);
    }
}
