package com.venjure.repository;

import com.venjure.domain.CollectionAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CollectionAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionAssetRepository extends JpaRepository<CollectionAsset, Long>, JpaSpecificationExecutor<CollectionAsset> {}
