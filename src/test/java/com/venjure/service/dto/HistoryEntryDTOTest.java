package com.venjure.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.venjure.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryEntryDTO.class);
        HistoryEntryDTO historyEntryDTO1 = new HistoryEntryDTO();
        historyEntryDTO1.setId(1L);
        HistoryEntryDTO historyEntryDTO2 = new HistoryEntryDTO();
        assertThat(historyEntryDTO1).isNotEqualTo(historyEntryDTO2);
        historyEntryDTO2.setId(historyEntryDTO1.getId());
        assertThat(historyEntryDTO1).isEqualTo(historyEntryDTO2);
        historyEntryDTO2.setId(2L);
        assertThat(historyEntryDTO1).isNotEqualTo(historyEntryDTO2);
        historyEntryDTO1.setId(null);
        assertThat(historyEntryDTO1).isNotEqualTo(historyEntryDTO2);
    }
}
