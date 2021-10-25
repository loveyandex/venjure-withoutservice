package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CollectionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Collection} and its DTO {@link CollectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssetMapper.class })
public interface CollectionMapper extends EntityMapper<CollectionDTO, Collection> {
    @Mapping(target = "featuredasset", source = "featuredasset", qualifiedByName = "id")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    CollectionDTO toDto(Collection s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CollectionDTO toDtoId(Collection collection);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CollectionDTO> toDtoIdSet(Set<Collection> collection);
}
