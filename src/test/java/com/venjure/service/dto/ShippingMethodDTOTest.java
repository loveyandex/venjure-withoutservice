package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingMethodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingMethodDTO.class);
        ShippingMethodDTO shippingMethodDTO1 = new ShippingMethodDTO();
        shippingMethodDTO1.setId(1L);
        ShippingMethodDTO shippingMethodDTO2 = new ShippingMethodDTO();
        assertThat(shippingMethodDTO1).isNotEqualTo(shippingMethodDTO2);
        shippingMethodDTO2.setId(shippingMethodDTO1.getId());
        assertThat(shippingMethodDTO1).isEqualTo(shippingMethodDTO2);
        shippingMethodDTO2.setId(2L);
        assertThat(shippingMethodDTO1).isNotEqualTo(shippingMethodDTO2);
        shippingMethodDTO1.setId(null);
        assertThat(shippingMethodDTO1).isNotEqualTo(shippingMethodDTO2);
    }
}
