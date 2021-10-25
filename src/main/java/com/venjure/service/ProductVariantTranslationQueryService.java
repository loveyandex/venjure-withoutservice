package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductVariantTranslation;
import com.venjure.repository.ProductVariantTranslationRepository;
import com.venjure.service.criteria.ProductVariantTranslationCriteria;
import com.venjure.service.dto.ProductVariantTranslationDTO;
import com.venjure.service.mapper.ProductVariantTranslationMapper;
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
 * Service for executing complex queries for {@link ProductVariantTranslation} entities in the database.
 * The main input is a {@link ProductVariantTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantTranslationDTO} or a {@link Page} of {@link ProductVariantTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantTranslationQueryService extends QueryService<ProductVariantTranslation> {

    private final Logger log = LoggerFactory.getLogger(ProductVariantTranslationQueryService.class);

    private final ProductVariantTranslationRepository productVariantTranslationRepository;

    private final ProductVariantTranslationMapper productVariantTranslationMapper;

    public ProductVariantTranslationQueryService(
        ProductVariantTranslationRepository productVariantTranslationRepository,
        ProductVariantTranslationMapper productVariantTranslationMapper
    ) {
        this.productVariantTranslationRepository = productVariantTranslationRepository;
        this.productVariantTranslationMapper = productVariantTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link ProductVariantTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantTranslationDTO> findByCriteria(ProductVariantTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductVariantTranslation> specification = createSpecification(criteria);
        return productVariantTranslationMapper.toDto(productVariantTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductVariantTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantTranslationDTO> findByCriteria(ProductVariantTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariantTranslation> specification = createSpecification(criteria);
        return productVariantTranslationRepository.findAll(specification, page).map(productVariantTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductVariantTranslation> specification = createSpecification(criteria);
        return productVariantTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariantTranslation> createSpecification(ProductVariantTranslationCriteria criteria) {
        Specification<ProductVariantTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVariantTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductVariantTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductVariantTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLanguagecode(), ProductVariantTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductVariantTranslation_.name));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(ProductVariantTranslation_.base, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
