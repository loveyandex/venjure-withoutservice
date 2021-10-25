package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.OrderModificationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderModification} and its DTO {@link OrderModificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentMapper.class, RefundMapper.class, JorderMapper.class })
public interface OrderModificationMapper extends EntityMapper<OrderModificationDTO, OrderModification> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    @Mapping(target = "refund", source = "refund", qualifiedByName = "id")
    @Mapping(target = "jorder", source = "jorder", qualifiedByName = "id")
    OrderModificationDTO toDto(OrderModification s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderModificationDTO toDtoId(OrderModification orderModification);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<OrderModificationDTO> toDtoIdSet(Set<OrderModification> orderModification);
}
