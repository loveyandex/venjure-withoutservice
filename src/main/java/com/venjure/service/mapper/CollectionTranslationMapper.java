package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CollectionTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectionTranslation} and its DTO {@link CollectionTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { CollectionMapper.class })
public interface CollectionTranslationMapper extends EntityMapper<CollectionTranslationDTO, CollectionTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    CollectionTranslationDTO toDto(CollectionTranslation s);
}
