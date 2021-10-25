package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ZoneDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Zone} and its DTO {@link ZoneDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface ZoneMapper extends EntityMapper<ZoneDTO, Zone> {
    @Mapping(target = "countries", source = "countries", qualifiedByName = "idSet")
    ZoneDTO toDto(Zone s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ZoneDTO toDtoId(Zone zone);

    @Mapping(target = "removeCountry", ignore = true)
    Zone toEntity(ZoneDTO zoneDTO);
}
