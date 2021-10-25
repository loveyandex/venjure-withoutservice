package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CollectionAssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectionAsset} and its DTO {@link CollectionAssetDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssetMapper.class, CollectionMapper.class })
public interface CollectionAssetMapper extends EntityMapper<CollectionAssetDTO, CollectionAsset> {
    @Mapping(target = "asset", source = "asset", qualifiedByName = "id")
    @Mapping(target = "collection", source = "collection", qualifiedByName = "id")
    CollectionAssetDTO toDto(CollectionAsset s);
}
