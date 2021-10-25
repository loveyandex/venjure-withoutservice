package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.RefundDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Refund} and its DTO {@link RefundDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentMapper.class })
public interface RefundMapper extends EntityMapper<RefundDTO, Refund> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    RefundDTO toDto(Refund s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RefundDTO toDtoId(Refund refund);
}
