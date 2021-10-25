package com.venjure.repository;

import com.venjure.domain.ProductTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTranslationRepository
    extends JpaRepository<ProductTranslation, Long>, JpaSpecificationExecutor<ProductTranslation> {}
