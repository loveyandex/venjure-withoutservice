package com.venjure.repository;

import com.venjure.domain.TaxRate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaxRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long>, JpaSpecificationExecutor<TaxRate> {}
