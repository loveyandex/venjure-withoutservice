package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.OrderModification;
import com.venjure.repository.OrderModificationRepository;
import com.venjure.service.criteria.OrderModificationCriteria;
import com.venjure.service.dto.OrderModificationDTO;
import com.venjure.service.mapper.OrderModificationMapper;
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
 * Service for executing complex queries for {@link OrderModification} entities in the database.
 * The main input is a {@link OrderModificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderModificationDTO} or a {@link Page} of {@link OrderModificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderModificationQueryService extends QueryService<OrderModification> {

    private final Logger log = LoggerFactory.getLogger(OrderModificationQueryService.class);

    private final OrderModificationRepository orderModificationRepository;

    private final OrderModificationMapper orderModificationMapper;

    public OrderModificationQueryService(
        OrderModificationRepository orderModificationRepository,
        OrderModificationMapper orderModificationMapper
    ) {
        this.orderModificationRepository = orderModificationRepository;
        this.orderModificationMapper = orderModificationMapper;
    }

    /**
     * Return a {@link List} of {@link OrderModificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderModificationDTO> findByCriteria(OrderModificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderModification> specification = createSpecification(criteria);
        return orderModificationMapper.toDto(orderModificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderModificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderModificationDTO> findByCriteria(OrderModificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderModification> specification = createSpecification(criteria);
        return orderModificationRepository.findAll(specification, page).map(orderModificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderModificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderModification> specification = createSpecification(criteria);
        return orderModificationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderModificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderModification> createSpecification(OrderModificationCriteria criteria) {
        Specification<OrderModification> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderModification_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), OrderModification_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), OrderModification_.updatedat));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), OrderModification_.note));
            }
            if (criteria.getPricechange() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPricechange(), OrderModification_.pricechange));
            }
            if (criteria.getShippingaddresschange() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getShippingaddresschange(), OrderModification_.shippingaddresschange)
                    );
            }
            if (criteria.getBillingaddresschange() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getBillingaddresschange(), OrderModification_.billingaddresschange)
                    );
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentId(),
                            root -> root.join(OrderModification_.payment, JoinType.LEFT).get(Payment_.id)
                        )
                    );
            }
            if (criteria.getRefundId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRefundId(),
                            root -> root.join(OrderModification_.refund, JoinType.LEFT).get(Refund_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJorderId(),
                            root -> root.join(OrderModification_.jorder, JoinType.LEFT).get(Jorder_.id)
                        )
                    );
            }
            if (criteria.getSurchargeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSurchargeId(),
                            root -> root.join(OrderModification_.surcharges, JoinType.LEFT).get(Surcharge_.id)
                        )
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(OrderModification_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
