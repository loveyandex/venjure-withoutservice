package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.HistoryEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoryEntry} and its DTO {@link HistoryEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdministratorMapper.class, CustomerMapper.class, JorderMapper.class })
public interface HistoryEntryMapper extends EntityMapper<HistoryEntryDTO, HistoryEntry> {
    @Mapping(target = "administrator", source = "administrator", qualifiedByName = "id")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    @Mapping(target = "jorder", source = "jorder", qualifiedByName = "id")
    HistoryEntryDTO toDto(HistoryEntry s);
}
