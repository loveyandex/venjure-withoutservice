package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionAssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionAssetDTO.class);
        CollectionAssetDTO collectionAssetDTO1 = new CollectionAssetDTO();
        collectionAssetDTO1.setId(1L);
        CollectionAssetDTO collectionAssetDTO2 = new CollectionAssetDTO();
        assertThat(collectionAssetDTO1).isNotEqualTo(collectionAssetDTO2);
        collectionAssetDTO2.setId(collectionAssetDTO1.getId());
        assertThat(collectionAssetDTO1).isEqualTo(collectionAssetDTO2);
        collectionAssetDTO2.setId(2L);
        assertThat(collectionAssetDTO1).isNotEqualTo(collectionAssetDTO2);
        collectionAssetDTO1.setId(null);
        assertThat(collectionAssetDTO1).isNotEqualTo(collectionAssetDTO2);
    }
}
