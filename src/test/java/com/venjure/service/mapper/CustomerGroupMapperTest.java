package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerGroupMapperTest {

    private CustomerGroupMapper customerGroupMapper;

    @BeforeEach
    public void setUp() {
        customerGroupMapper = new CustomerGroupMapperImpl();
    }
}
