package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductOption;
import com.venjure.repository.ProductOptionRepository;
import com.venjure.service.criteria.ProductOptionCriteria;
import com.venjure.service.dto.ProductOptionDTO;
import com.venjure.service.mapper.ProductOptionMapper;
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
 * Service for executing complex queries for {@link ProductOption} entities in the database.
 * The main input is a {@link ProductOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductOptionDTO} or a {@link Page} of {@link ProductOptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductOptionQueryService extends QueryService<ProductOption> {

    private final Logger log = LoggerFactory.getLogger(ProductOptionQueryService.class);

    private final ProductOptionRepository productOptionRepository;

    private final ProductOptionMapper productOptionMapper;

    public ProductOptionQueryService(ProductOptionRepository productOptionRepository, ProductOptionMapper productOptionMapper) {
        this.productOptionRepository = productOptionRepository;
        this.productOptionMapper = productOptionMapper;
    }

    /**
     * Return a {@link List} of {@link ProductOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOptionDTO> findByCriteria(ProductOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductOption> specification = createSpecification(criteria);
        return productOptionMapper.toDto(productOptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOptionDTO> findByCriteria(ProductOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductOption> specification = createSpecification(criteria);
        return productOptionRepository.findAll(specification, page).map(productOptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductOption> specification = createSpecification(criteria);
        return productOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductOption> createSpecification(ProductOptionCriteria criteria) {
        Specification<ProductOption> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductOption_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductOption_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductOption_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), ProductOption_.deletedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ProductOption_.code));
            }
            if (criteria.getGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGroupId(),
                            root -> root.join(ProductOption_.group, JoinType.LEFT).get(ProductOptionGroup_.id)
                        )
                    );
            }
            if (criteria.getProductOptionTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductOptionTranslationId(),
                            root -> root.join(ProductOption_.productOptionTranslations, JoinType.LEFT).get(ProductOptionTranslation_.id)
                        )
                    );
            }
            if (criteria.getProductVariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantId(),
                            root -> root.join(ProductOption_.productVariants, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
