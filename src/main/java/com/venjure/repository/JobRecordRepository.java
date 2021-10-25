package com.venjure.repository;

import com.venjure.domain.JobRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRecordRepository extends JpaRepository<JobRecord, Long>, JpaSpecificationExecutor<JobRecord> {}
