package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionTranslation.class);
        CollectionTranslation collectionTranslation1 = new CollectionTranslation();
        collectionTranslation1.setId(1L);
        CollectionTranslation collectionTranslation2 = new CollectionTranslation();
        collectionTranslation2.setId(collectionTranslation1.getId());
        assertThat(collectionTranslation1).isEqualTo(collectionTranslation2);
        collectionTranslation2.setId(2L);
        assertThat(collectionTranslation1).isNotEqualTo(collectionTranslation2);
        collectionTranslation1.setId(null);
        assertThat(collectionTranslation1).isNotEqualTo(collectionTranslation2);
    }
}
