package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductAsset;
import com.venjure.repository.ProductAssetRepository;
import com.venjure.service.criteria.ProductAssetCriteria;
import com.venjure.service.dto.ProductAssetDTO;
import com.venjure.service.mapper.ProductAssetMapper;
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
 * Service for executing complex queries for {@link ProductAsset} entities in the database.
 * The main input is a {@link ProductAssetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductAssetDTO} or a {@link Page} of {@link ProductAssetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductAssetQueryService extends QueryService<ProductAsset> {

    private final Logger log = LoggerFactory.getLogger(ProductAssetQueryService.class);

    private final ProductAssetRepository productAssetRepository;

    private final ProductAssetMapper productAssetMapper;

    public ProductAssetQueryService(ProductAssetRepository productAssetRepository, ProductAssetMapper productAssetMapper) {
        this.productAssetRepository = productAssetRepository;
        this.productAssetMapper = productAssetMapper;
    }

    /**
     * Return a {@link List} of {@link ProductAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductAssetDTO> findByCriteria(ProductAssetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductAsset> specification = createSpecification(criteria);
        return productAssetMapper.toDto(productAssetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductAssetDTO> findByCriteria(ProductAssetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductAsset> specification = createSpecification(criteria);
        return productAssetRepository.findAll(specification, page).map(productAssetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductAssetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductAsset> specification = createSpecification(criteria);
        return productAssetRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductAssetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductAsset> createSpecification(ProductAssetCriteria criteria) {
        Specification<ProductAsset> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductAsset_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductAsset_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductAsset_.updatedat));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), ProductAsset_.position));
            }
            if (criteria.getAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAssetId(), root -> root.join(ProductAsset_.asset, JoinType.LEFT).get(Asset_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(ProductAsset_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
