package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.OrderItemDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { OrderLineMapper.class, RefundMapper.class, FulfillmentMapper.class, OrderModificationMapper.class }
)
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "line", source = "line", qualifiedByName = "id")
    @Mapping(target = "refund", source = "refund", qualifiedByName = "id")
    @Mapping(target = "fulfillments", source = "fulfillments", qualifiedByName = "idSet")
    @Mapping(target = "orderModifications", source = "orderModifications", qualifiedByName = "idSet")
    OrderItemDTO toDto(OrderItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoId(OrderItem orderItem);

    @Mapping(target = "removeFulfillment", ignore = true)
    @Mapping(target = "removeOrderModification", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
