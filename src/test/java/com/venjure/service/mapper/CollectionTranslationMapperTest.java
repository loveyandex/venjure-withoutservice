package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTranslationMapperTest {

    private CollectionTranslationMapper collectionTranslationMapper;

    @BeforeEach
    public void setUp() {
        collectionTranslationMapper = new CollectionTranslationMapperImpl();
    }
}
