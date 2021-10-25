package com.venjure.repository;

import com.venjure.domain.OrderModification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderModification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderModificationRepository extends JpaRepository<OrderModification, Long>, JpaSpecificationExecutor<OrderModification> {}
