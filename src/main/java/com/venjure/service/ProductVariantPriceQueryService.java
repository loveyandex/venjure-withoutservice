package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ProductVariantPrice;
import com.venjure.repository.ProductVariantPriceRepository;
import com.venjure.service.criteria.ProductVariantPriceCriteria;
import com.venjure.service.dto.ProductVariantPriceDTO;
import com.venjure.service.mapper.ProductVariantPriceMapper;
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
 * Service for executing complex queries for {@link ProductVariantPrice} entities in the database.
 * The main input is a {@link ProductVariantPriceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantPriceDTO} or a {@link Page} of {@link ProductVariantPriceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantPriceQueryService extends QueryService<ProductVariantPrice> {

    private final Logger log = LoggerFactory.getLogger(ProductVariantPriceQueryService.class);

    private final ProductVariantPriceRepository productVariantPriceRepository;

    private final ProductVariantPriceMapper productVariantPriceMapper;

    public ProductVariantPriceQueryService(
        ProductVariantPriceRepository productVariantPriceRepository,
        ProductVariantPriceMapper productVariantPriceMapper
    ) {
        this.productVariantPriceRepository = productVariantPriceRepository;
        this.productVariantPriceMapper = productVariantPriceMapper;
    }

    /**
     * Return a {@link List} of {@link ProductVariantPriceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantPriceDTO> findByCriteria(ProductVariantPriceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductVariantPrice> specification = createSpecification(criteria);
        return productVariantPriceMapper.toDto(productVariantPriceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductVariantPriceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantPriceDTO> findByCriteria(ProductVariantPriceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariantPrice> specification = createSpecification(criteria);
        return productVariantPriceRepository.findAll(specification, page).map(productVariantPriceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantPriceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductVariantPrice> specification = createSpecification(criteria);
        return productVariantPriceRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantPriceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariantPrice> createSpecification(ProductVariantPriceCriteria criteria) {
        Specification<ProductVariantPrice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVariantPrice_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ProductVariantPrice_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ProductVariantPrice_.updatedat));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductVariantPrice_.price));
            }
            if (criteria.getChannelid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChannelid(), ProductVariantPrice_.channelid));
            }
            if (criteria.getVariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVariantId(),
                            root -> root.join(ProductVariantPrice_.variant, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
