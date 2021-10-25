package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.JorderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Jorder} and its DTO {@link JorderDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, ChannelMapper.class, PromotionMapper.class })
public interface JorderMapper extends EntityMapper<JorderDTO, Jorder> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    @Mapping(target = "channels", source = "channels", qualifiedByName = "idSet")
    @Mapping(target = "promotions", source = "promotions", qualifiedByName = "idSet")
    JorderDTO toDto(Jorder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JorderDTO toDtoId(Jorder jorder);

    @Mapping(target = "removeChannel", ignore = true)
    @Mapping(target = "removePromotion", ignore = true)
    Jorder toEntity(JorderDTO jorderDTO);
}
