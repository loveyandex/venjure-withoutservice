package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.TaxRate;
import com.venjure.repository.TaxRateRepository;
import com.venjure.service.criteria.TaxRateCriteria;
import com.venjure.service.dto.TaxRateDTO;
import com.venjure.service.mapper.TaxRateMapper;
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
 * Service for executing complex queries for {@link TaxRate} entities in the database.
 * The main input is a {@link TaxRateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxRateDTO} or a {@link Page} of {@link TaxRateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxRateQueryService extends QueryService<TaxRate> {

    private final Logger log = LoggerFactory.getLogger(TaxRateQueryService.class);

    private final TaxRateRepository taxRateRepository;

    private final TaxRateMapper taxRateMapper;

    public TaxRateQueryService(TaxRateRepository taxRateRepository, TaxRateMapper taxRateMapper) {
        this.taxRateRepository = taxRateRepository;
        this.taxRateMapper = taxRateMapper;
    }

    /**
     * Return a {@link List} of {@link TaxRateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxRateDTO> findByCriteria(TaxRateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaxRate> specification = createSpecification(criteria);
        return taxRateMapper.toDto(taxRateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaxRateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxRateDTO> findByCriteria(TaxRateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaxRate> specification = createSpecification(criteria);
        return taxRateRepository.findAll(specification, page).map(taxRateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxRateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaxRate> specification = createSpecification(criteria);
        return taxRateRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxRateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxRate> createSpecification(TaxRateCriteria criteria) {
        Specification<TaxRate> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaxRate_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), TaxRate_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), TaxRate_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TaxRate_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), TaxRate_.enabled));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), TaxRate_.value));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(TaxRate_.category, JoinType.LEFT).get(TaxCategory_.id)
                        )
                    );
            }
            if (criteria.getZoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getZoneId(), root -> root.join(TaxRate_.zone, JoinType.LEFT).get(Zone_.id))
                    );
            }
            if (criteria.getCustomergroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomergroupId(),
                            root -> root.join(TaxRate_.customergroup, JoinType.LEFT).get(CustomerGroup_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
