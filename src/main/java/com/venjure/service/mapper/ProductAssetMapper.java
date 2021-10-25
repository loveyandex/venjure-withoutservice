package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductAssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductAsset} and its DTO {@link ProductAssetDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssetMapper.class, ProductMapper.class })
public interface ProductAssetMapper extends EntityMapper<ProductAssetDTO, ProductAsset> {
    @Mapping(target = "asset", source = "asset", qualifiedByName = "id")
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    ProductAssetDTO toDto(ProductAsset s);
}
