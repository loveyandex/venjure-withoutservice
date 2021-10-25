package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Refund;
import com.venjure.repository.RefundRepository;
import com.venjure.service.criteria.RefundCriteria;
import com.venjure.service.dto.RefundDTO;
import com.venjure.service.mapper.RefundMapper;
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
 * Service for executing complex queries for {@link Refund} entities in the database.
 * The main input is a {@link RefundCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RefundDTO} or a {@link Page} of {@link RefundDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RefundQueryService extends QueryService<Refund> {

    private final Logger log = LoggerFactory.getLogger(RefundQueryService.class);

    private final RefundRepository refundRepository;

    private final RefundMapper refundMapper;

    public RefundQueryService(RefundRepository refundRepository, RefundMapper refundMapper) {
        this.refundRepository = refundRepository;
        this.refundMapper = refundMapper;
    }

    /**
     * Return a {@link List} of {@link RefundDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RefundDTO> findByCriteria(RefundCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Refund> specification = createSpecification(criteria);
        return refundMapper.toDto(refundRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RefundDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RefundDTO> findByCriteria(RefundCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Refund> specification = createSpecification(criteria);
        return refundRepository.findAll(specification, page).map(refundMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RefundCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Refund> specification = createSpecification(criteria);
        return refundRepository.count(specification);
    }

    /**
     * Function to convert {@link RefundCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Refund> createSpecification(RefundCriteria criteria) {
        Specification<Refund> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Refund_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Refund_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Refund_.updatedat));
            }
            if (criteria.getItems() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItems(), Refund_.items));
            }
            if (criteria.getShipping() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShipping(), Refund_.shipping));
            }
            if (criteria.getAdjustment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAdjustment(), Refund_.adjustment));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Refund_.total));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMethod(), Refund_.method));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), Refund_.reason));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Refund_.state));
            }
            if (criteria.getTransactionid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionid(), Refund_.transactionid));
            }
            if (criteria.getMetadata() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMetadata(), Refund_.metadata));
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPaymentId(), root -> root.join(Refund_.payment, JoinType.LEFT).get(Payment_.id))
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(Refund_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
