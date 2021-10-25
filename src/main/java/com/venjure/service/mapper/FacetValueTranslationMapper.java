package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.FacetValueTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacetValueTranslation} and its DTO {@link FacetValueTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacetValueMapper.class })
public interface FacetValueTranslationMapper extends EntityMapper<FacetValueTranslationDTO, FacetValueTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    FacetValueTranslationDTO toDto(FacetValueTranslation s);
}
