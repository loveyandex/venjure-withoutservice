package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ShippingMethodDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShippingMethod} and its DTO {@link ShippingMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShippingMethodMapper extends EntityMapper<ShippingMethodDTO, ShippingMethod> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShippingMethodDTO toDtoId(ShippingMethod shippingMethod);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ShippingMethodDTO> toDtoIdSet(Set<ShippingMethod> shippingMethod);
}
