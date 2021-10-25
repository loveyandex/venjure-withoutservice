package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductVariantAsset;
import com.venjure.repository.ProductVariantAssetRepository;
import com.venjure.service.criteria.ProductVariantAssetCriteria;
import com.venjure.service.dto.ProductVariantAssetDTO;
import com.venjure.service.mapper.ProductVariantAssetMapper;
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
 * Service for executing complex queries for {@link ProductVariantAsset} entities in the database.
 * The main input is a {@link ProductVariantAssetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantAssetDTO} or a {@link Page} of {@link ProductVariantAssetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantAssetQueryService extends QueryService<ProductVariantAsset> {

    private final Logger log = LoggerFactory.getLogger(ProductVariantAssetQueryService.class);

    private final ProductVariantAssetRepository productVariantAssetRepository;

    private final ProductVariantAssetMapper productVariantAssetMapper;

    public ProductVariantAssetQueryService(
        ProductVariantAssetRepository productVariantAssetRepository,
        ProductVariantAssetMapper productVariantAssetMapper
    ) {
        this.productVariantAssetRepository = productVariantAssetRepository;
        this.productVariantAssetMapper = productVariantAssetMapper;
    }

    /**
     * Return a {@link List} of {@link ProductVariantAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantAssetDTO> findByCriteria(ProductVariantAssetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductVariantAsset> specification = createSpecification(criteria);
        return productVariantAssetMapper.toDto(productVariantAssetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductVariantAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantAssetDTO> findByCriteria(ProductVariantAssetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariantAsset> specification = createSpecification(criteria);
        return productVariantAssetRepository.findAll(specification, page).map(productVariantAssetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantAssetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductVariantAsset> specification = createSpecification(criteria);
        return productVariantAssetRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantAssetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariantAsset> createSpecification(ProductVariantAssetCriteria criteria) {
        Specification<ProductVariantAsset> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVariantAsset_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductVariantAsset_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductVariantAsset_.updatedat));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), ProductVariantAsset_.position));
            }
            if (criteria.getAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetId(),
                            root -> root.join(ProductVariantAsset_.asset, JoinType.LEFT).get(Asset_.id)
                        )
                    );
            }
            if (criteria.getProductvariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductvariantId(),
                            root -> root.join(ProductVariantAsset_.productvariant, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
