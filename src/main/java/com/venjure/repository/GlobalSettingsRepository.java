package com.venjure.repository;

import com.venjure.domain.GlobalSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GlobalSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long>, JpaSpecificationExecutor<GlobalSettings> {}
