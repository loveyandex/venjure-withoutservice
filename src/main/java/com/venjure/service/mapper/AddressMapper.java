package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, CountryMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    AddressDTO toDto(Address s);
}
