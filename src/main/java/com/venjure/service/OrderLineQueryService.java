package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.OrderLine;
import com.venjure.repository.OrderLineRepository;
import com.venjure.service.criteria.OrderLineCriteria;
import com.venjure.service.dto.OrderLineDTO;
import com.venjure.service.mapper.OrderLineMapper;
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
 * Service for executing complex queries for {@link OrderLine} entities in the database.
 * The main input is a {@link OrderLineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderLineDTO} or a {@link Page} of {@link OrderLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderLineQueryService extends QueryService<OrderLine> {

    private final Logger log = LoggerFactory.getLogger(OrderLineQueryService.class);

    private final OrderLineRepository orderLineRepository;

    private final OrderLineMapper orderLineMapper;

    public OrderLineQueryService(OrderLineRepository orderLineRepository, OrderLineMapper orderLineMapper) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
    }

    /**
     * Return a {@link List} of {@link OrderLineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderLineDTO> findByCriteria(OrderLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderLine> specification = createSpecification(criteria);
        return orderLineMapper.toDto(orderLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderLineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderLineDTO> findByCriteria(OrderLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderLine> specification = createSpecification(criteria);
        return orderLineRepository.findAll(specification, page).map(orderLineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderLineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderLine> specification = createSpecification(criteria);
        return orderLineRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderLineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderLine> createSpecification(OrderLineCriteria criteria) {
        Specification<OrderLine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderLine_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), OrderLine_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), OrderLine_.updatedat));
            }
            if (criteria.getProductvariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductvariantId(),
                            root -> root.join(OrderLine_.productvariant, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
            if (criteria.getTaxcategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxcategoryId(),
                            root -> root.join(OrderLine_.taxcategory, JoinType.LEFT).get(TaxCategory_.id)
                        )
                    );
            }
            if (criteria.getFeaturedAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFeaturedAssetId(),
                            root -> root.join(OrderLine_.featuredAsset, JoinType.LEFT).get(Asset_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(OrderLine_.jorder, JoinType.LEFT).get(Jorder_.id))
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(OrderLine_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getStockMovementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockMovementId(),
                            root -> root.join(OrderLine_.stockMovements, JoinType.LEFT).get(StockMovement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
