package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryTranslation.class);
        CountryTranslation countryTranslation1 = new CountryTranslation();
        countryTranslation1.setId(1L);
        CountryTranslation countryTranslation2 = new CountryTranslation();
        countryTranslation2.setId(countryTranslation1.getId());
        assertThat(countryTranslation1).isEqualTo(countryTranslation2);
        countryTranslation2.setId(2L);
        assertThat(countryTranslation1).isNotEqualTo(countryTranslation2);
        countryTranslation1.setId(null);
        assertThat(countryTranslation1).isNotEqualTo(countryTranslation2);
    }
}
