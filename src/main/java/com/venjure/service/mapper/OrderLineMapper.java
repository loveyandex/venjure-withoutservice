package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.OrderLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderLine} and its DTO {@link OrderLineDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class, TaxCategoryMapper.class, AssetMapper.class, JorderMapper.class })
public interface OrderLineMapper extends EntityMapper<OrderLineDTO, OrderLine> {
    @Mapping(target = "productvariant", source = "productvariant", qualifiedByName = "id")
    @Mapping(target = "taxcategory", source = "taxcategory", qualifiedByName = "id")
    @Mapping(target = "featuredAsset", source = "featuredAsset", qualifiedByName = "id")
    @Mapping(target = "jorder", source = "jorder", qualifiedByName = "id")
    OrderLineDTO toDto(OrderLine s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderLineDTO toDtoId(OrderLine orderLine);
}
