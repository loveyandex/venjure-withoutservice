package com.venjure.repository;

import com.venjure.domain.ProductOptionGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductOptionGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOptionGroupRepository
    extends JpaRepository<ProductOptionGroup, Long>, JpaSpecificationExecutor<ProductOptionGroup> {}
