package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssetDTO toDtoId(Asset asset);
}
