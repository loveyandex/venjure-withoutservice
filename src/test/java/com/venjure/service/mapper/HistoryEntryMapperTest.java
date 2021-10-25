package com.venjure.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryEntryMapperTest {

    private HistoryEntryMapper historyEntryMapper;

    @BeforeEach
    public void setUp() {
        historyEntryMapper = new HistoryEntryMapperImpl();
    }
}
