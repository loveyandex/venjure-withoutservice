package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacetValueMapperTest {

    private FacetValueMapper facetValueMapper;

    @BeforeEach
    public void setUp() {
        facetValueMapper = new FacetValueMapperImpl();
    }
}
