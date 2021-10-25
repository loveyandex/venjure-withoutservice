package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacetTranslationMapperTest {

    private FacetTranslationMapper facetTranslationMapper;

    @BeforeEach
    public void setUp() {
        facetTranslationMapper = new FacetTranslationMapperImpl();
    }
}
