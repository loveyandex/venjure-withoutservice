package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductOptionTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOptionTranslation} and its DTO {@link ProductOptionTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductOptionMapper.class })
public interface ProductOptionTranslationMapper extends EntityMapper<ProductOptionTranslationDTO, ProductOptionTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    ProductOptionTranslationDTO toDto(ProductOptionTranslation s);
}
