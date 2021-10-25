package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderModificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderModificationDTO.class);
        OrderModificationDTO orderModificationDTO1 = new OrderModificationDTO();
        orderModificationDTO1.setId(1L);
        OrderModificationDTO orderModificationDTO2 = new OrderModificationDTO();
        assertThat(orderModificationDTO1).isNotEqualTo(orderModificationDTO2);
        orderModificationDTO2.setId(orderModificationDTO1.getId());
        assertThat(orderModificationDTO1).isEqualTo(orderModificationDTO2);
        orderModificationDTO2.setId(2L);
        assertThat(orderModificationDTO1).isNotEqualTo(orderModificationDTO2);
        orderModificationDTO1.setId(null);
        assertThat(orderModificationDTO1).isNotEqualTo(orderModificationDTO2);
    }
}
