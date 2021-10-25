package com.venjure.repository;

import com.venjure.domain.ProductAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, Long>, JpaSpecificationExecutor<ProductAsset> {}
