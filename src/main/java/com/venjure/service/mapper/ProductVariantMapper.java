package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ProductVariantDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariant} and its DTO {@link ProductVariantDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ProductMapper.class,
        AssetMapper.class,
        TaxCategoryMapper.class,
        ChannelMapper.class,
        CollectionMapper.class,
        FacetValueMapper.class,
        ProductOptionMapper.class,
    }
)
public interface ProductVariantMapper extends EntityMapper<ProductVariantDTO, ProductVariant> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "featuredasset", source = "featuredasset", qualifiedByName = "id")
    @Mapping(target = "taxcategory", source = "taxcategory", qualifiedByName = "id")
    @Mapping(target = "channels", source = "channels", qualifiedByName = "idSet")
    @Mapping(target = "productVariants", source = "productVariants", qualifiedByName = "idSet")
    @Mapping(target = "facetValues", source = "facetValues", qualifiedByName = "idSet")
    @Mapping(target = "productOptions", source = "productOptions", qualifiedByName = "idSet")
    ProductVariantDTO toDto(ProductVariant s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductVariantDTO toDtoId(ProductVariant productVariant);

    @Mapping(target = "removeChannel", ignore = true)
    @Mapping(target = "removeProductVariants", ignore = true)
    @Mapping(target = "removeFacetValue", ignore = true)
    @Mapping(target = "removeProductOption", ignore = true)
    ProductVariant toEntity(ProductVariantDTO productVariantDTO);
}
