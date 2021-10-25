package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.TaxCategory;
import com.venjure.repository.TaxCategoryRepository;
import com.venjure.service.criteria.TaxCategoryCriteria;
import com.venjure.service.dto.TaxCategoryDTO;
import com.venjure.service.mapper.TaxCategoryMapper;
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
 * Service for executing complex queries for {@link TaxCategory} entities in the database.
 * The main input is a {@link TaxCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxCategoryDTO} or a {@link Page} of {@link TaxCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxCategoryQueryService extends QueryService<TaxCategory> {

    private final Logger log = LoggerFactory.getLogger(TaxCategoryQueryService.class);

    private final TaxCategoryRepository taxCategoryRepository;

    private final TaxCategoryMapper taxCategoryMapper;

    public TaxCategoryQueryService(TaxCategoryRepository taxCategoryRepository, TaxCategoryMapper taxCategoryMapper) {
        this.taxCategoryRepository = taxCategoryRepository;
        this.taxCategoryMapper = taxCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link TaxCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxCategoryDTO> findByCriteria(TaxCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaxCategory> specification = createSpecification(criteria);
        return taxCategoryMapper.toDto(taxCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaxCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxCategoryDTO> findByCriteria(TaxCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaxCategory> specification = createSpecification(criteria);
        return taxCategoryRepository.findAll(specification, page).map(taxCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaxCategory> specification = createSpecification(criteria);
        return taxCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxCategory> createSpecification(TaxCategoryCriteria criteria) {
        Specification<TaxCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaxCategory_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), TaxCategory_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), TaxCategory_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TaxCategory_.name));
            }
            if (criteria.getIsdefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsdefault(), TaxCategory_.isdefault));
            }
            if (criteria.getTaxRateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxRateId(),
                            root -> root.join(TaxCategory_.taxRates, JoinType.LEFT).get(TaxRate_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
