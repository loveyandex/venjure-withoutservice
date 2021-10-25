package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacetValueTranslationMapperTest {

    private FacetValueTranslationMapper facetValueTranslationMapper;

    @BeforeEach
    public void setUp() {
        facetValueTranslationMapper = new FacetValueTranslationMapperImpl();
    }
}
