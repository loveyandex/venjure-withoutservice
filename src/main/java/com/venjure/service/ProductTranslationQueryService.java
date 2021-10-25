package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductTranslation;
import com.venjure.repository.ProductTranslationRepository;
import com.venjure.service.criteria.ProductTranslationCriteria;
import com.venjure.service.dto.ProductTranslationDTO;
import com.venjure.service.mapper.ProductTranslationMapper;
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
 * Service for executing complex queries for {@link ProductTranslation} entities in the database.
 * The main input is a {@link ProductTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductTranslationDTO} or a {@link Page} of {@link ProductTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTranslationQueryService extends QueryService<ProductTranslation> {

    private final Logger log = LoggerFactory.getLogger(ProductTranslationQueryService.class);

    private final ProductTranslationRepository productTranslationRepository;

    private final ProductTranslationMapper productTranslationMapper;

    public ProductTranslationQueryService(
        ProductTranslationRepository productTranslationRepository,
        ProductTranslationMapper productTranslationMapper
    ) {
        this.productTranslationRepository = productTranslationRepository;
        this.productTranslationMapper = productTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link ProductTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTranslationDTO> findByCriteria(ProductTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductTranslation> specification = createSpecification(criteria);
        return productTranslationMapper.toDto(productTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTranslationDTO> findByCriteria(ProductTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductTranslation> specification = createSpecification(criteria);
        return productTranslationRepository.findAll(specification, page).map(productTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductTranslation> specification = createSpecification(criteria);
        return productTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductTranslation> createSpecification(ProductTranslationCriteria criteria) {
        Specification<ProductTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguagecode(), ProductTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductTranslation_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), ProductTranslation_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductTranslation_.description));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(ProductTranslation_.base, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
