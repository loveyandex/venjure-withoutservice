package com.venjure.repository;

import com.venjure.domain.StockMovement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StockMovement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, JpaSpecificationExecutor<StockMovement> {}
