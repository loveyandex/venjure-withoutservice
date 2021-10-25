package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionAssetMapperTest {

    private CollectionAssetMapper collectionAssetMapper;

    @BeforeEach
    public void setUp() {
        collectionAssetMapper = new CollectionAssetMapperImpl();
    }
}
