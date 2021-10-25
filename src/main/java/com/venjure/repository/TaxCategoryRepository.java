package com.venjure.repository;

import com.venjure.domain.TaxCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaxCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Long>, JpaSpecificationExecutor<TaxCategory> {}
