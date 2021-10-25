package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.StockMovementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockMovement} and its DTO {@link StockMovementDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class, OrderItemMapper.class, OrderLineMapper.class })
public interface StockMovementMapper extends EntityMapper<StockMovementDTO, StockMovement> {
    @Mapping(target = "productvariant", source = "productvariant", qualifiedByName = "id")
    @Mapping(target = "orderitem", source = "orderitem", qualifiedByName = "id")
    @Mapping(target = "orderline", source = "orderline", qualifiedByName = "id")
    StockMovementDTO toDto(StockMovement s);
}
