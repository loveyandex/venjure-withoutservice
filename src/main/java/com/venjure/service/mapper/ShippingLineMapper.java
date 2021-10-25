package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ShippingLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShippingLine} and its DTO {@link ShippingLineDTO}.
 */
@Mapper(componentModel = "spring", uses = { ShippingMethodMapper.class, JorderMapper.class })
public interface ShippingLineMapper extends EntityMapper<ShippingLineDTO, ShippingLine> {
    @Mapping(target = "shippingmethod", source = "shippingmethod", qualifiedByName = "id")
    @Mapping(target = "jorder", source = "jorder", qualifiedByName = "id")
    ShippingLineDTO toDto(ShippingLine s);
}
