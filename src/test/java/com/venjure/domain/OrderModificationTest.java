package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderModificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderModification.class);
        OrderModification orderModification1 = new OrderModification();
        orderModification1.setId(1L);
        OrderModification orderModification2 = new OrderModification();
        orderModification2.setId(orderModification1.getId());
        assertThat(orderModification1).isEqualTo(orderModification2);
        orderModification2.setId(2L);
        assertThat(orderModification1).isNotEqualTo(orderModification2);
        orderModification1.setId(null);
        assertThat(orderModification1).isNotEqualTo(orderModification2);
    }
}
