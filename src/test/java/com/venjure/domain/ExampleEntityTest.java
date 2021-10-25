package com.venjure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExampleEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExampleEntity.class);
        ExampleEntity exampleEntity1 = new ExampleEntity();
        exampleEntity1.setId(1L);
        ExampleEntity exampleEntity2 = new ExampleEntity();
        exampleEntity2.setId(exampleEntity1.getId());
        assertThat(exampleEntity1).isEqualTo(exampleEntity2);
        exampleEntity2.setId(2L);
        assertThat(exampleEntity1).isNotEqualTo(exampleEntity2);
        exampleEntity1.setId(null);
        assertThat(exampleEntity1).isNotEqualTo(exampleEntity2);
    }
}
