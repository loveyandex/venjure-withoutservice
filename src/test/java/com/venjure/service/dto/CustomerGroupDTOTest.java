package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerGroupDTO.class);
        CustomerGroupDTO customerGroupDTO1 = new CustomerGroupDTO();
        customerGroupDTO1.setId(1L);
        CustomerGroupDTO customerGroupDTO2 = new CustomerGroupDTO();
        assertThat(customerGroupDTO1).isNotEqualTo(customerGroupDTO2);
        customerGroupDTO2.setId(customerGroupDTO1.getId());
        assertThat(customerGroupDTO1).isEqualTo(customerGroupDTO2);
        customerGroupDTO2.setId(2L);
        assertThat(customerGroupDTO1).isNotEqualTo(customerGroupDTO2);
        customerGroupDTO1.setId(null);
        assertThat(customerGroupDTO1).isNotEqualTo(customerGroupDTO2);
    }
}
