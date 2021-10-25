package com.venjure.repository;

import com.venjure.domain.ProductVariantAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductVariantAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductVariantAssetRepository
    extends JpaRepository<ProductVariantAsset, Long>, JpaSpecificationExecutor<ProductVariantAsset> {}
