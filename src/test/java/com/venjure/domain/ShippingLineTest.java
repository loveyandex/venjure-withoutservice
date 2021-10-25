package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingLine.class);
        ShippingLine shippingLine1 = new ShippingLine();
        shippingLine1.setId(1L);
        ShippingLine shippingLine2 = new ShippingLine();
        shippingLine2.setId(shippingLine1.getId());
        assertThat(shippingLine1).isEqualTo(shippingLine2);
        shippingLine2.setId(2L);
        assertThat(shippingLine1).isNotEqualTo(shippingLine2);
        shippingLine1.setId(null);
        assertThat(shippingLine1).isNotEqualTo(shippingLine2);
    }
}
