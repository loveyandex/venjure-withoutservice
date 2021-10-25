package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryTranslationMapperTest {

    private CountryTranslationMapper countryTranslationMapper;

    @BeforeEach
    public void setUp() {
        countryTranslationMapper = new CountryTranslationMapperImpl();
    }
}
