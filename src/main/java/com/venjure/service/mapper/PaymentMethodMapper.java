package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.PaymentMethodDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PaymentMethodDTO> toDtoIdSet(Set<PaymentMethod> paymentMethod);
}
