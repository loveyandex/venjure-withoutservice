package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductVariant;
import com.venjure.repository.ProductVariantRepository;
import com.venjure.service.criteria.ProductVariantCriteria;
import com.venjure.service.dto.ProductVariantDTO;
import com.venjure.service.mapper.ProductVariantMapper;
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
 * Service for executing complex queries for {@link ProductVariant} entities in the database.
 * The main input is a {@link ProductVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantDTO} or a {@link Page} of {@link ProductVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantQueryService extends QueryService<ProductVariant> {

    private final Logger log = LoggerFactory.getLogger(ProductVariantQueryService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantQueryService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    /**
     * Return a {@link List} of {@link ProductVariantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantMapper.toDto(productVariantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductVariantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.findAll(specification, page).map(productVariantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariant> createSpecification(ProductVariantCriteria criteria) {
        Specification<ProductVariant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVariant_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductVariant_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductVariant_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), ProductVariant_.deletedat));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), ProductVariant_.enabled));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), ProductVariant_.sku));
            }
            if (criteria.getStockonhand() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStockonhand(), ProductVariant_.stockonhand));
            }
            if (criteria.getStockallocated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStockallocated(), ProductVariant_.stockallocated));
            }
            if (criteria.getOutofstockthreshold() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutofstockthreshold(), ProductVariant_.outofstockthreshold));
            }
            if (criteria.getUseglobaloutofstockthreshold() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUseglobaloutofstockthreshold(), ProductVariant_.useglobaloutofstockthreshold)
                    );
            }
            if (criteria.getTrackinventory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackinventory(), ProductVariant_.trackinventory));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(ProductVariant_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getFeaturedassetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFeaturedassetId(),
                            root -> root.join(ProductVariant_.featuredasset, JoinType.LEFT).get(Asset_.id)
                        )
                    );
            }
            if (criteria.getTaxcategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxcategoryId(),
                            root -> root.join(ProductVariant_.taxcategory, JoinType.LEFT).get(TaxCategory_.id)
                        )
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChannelId(),
                            root -> root.join(ProductVariant_.channels, JoinType.LEFT).get(Channel_.id)
                        )
                    );
            }
            if (criteria.getProductVariantsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantsId(),
                            root -> root.join(ProductVariant_.productVariants, JoinType.LEFT).get(Collection_.id)
                        )
                    );
            }
            if (criteria.getFacetValueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacetValueId(),
                            root -> root.join(ProductVariant_.facetValues, JoinType.LEFT).get(FacetValue_.id)
                        )
                    );
            }
            if (criteria.getProductOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductOptionId(),
                            root -> root.join(ProductVariant_.productOptions, JoinType.LEFT).get(ProductOption_.id)
                        )
                    );
            }
            if (criteria.getProductVariantAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantAssetId(),
                            root -> root.join(ProductVariant_.productVariantAssets, JoinType.LEFT).get(ProductVariantAsset_.id)
                        )
                    );
            }
            if (criteria.getProductVariantPriceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantPriceId(),
                            root -> root.join(ProductVariant_.productVariantPrices, JoinType.LEFT).get(ProductVariantPrice_.id)
                        )
                    );
            }
            if (criteria.getProductVariantTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantTranslationId(),
                            root -> root.join(ProductVariant_.productVariantTranslations, JoinType.LEFT).get(ProductVariantTranslation_.id)
                        )
                    );
            }
            if (criteria.getStockMovementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockMovementId(),
                            root -> root.join(ProductVariant_.stockMovements, JoinType.LEFT).get(StockMovement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
