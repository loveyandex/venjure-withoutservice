package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.FacetValueDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacetValue} and its DTO {@link FacetValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacetMapper.class, ChannelMapper.class, ProductMapper.class })
public interface FacetValueMapper extends EntityMapper<FacetValueDTO, FacetValue> {
    @Mapping(target = "facet", source = "facet", qualifiedByName = "id")
    @Mapping(target = "channels", source = "channels", qualifiedByName = "idSet")
    @Mapping(target = "products", source = "products", qualifiedByName = "idSet")
    FacetValueDTO toDto(FacetValue s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacetValueDTO toDtoId(FacetValue facetValue);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<FacetValueDTO> toDtoIdSet(Set<FacetValue> facetValue);

    @Mapping(target = "removeChannel", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    FacetValue toEntity(FacetValueDTO facetValueDTO);
}
