package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.OrderItem;
import com.venjure.repository.OrderItemRepository;
import com.venjure.service.criteria.OrderItemCriteria;
import com.venjure.service.dto.OrderItemDTO;
import com.venjure.service.mapper.OrderItemMapper;
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
 * Service for executing complex queries for {@link OrderItem} entities in the database.
 * The main input is a {@link OrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderItemDTO} or a {@link Page} of {@link OrderItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemQueryService extends QueryService<OrderItem> {

    private final Logger log = LoggerFactory.getLogger(OrderItemQueryService.class);

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    public OrderItemQueryService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    /**
     * Return a {@link List} of {@link OrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItemDTO> findByCriteria(OrderItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemMapper.toDto(orderItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> findByCriteria(OrderItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.findAll(specification, page).map(orderItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItem> createSpecification(OrderItemCriteria criteria) {
        Specification<OrderItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderItem_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), OrderItem_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), OrderItem_.updatedat));
            }
            if (criteria.getInitiallistprice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInitiallistprice(), OrderItem_.initiallistprice));
            }
            if (criteria.getListprice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getListprice(), OrderItem_.listprice));
            }
            if (criteria.getListpriceincludestax() != null) {
                specification = specification.and(buildSpecification(criteria.getListpriceincludestax(), OrderItem_.listpriceincludestax));
            }
            if (criteria.getAdjustments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdjustments(), OrderItem_.adjustments));
            }
            if (criteria.getTaxlines() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxlines(), OrderItem_.taxlines));
            }
            if (criteria.getCancelled() != null) {
                specification = specification.and(buildSpecification(criteria.getCancelled(), OrderItem_.cancelled));
            }
            if (criteria.getLineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLineId(), root -> root.join(OrderItem_.line, JoinType.LEFT).get(OrderLine_.id))
                    );
            }
            if (criteria.getRefundId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRefundId(), root -> root.join(OrderItem_.refund, JoinType.LEFT).get(Refund_.id))
                    );
            }
            if (criteria.getFulfillmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFulfillmentId(),
                            root -> root.join(OrderItem_.fulfillments, JoinType.LEFT).get(Fulfillment_.id)
                        )
                    );
            }
            if (criteria.getOrderModificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderModificationId(),
                            root -> root.join(OrderItem_.orderModifications, JoinType.LEFT).get(OrderModification_.id)
                        )
                    );
            }
            if (criteria.getStockMovementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockMovementId(),
                            root -> root.join(OrderItem_.stockMovements, JoinType.LEFT).get(StockMovement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
