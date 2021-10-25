package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductTranslation} and its DTO {@link ProductTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface ProductTranslationMapper extends EntityMapper<ProductTranslationDTO, ProductTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    ProductTranslationDTO toDto(ProductTranslation s);
}
