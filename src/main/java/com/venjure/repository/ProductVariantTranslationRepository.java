package com.venjure.repository;

import com.venjure.domain.ProductVariantTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductVariantTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductVariantTranslationRepository
    extends JpaRepository<ProductVariantTranslation, Long>, JpaSpecificationExecutor<ProductVariantTranslation> {}
