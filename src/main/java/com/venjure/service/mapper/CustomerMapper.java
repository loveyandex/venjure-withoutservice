package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.CustomerDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, AssetMapper.class, ChannelMapper.class, CustomerGroupMapper.class })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "id")
    @Mapping(target = "channels", source = "channels", qualifiedByName = "idSet")
    @Mapping(target = "customerGroups", source = "customerGroups", qualifiedByName = "idSet")
    CustomerDTO toDto(Customer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoId(Customer customer);

    @Mapping(target = "removeChannel", ignore = true)
    @Mapping(target = "removeCustomerGroup", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);
}
