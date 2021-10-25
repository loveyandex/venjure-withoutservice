package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductVariantAssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariantAsset} and its DTO {@link ProductVariantAssetDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssetMapper.class, ProductVariantMapper.class })
public interface ProductVariantAssetMapper extends EntityMapper<ProductVariantAssetDTO, ProductVariantAsset> {
    @Mapping(target = "asset", source = "asset", qualifiedByName = "id")
    @Mapping(target = "productvariant", source = "productvariant", qualifiedByName = "id")
    ProductVariantAssetDTO toDto(ProductVariantAsset s);
}
