package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryTranslationDTO.class);
        CountryTranslationDTO countryTranslationDTO1 = new CountryTranslationDTO();
        countryTranslationDTO1.setId(1L);
        CountryTranslationDTO countryTranslationDTO2 = new CountryTranslationDTO();
        assertThat(countryTranslationDTO1).isNotEqualTo(countryTranslationDTO2);
        countryTranslationDTO2.setId(countryTranslationDTO1.getId());
        assertThat(countryTranslationDTO1).isEqualTo(countryTranslationDTO2);
        countryTranslationDTO2.setId(2L);
        assertThat(countryTranslationDTO1).isNotEqualTo(countryTranslationDTO2);
        countryTranslationDTO1.setId(null);
        assertThat(countryTranslationDTO1).isNotEqualTo(countryTranslationDTO2);
    }
}
