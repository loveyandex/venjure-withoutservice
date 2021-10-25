package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductVariantTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariantTranslation} and its DTO {@link ProductVariantTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class })
public interface ProductVariantTranslationMapper extends EntityMapper<ProductVariantTranslationDTO, ProductVariantTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    ProductVariantTranslationDTO toDto(ProductVariantTranslation s);
}
