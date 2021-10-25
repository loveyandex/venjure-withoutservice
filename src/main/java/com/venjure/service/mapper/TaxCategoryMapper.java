package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.TaxCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxCategory} and its DTO {@link TaxCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxCategoryMapper extends EntityMapper<TaxCategoryDTO, TaxCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaxCategoryDTO toDtoId(TaxCategory taxCategory);
}
