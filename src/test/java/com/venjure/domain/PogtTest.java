package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PogtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pogt.class);
        Pogt pogt1 = new Pogt();
        pogt1.setId(1L);
        Pogt pogt2 = new Pogt();
        pogt2.setId(pogt1.getId());
        assertThat(pogt1).isEqualTo(pogt2);
        pogt2.setId(2L);
        assertThat(pogt1).isNotEqualTo(pogt2);
        pogt1.setId(null);
        assertThat(pogt1).isNotEqualTo(pogt2);
    }
}
