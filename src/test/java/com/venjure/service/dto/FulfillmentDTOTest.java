package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FulfillmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FulfillmentDTO.class);
        FulfillmentDTO fulfillmentDTO1 = new FulfillmentDTO();
        fulfillmentDTO1.setId(1L);
        FulfillmentDTO fulfillmentDTO2 = new FulfillmentDTO();
        assertThat(fulfillmentDTO1).isNotEqualTo(fulfillmentDTO2);
        fulfillmentDTO2.setId(fulfillmentDTO1.getId());
        assertThat(fulfillmentDTO1).isEqualTo(fulfillmentDTO2);
        fulfillmentDTO2.setId(2L);
        assertThat(fulfillmentDTO1).isNotEqualTo(fulfillmentDTO2);
        fulfillmentDTO1.setId(null);
        assertThat(fulfillmentDTO1).isNotEqualTo(fulfillmentDTO2);
    }
}
