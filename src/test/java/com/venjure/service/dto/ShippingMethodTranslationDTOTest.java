package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingMethodTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethodTranslationDTO.class);
        ShippingMethodTranslationDTO shippingMethodTranslationDTO1 = new ShippingMethodTranslationDTO();
        shippingMethodTranslationDTO1.setId(1L);
        ShippingMethodTranslationDTO shippingMethodTranslationDTO2 = new ShippingMethodTranslationDTO();
        assertThat(shippingMethodTranslationDTO1).isNotEqualTo(shippingMethodTranslationDTO2);
        shippingMethodTranslationDTO2.setId(shippingMethodTranslationDTO1.getId());
        assertThat(shippingMethodTranslationDTO1).isEqualTo(shippingMethodTranslationDTO2);
        shippingMethodTranslationDTO2.setId(2L);
        assertThat(shippingMethodTranslationDTO1).isNotEqualTo(shippingMethodTranslationDTO2);
        shippingMethodTranslationDTO1.setId(null);
        assertThat(shippingMethodTranslationDTO1).isNotEqualTo(shippingMethodTranslationDTO2);
    }
}
