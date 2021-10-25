package com.venjure.repository;

import com.venjure.domain.FacetTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacetTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacetTranslationRepository extends JpaRepository<FacetTranslation, Long>, JpaSpecificationExecutor<FacetTranslation> {}
