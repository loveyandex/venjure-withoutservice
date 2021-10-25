package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FulfillmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fulfillment.class);
        Fulfillment fulfillment1 = new Fulfillment();
        fulfillment1.setId(1L);
        Fulfillment fulfillment2 = new Fulfillment();
        fulfillment2.setId(fulfillment1.getId());
        assertThat(fulfillment1).isEqualTo(fulfillment2);
        fulfillment2.setId(2L);
        assertThat(fulfillment1).isNotEqualTo(fulfillment2);
        fulfillment1.setId(null);
        assertThat(fulfillment1).isNotEqualTo(fulfillment2);
    }
}
