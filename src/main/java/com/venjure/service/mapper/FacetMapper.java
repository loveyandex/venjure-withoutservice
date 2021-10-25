package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.FacetDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facet} and its DTO {@link FacetDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChannelMapper.class })
public interface FacetMapper extends EntityMapper<FacetDTO, Facet> {
    @Mapping(target = "channels", source = "channels", qualifiedByName = "idSet")
    FacetDTO toDto(Facet s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacetDTO toDtoId(Facet facet);

    @Mapping(target = "removeChannel", ignore = true)
    Facet toEntity(FacetDTO facetDTO);
}
