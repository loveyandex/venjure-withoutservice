package com.venjure.repository;

import com.venjure.domain.PaymentMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {}
