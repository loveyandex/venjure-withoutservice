package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.FulfillmentDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fulfillment} and its DTO {@link FulfillmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FulfillmentMapper extends EntityMapper<FulfillmentDTO, Fulfillment> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<FulfillmentDTO> toDtoIdSet(Set<Fulfillment> fulfillment);
}
