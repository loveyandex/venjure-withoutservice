package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductOptionGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOptionGroup} and its DTO {@link ProductOptionGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface ProductOptionGroupMapper extends EntityMapper<ProductOptionGroupDTO, ProductOptionGroup> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    ProductOptionGroupDTO toDto(ProductOptionGroup s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductOptionGroupDTO toDtoId(ProductOptionGroup productOptionGroup);
}
