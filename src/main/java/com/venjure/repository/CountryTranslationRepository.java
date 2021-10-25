package com.venjure.repository;

import com.venjure.domain.CountryTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountryTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryTranslationRepository
    extends JpaRepository<CountryTranslation, Long>, JpaSpecificationExecutor<CountryTranslation> {}
