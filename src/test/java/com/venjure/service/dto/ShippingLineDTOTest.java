package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingLineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingLineDTO.class);
        ShippingLineDTO shippingLineDTO1 = new ShippingLineDTO();
        shippingLineDTO1.setId(1L);
        ShippingLineDTO shippingLineDTO2 = new ShippingLineDTO();
        assertThat(shippingLineDTO1).isNotEqualTo(shippingLineDTO2);
        shippingLineDTO2.setId(shippingLineDTO1.getId());
        assertThat(shippingLineDTO1).isEqualTo(shippingLineDTO2);
        shippingLineDTO2.setId(2L);
        assertThat(shippingLineDTO1).isNotEqualTo(shippingLineDTO2);
        shippingLineDTO1.setId(null);
        assertThat(shippingLineDTO1).isNotEqualTo(shippingLineDTO2);
    }
}
