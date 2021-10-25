package com.venjure.repository;

import com.venjure.domain.Pogt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pogt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PogtRepository extends JpaRepository<Pogt, Long>, JpaSpecificationExecutor<Pogt> {}
