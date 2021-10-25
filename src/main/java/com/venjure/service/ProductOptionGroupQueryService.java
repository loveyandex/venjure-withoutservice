package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductOptionGroup;
import com.venjure.repository.ProductOptionGroupRepository;
import com.venjure.service.criteria.ProductOptionGroupCriteria;
import com.venjure.service.dto.ProductOptionGroupDTO;
import com.venjure.service.mapper.ProductOptionGroupMapper;
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
 * Service for executing complex queries for {@link ProductOptionGroup} entities in the database.
 * The main input is a {@link ProductOptionGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductOptionGroupDTO} or a {@link Page} of {@link ProductOptionGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductOptionGroupQueryService extends QueryService<ProductOptionGroup> {

    private final Logger log = LoggerFactory.getLogger(ProductOptionGroupQueryService.class);

    private final ProductOptionGroupRepository productOptionGroupRepository;

    private final ProductOptionGroupMapper productOptionGroupMapper;

    public ProductOptionGroupQueryService(
        ProductOptionGroupRepository productOptionGroupRepository,
        ProductOptionGroupMapper productOptionGroupMapper
    ) {
        this.productOptionGroupRepository = productOptionGroupRepository;
        this.productOptionGroupMapper = productOptionGroupMapper;
    }

    /**
     * Return a {@link List} of {@link ProductOptionGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOptionGroupDTO> findByCriteria(ProductOptionGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductOptionGroup> specification = createSpecification(criteria);
        return productOptionGroupMapper.toDto(productOptionGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductOptionGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOptionGroupDTO> findByCriteria(ProductOptionGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductOptionGroup> specification = createSpecification(criteria);
        return productOptionGroupRepository.findAll(specification, page).map(productOptionGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductOptionGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductOptionGroup> specification = createSpecification(criteria);
        return productOptionGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductOptionGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductOptionGroup> createSpecification(ProductOptionGroupCriteria criteria) {
        Specification<ProductOptionGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductOptionGroup_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductOptionGroup_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductOptionGroup_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), ProductOptionGroup_.deletedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ProductOptionGroup_.code));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(ProductOptionGroup_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getProductOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductOptionId(),
                            root -> root.join(ProductOptionGroup_.productOptions, JoinType.LEFT).get(ProductOption_.id)
                        )
                    );
            }
            if (criteria.getProductOptionGroupTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductOptionGroupTranslationId(),
                            root -> root.join(ProductOptionGroup_.productOptionGroupTranslations, JoinType.LEFT).get(Pogt_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
