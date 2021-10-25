package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JorderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jorder.class);
        Jorder jorder1 = new Jorder();
        jorder1.setId(1L);
        Jorder jorder2 = new Jorder();
        jorder2.setId(jorder1.getId());
        assertThat(jorder1).isEqualTo(jorder2);
        jorder2.setId(2L);
        assertThat(jorder1).isNotEqualTo(jorder2);
        jorder1.setId(null);
        assertThat(jorder1).isNotEqualTo(jorder2);
    }
}
