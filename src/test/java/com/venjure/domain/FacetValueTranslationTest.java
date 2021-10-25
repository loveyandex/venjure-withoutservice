package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetValueTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacetValueTranslation.class);
        FacetValueTranslation facetValueTranslation1 = new FacetValueTranslation();
        facetValueTranslation1.setId(1L);
        FacetValueTranslation facetValueTranslation2 = new FacetValueTranslation();
        facetValueTranslation2.setId(facetValueTranslation1.getId());
        assertThat(facetValueTranslation1).isEqualTo(facetValueTranslation2);
        facetValueTranslation2.setId(2L);
        assertThat(facetValueTranslation1).isNotEqualTo(facetValueTranslation2);
        facetValueTranslation1.setId(null);
        assertThat(facetValueTranslation1).isNotEqualTo(facetValueTranslation2);
    }
}
