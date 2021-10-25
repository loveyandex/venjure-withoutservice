package com.venjure.repository;

import com.venjure.domain.ShippingMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShippingMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long>, JpaSpecificationExecutor<ShippingMethod> {}
