package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethod.class);
        ShippingMethod shippingMethod1 = new ShippingMethod();
        shippingMethod1.setId(1L);
        ShippingMethod shippingMethod2 = new ShippingMethod();
        shippingMethod2.setId(shippingMethod1.getId());
        assertThat(shippingMethod1).isEqualTo(shippingMethod2);
        shippingMethod2.setId(2L);
        assertThat(shippingMethod1).isNotEqualTo(shippingMethod2);
        shippingMethod1.setId(null);
        assertThat(shippingMethod1).isNotEqualTo(shippingMethod2);
    }
}
