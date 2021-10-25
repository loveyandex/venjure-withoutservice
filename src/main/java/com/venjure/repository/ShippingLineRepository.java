package com.venjure.repository;

import com.venjure.domain.ShippingLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShippingLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingLineRepository extends JpaRepository<ShippingLine, Long>, JpaSpecificationExecutor<ShippingLine> {}
