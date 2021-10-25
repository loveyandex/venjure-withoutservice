package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.PromotionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promotion} and its DTO {@link PromotionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PromotionMapper extends EntityMapper<PromotionDTO, Promotion> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PromotionDTO> toDtoIdSet(Set<Promotion> promotion);
}
