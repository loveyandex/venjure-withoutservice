package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionAssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionAsset.class);
        CollectionAsset collectionAsset1 = new CollectionAsset();
        collectionAsset1.setId(1L);
        CollectionAsset collectionAsset2 = new CollectionAsset();
        collectionAsset2.setId(collectionAsset1.getId());
        assertThat(collectionAsset1).isEqualTo(collectionAsset2);
        collectionAsset2.setId(2L);
        assertThat(collectionAsset1).isNotEqualTo(collectionAsset2);
        collectionAsset1.setId(null);
        assertThat(collectionAsset1).isNotEqualTo(collectionAsset2);
    }
}
