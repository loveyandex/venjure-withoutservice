package com.venjure.repository;

import com.venjure.domain.Refund;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Refund entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefundRepository extends JpaRepository<Refund, Long>, JpaSpecificationExecutor<Refund> {}
