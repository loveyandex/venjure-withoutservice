package com.venjure.repository;

import com.venjure.domain.CollectionTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CollectionTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionTranslationRepository
    extends JpaRepository<CollectionTranslation, Long>, JpaSpecificationExecutor<CollectionTranslation> {}
