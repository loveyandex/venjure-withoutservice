package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PogtDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PogtDTO.class);
        PogtDTO pogtDTO1 = new PogtDTO();
        pogtDTO1.setId(1L);
        PogtDTO pogtDTO2 = new PogtDTO();
        assertThat(pogtDTO1).isNotEqualTo(pogtDTO2);
        pogtDTO2.setId(pogtDTO1.getId());
        assertThat(pogtDTO1).isEqualTo(pogtDTO2);
        pogtDTO2.setId(2L);
        assertThat(pogtDTO1).isNotEqualTo(pogtDTO2);
        pogtDTO1.setId(null);
        assertThat(pogtDTO1).isNotEqualTo(pogtDTO2);
    }
}
