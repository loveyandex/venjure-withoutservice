package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductOptionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOption} and its DTO {@link ProductOptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductOptionGroupMapper.class })
public interface ProductOptionMapper extends EntityMapper<ProductOptionDTO, ProductOption> {
    @Mapping(target = "group", source = "group", qualifiedByName = "id")
    ProductOptionDTO toDto(ProductOption s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductOptionDTO toDtoId(ProductOption productOption);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ProductOptionDTO> toDtoIdSet(Set<ProductOption> productOption);
}
