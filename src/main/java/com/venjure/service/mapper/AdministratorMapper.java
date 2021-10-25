package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.AdministratorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrator} and its DTO {@link AdministratorDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AdministratorMapper extends EntityMapper<AdministratorDTO, Administrator> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    AdministratorDTO toDto(Administrator s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdministratorDTO toDtoId(Administrator administrator);
}
