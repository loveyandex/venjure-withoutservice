package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.SurchargeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Surcharge} and its DTO {@link SurchargeDTO}.
 */
@Mapper(componentModel = "spring", uses = { JorderMapper.class, OrderModificationMapper.class })
public interface SurchargeMapper extends EntityMapper<SurchargeDTO, Surcharge> {
    @Mapping(target = "jorder", source = "jorder", qualifiedByName = "id")
    @Mapping(target = "ordermodification", source = "ordermodification", qualifiedByName = "id")
    SurchargeDTO toDto(Surcharge s);
}
