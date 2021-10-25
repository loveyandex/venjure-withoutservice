package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.PogtDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pogt} and its DTO {@link PogtDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductOptionGroupMapper.class })
public interface PogtMapper extends EntityMapper<PogtDTO, Pogt> {
    @Mapping(target = "base", source = "base", qualifiedByName = "id")
    PogtDTO toDto(Pogt s);
}
