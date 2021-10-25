package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facet.class);
        Facet facet1 = new Facet();
        facet1.setId(1L);
        Facet facet2 = new Facet();
        facet2.setId(facet1.getId());
        assertThat(facet1).isEqualTo(facet2);
        facet2.setId(2L);
        assertThat(facet1).isNotEqualTo(facet2);
        facet1.setId(null);
        assertThat(facet1).isNotEqualTo(facet2);
    }
}
