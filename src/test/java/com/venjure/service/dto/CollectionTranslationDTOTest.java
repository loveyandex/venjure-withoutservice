package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionTranslationDTO.class);
        CollectionTranslationDTO collectionTranslationDTO1 = new CollectionTranslationDTO();
        collectionTranslationDTO1.setId(1L);
        CollectionTranslationDTO collectionTranslationDTO2 = new CollectionTranslationDTO();
        assertThat(collectionTranslationDTO1).isNotEqualTo(collectionTranslationDTO2);
        collectionTranslationDTO2.setId(collectionTranslationDTO1.getId());
        assertThat(collectionTranslationDTO1).isEqualTo(collectionTranslationDTO2);
        collectionTranslationDTO2.setId(2L);
        assertThat(collectionTranslationDTO1).isNotEqualTo(collectionTranslationDTO2);
        collectionTranslationDTO1.setId(null);
        assertThat(collectionTranslationDTO1).isNotEqualTo(collectionTranslationDTO2);
    }
}
