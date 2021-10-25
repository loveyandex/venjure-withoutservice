package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GlobalSettingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalSettingsDTO.class);
        GlobalSettingsDTO globalSettingsDTO1 = new GlobalSettingsDTO();
        globalSettingsDTO1.setId(1L);
        GlobalSettingsDTO globalSettingsDTO2 = new GlobalSettingsDTO();
        assertThat(globalSettingsDTO1).isNotEqualTo(globalSettingsDTO2);
        globalSettingsDTO2.setId(globalSettingsDTO1.getId());
        assertThat(globalSettingsDTO1).isEqualTo(globalSettingsDTO2);
        globalSettingsDTO2.setId(2L);
        assertThat(globalSettingsDTO1).isNotEqualTo(globalSettingsDTO2);
        globalSettingsDTO1.setId(null);
        assertThat(globalSettingsDTO1).isNotEqualTo(globalSettingsDTO2);
    }
}
