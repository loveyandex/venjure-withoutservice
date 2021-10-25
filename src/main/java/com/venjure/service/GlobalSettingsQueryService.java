package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.GlobalSettings;
import com.venjure.repository.GlobalSettingsRepository;
import com.venjure.service.criteria.GlobalSettingsCriteria;
import com.venjure.service.dto.GlobalSettingsDTO;
import com.venjure.service.mapper.GlobalSettingsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GlobalSettings} entities in the database.
 * The main input is a {@link GlobalSettingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GlobalSettingsDTO} or a {@link Page} of {@link GlobalSettingsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GlobalSettingsQueryService extends QueryService<GlobalSettings> {

    private final Logger log = LoggerFactory.getLogger(GlobalSettingsQueryService.class);

    private final GlobalSettingsRepository globalSettingsRepository;

    private final GlobalSettingsMapper globalSettingsMapper;

    public GlobalSettingsQueryService(GlobalSettingsRepository globalSettingsRepository, GlobalSettingsMapper globalSettingsMapper) {
        this.globalSettingsRepository = globalSettingsRepository;
        this.globalSettingsMapper = globalSettingsMapper;
    }

    /**
     * Return a {@link List} of {@link GlobalSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GlobalSettingsDTO> findByCriteria(GlobalSettingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GlobalSettings> specification = createSpecification(criteria);
        return globalSettingsMapper.toDto(globalSettingsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GlobalSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GlobalSettingsDTO> findByCriteria(GlobalSettingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GlobalSettings> specification = createSpecification(criteria);
        return globalSettingsRepository.findAll(specification, page).map(globalSettingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GlobalSettingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GlobalSettings> specification = createSpecification(criteria);
        return globalSettingsRepository.count(specification);
    }

    /**
     * Function to convert {@link GlobalSettingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GlobalSettings> createSpecification(GlobalSettingsCriteria criteria) {
        Specification<GlobalSettings> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GlobalSettings_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), GlobalSettings_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), GlobalSettings_.updatedat));
            }
            if (criteria.getAvailablelanguages() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAvailablelanguages(), GlobalSettings_.availablelanguages));
            }
            if (criteria.getTrackinventory() != null) {
                specification = specification.and(buildSpecification(criteria.getTrackinventory(), GlobalSettings_.trackinventory));
            }
            if (criteria.getOutofstockthreshold() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutofstockthreshold(), GlobalSettings_.outofstockthreshold));
            }
        }
        return specification;
    }
}
