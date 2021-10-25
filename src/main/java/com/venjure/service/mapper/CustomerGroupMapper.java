package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CustomerGroupDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerGroup} and its DTO {@link CustomerGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerGroupMapper extends EntityMapper<CustomerGroupDTO, CustomerGroup> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerGroupDTO toDtoId(CustomerGroup customerGroup);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CustomerGroupDTO> toDtoIdSet(Set<CustomerGroup> customerGroup);
}
