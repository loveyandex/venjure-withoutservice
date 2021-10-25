package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductOptionTranslation;
import com.venjure.repository.ProductOptionTranslationRepository;
import com.venjure.service.criteria.ProductOptionTranslationCriteria;
import com.venjure.service.dto.ProductOptionTranslationDTO;
import com.venjure.service.mapper.ProductOptionTranslationMapper;
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
 * Service for executing complex queries for {@link ProductOptionTranslation} entities in the database.
 * The main input is a {@link ProductOptionTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductOptionTranslationDTO} or a {@link Page} of {@link ProductOptionTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductOptionTranslationQueryService extends QueryService<ProductOptionTranslation> {

    private final Logger log = LoggerFactory.getLogger(ProductOptionTranslationQueryService.class);

    private final ProductOptionTranslationRepository productOptionTranslationRepository;

    private final ProductOptionTranslationMapper productOptionTranslationMapper;

    public ProductOptionTranslationQueryService(
        ProductOptionTranslationRepository productOptionTranslationRepository,
        ProductOptionTranslationMapper productOptionTranslationMapper
    ) {
        this.productOptionTranslationRepository = productOptionTranslationRepository;
        this.productOptionTranslationMapper = productOptionTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link ProductOptionTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOptionTranslationDTO> findByCriteria(ProductOptionTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductOptionTranslation> specification = createSpecification(criteria);
        return productOptionTranslationMapper.toDto(productOptionTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductOptionTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOptionTranslationDTO> findByCriteria(ProductOptionTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductOptionTranslation> specification = createSpecification(criteria);
        return productOptionTranslationRepository.findAll(specification, page).map(productOptionTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductOptionTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductOptionTranslation> specification = createSpecification(criteria);
        return productOptionTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductOptionTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductOptionTranslation> createSpecification(ProductOptionTranslationCriteria criteria) {
        Specification<ProductOptionTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductOptionTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductOptionTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductOptionTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLanguagecode(), ProductOptionTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductOptionTranslation_.name));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(ProductOptionTranslation_.base, JoinType.LEFT).get(ProductOption_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
