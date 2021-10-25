package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.FacetTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacetTranslation} and its DTO {@link FacetTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacetMapper.class })
public interface FacetTranslationMapper extends EntityMapper<FacetTranslationDTO, FacetTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    FacetTranslationDTO toDto(FacetTranslation s);
}
