package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionMapperTest {

    private CollectionMapper collectionMapper;

    @BeforeEach
    public void setUp() {
        collectionMapper = new CollectionMapperImpl();
    }
}
