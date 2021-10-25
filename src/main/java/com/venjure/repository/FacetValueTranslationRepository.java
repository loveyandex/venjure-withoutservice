package com.venjure.repository;

import com.venjure.domain.FacetValueTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacetValueTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacetValueTranslationRepository
    extends JpaRepository<FacetValueTranslation, Long>, JpaSpecificationExecutor<FacetValueTranslation> {}
