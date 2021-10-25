package com.venjure.repository;

import com.venjure.domain.ProductOptionTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductOptionTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOptionTranslationRepository
    extends JpaRepository<ProductOptionTranslation, Long>, JpaSpecificationExecutor<ProductOptionTranslation> {}
