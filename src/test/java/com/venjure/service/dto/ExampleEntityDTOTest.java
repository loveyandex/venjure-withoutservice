package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExampleEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExampleEntityDTO.class);
        ExampleEntityDTO exampleEntityDTO1 = new ExampleEntityDTO();
        exampleEntityDTO1.setId(1L);
        ExampleEntityDTO exampleEntityDTO2 = new ExampleEntityDTO();
        assertThat(exampleEntityDTO1).isNotEqualTo(exampleEntityDTO2);
        exampleEntityDTO2.setId(exampleEntityDTO1.getId());
        assertThat(exampleEntityDTO1).isEqualTo(exampleEntityDTO2);
        exampleEntityDTO2.setId(2L);
        assertThat(exampleEntityDTO1).isNotEqualTo(exampleEntityDTO2);
        exampleEntityDTO1.setId(null);
        assertThat(exampleEntityDTO1).isNotEqualTo(exampleEntityDTO2);
    }
}
