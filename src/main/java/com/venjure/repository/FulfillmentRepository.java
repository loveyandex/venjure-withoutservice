package com.venjure.repository;

import com.venjure.domain.Fulfillment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fulfillment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FulfillmentRepository extends JpaRepository<Fulfillment, Long>, JpaSpecificationExecutor<Fulfillment> {}
