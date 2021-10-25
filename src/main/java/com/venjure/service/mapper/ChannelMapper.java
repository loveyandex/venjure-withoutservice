package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ChannelDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Channel} and its DTO {@link ChannelDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ZoneMapper.class, PaymentMethodMapper.class, ProductMapper.class, PromotionMapper.class, ShippingMethodMapper.class }
)
public interface ChannelMapper extends EntityMapper<ChannelDTO, Channel> {
    @Mapping(target = "defaulttaxzone", source = "defaulttaxzone", qualifiedByName = "id")
    @Mapping(target = "defaultshippingzone", source = "defaultshippingzone", qualifiedByName = "id")
    @Mapping(target = "paymentMethods", source = "paymentMethods", qualifiedByName = "idSet")
    @Mapping(target = "products", source = "products", qualifiedByName = "idSet")
    @Mapping(target = "promotions", source = "promotions", qualifiedByName = "idSet")
    @Mapping(target = "shippingMethods", source = "shippingMethods", qualifiedByName = "idSet")
    ChannelDTO toDto(Channel s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ChannelDTO> toDtoIdSet(Set<Channel> channel);

    @Mapping(target = "removePaymentMethod", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    @Mapping(target = "removePromotion", ignore = true)
    @Mapping(target = "removeShippingMethod", ignore = true)
    Channel toEntity(ChannelDTO channelDTO);
}
