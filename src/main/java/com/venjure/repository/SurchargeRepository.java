package com.venjure.repository;

import com.venjure.domain.Surcharge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Surcharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurchargeRepository extends JpaRepository<Surcharge, Long>, JpaSpecificationExecutor<Surcharge> {}
