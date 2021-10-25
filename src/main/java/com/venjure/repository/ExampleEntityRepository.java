package com.venjure.repository;

import com.venjure.domain.ExampleEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExampleEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExampleEntityRepository extends JpaRepository<ExampleEntity, Long>, JpaSpecificationExecutor<ExampleEntity> {}
