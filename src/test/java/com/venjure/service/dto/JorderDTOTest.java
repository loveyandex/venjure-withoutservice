package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JorderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JorderDTO.class);
        JorderDTO jorderDTO1 = new JorderDTO();
        jorderDTO1.setId(1L);
        JorderDTO jorderDTO2 = new JorderDTO();
        assertThat(jorderDTO1).isNotEqualTo(jorderDTO2);
        jorderDTO2.setId(jorderDTO1.getId());
        assertThat(jorderDTO1).isEqualTo(jorderDTO2);
        jorderDTO2.setId(2L);
        assertThat(jorderDTO1).isNotEqualTo(jorderDTO2);
        jorderDTO1.setId(null);
        assertThat(jorderDTO1).isNotEqualTo(jorderDTO2);
    }
}
