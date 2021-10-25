package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacetMapperTest {

    private FacetMapper facetMapper;

    @BeforeEach
    public void setUp() {
        facetMapper = new FacetMapperImpl();
    }
}
