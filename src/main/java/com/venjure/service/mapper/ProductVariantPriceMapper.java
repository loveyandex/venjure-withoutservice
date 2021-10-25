package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductVariantPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariantPrice} and its DTO {@link ProductVariantPriceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class })
public interface ProductVariantPriceMapper extends EntityMapper<ProductVariantPriceDTO, ProductVariantPrice> {
    @Mapping(target = "variant", source = "variant", qualifiedByName = "id")
    ProductVariantPriceDTO toDto(ProductVariantPrice s);
}
