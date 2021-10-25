package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurchargeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurchargeDTO.class);
        SurchargeDTO surchargeDTO1 = new SurchargeDTO();
        surchargeDTO1.setId(1L);
        SurchargeDTO surchargeDTO2 = new SurchargeDTO();
        assertThat(surchargeDTO1).isNotEqualTo(surchargeDTO2);
        surchargeDTO2.setId(surchargeDTO1.getId());
        assertThat(surchargeDTO1).isEqualTo(surchargeDTO2);
        surchargeDTO2.setId(2L);
        assertThat(surchargeDTO1).isNotEqualTo(surchargeDTO2);
        surchargeDTO1.setId(null);
        assertThat(surchargeDTO1).isNotEqualTo(surchargeDTO2);
    }
}
