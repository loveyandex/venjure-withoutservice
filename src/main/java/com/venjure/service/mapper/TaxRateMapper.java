package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.TaxRateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxRate} and its DTO {@link TaxRateDTO}.
 */
@Mapper(componentModel = "spring", uses = { TaxCategoryMapper.class, ZoneMapper.class, CustomerGroupMapper.class })
public interface TaxRateMapper extends EntityMapper<TaxRateDTO, TaxRate> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "zone", source = "zone", qualifiedByName = "id")
    @Mapping(target = "customergroup", source = "customergroup", qualifiedByName = "id")
    TaxRateDTO toDto(TaxRate s);
}
