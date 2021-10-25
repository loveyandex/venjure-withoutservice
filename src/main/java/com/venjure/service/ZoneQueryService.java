package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Zone;
import com.venjure.repository.ZoneRepository;
import com.venjure.service.criteria.ZoneCriteria;
import com.venjure.service.dto.ZoneDTO;
import com.venjure.service.mapper.ZoneMapper;
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
 * Service for executing complex queries for {@link Zone} entities in the database.
 * The main input is a {@link ZoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ZoneDTO} or a {@link Page} of {@link ZoneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ZoneQueryService extends QueryService<Zone> {

    private final Logger log = LoggerFactory.getLogger(ZoneQueryService.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    public ZoneQueryService(ZoneRepository zoneRepository, ZoneMapper zoneMapper) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    /**
     * Return a {@link List} of {@link ZoneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ZoneDTO> findByCriteria(ZoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Zone> specification = createSpecification(criteria);
        return zoneMapper.toDto(zoneRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ZoneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ZoneDTO> findByCriteria(ZoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Zone> specification = createSpecification(criteria);
        return zoneRepository.findAll(specification, page).map(zoneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ZoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Zone> specification = createSpecification(criteria);
        return zoneRepository.count(specification);
    }

    /**
     * Function to convert {@link ZoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Zone> createSpecification(ZoneCriteria criteria) {
        Specification<Zone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Zone_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Zone_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Zone_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Zone_.name));
            }
            if (criteria.getCountryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountryId(), root -> root.join(Zone_.countries, JoinType.LEFT).get(Country_.id))
                    );
            }
        }
        return specification;
    }
}
