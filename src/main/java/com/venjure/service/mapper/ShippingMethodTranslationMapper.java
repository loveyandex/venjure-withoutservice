package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ShippingMethodTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShippingMethodTranslation} and its DTO {@link ShippingMethodTranslationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ShippingMethodMapper.class })
public interface ShippingMethodTranslationMapper extends EntityMapper<ShippingMethodTranslationDTO, ShippingMethodTranslation> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    ShippingMethodTranslationDTO toDto(ShippingMethodTranslation s);
}
