package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingMethodTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethodTranslation.class);
        ShippingMethodTranslation shippingMethodTranslation1 = new ShippingMethodTranslation();
        shippingMethodTranslation1.setId(1L);
        ShippingMethodTranslation shippingMethodTranslation2 = new ShippingMethodTranslation();
        shippingMethodTranslation2.setId(shippingMethodTranslation1.getId());
        assertThat(shippingMethodTranslation1).isEqualTo(shippingMethodTranslation2);
        shippingMethodTranslation2.setId(2L);
        assertThat(shippingMethodTranslation1).isNotEqualTo(shippingMethodTranslation2);
        shippingMethodTranslation1.setId(null);
        assertThat(shippingMethodTranslation1).isNotEqualTo(shippingMethodTranslation2);
    }
}
