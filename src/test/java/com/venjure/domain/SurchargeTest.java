package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurchargeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Surcharge.class);
        Surcharge surcharge1 = new Surcharge();
        surcharge1.setId(1L);
        Surcharge surcharge2 = new Surcharge();
        surcharge2.setId(surcharge1.getId());
        assertThat(surcharge1).isEqualTo(surcharge2);
        surcharge2.setId(2L);
        assertThat(surcharge1).isNotEqualTo(surcharge2);
        surcharge1.setId(null);
        assertThat(surcharge1).isNotEqualTo(surcharge2);
    }
}
