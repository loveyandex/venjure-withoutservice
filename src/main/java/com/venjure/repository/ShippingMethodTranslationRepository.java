package com.venjure.repository;

import com.venjure.domain.ShippingMethodTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShippingMethodTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShippingMethodTranslationRepository
    extends JpaRepository<ShippingMethodTranslation, Long>, JpaSpecificationExecutor<ShippingMethodTranslation> {}
