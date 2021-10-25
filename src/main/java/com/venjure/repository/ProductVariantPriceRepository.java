package com.venjure.repository;

import com.venjure.domain.ProductVariantPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductVariantPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductVariantPriceRepository
    extends JpaRepository<ProductVariantPrice, Long>, JpaSpecificationExecutor<ProductVariantPrice> {}
