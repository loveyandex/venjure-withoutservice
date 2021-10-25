package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CountryTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountryTranslation} and its DTO {@link CountryTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface CountryTranslationMapper extends EntityMapper<CountryTranslationDTO, CountryTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    CountryTranslationDTO toDto(CountryTranslation s);
}
