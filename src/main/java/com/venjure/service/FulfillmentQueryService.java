package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Fulfillment;
import com.venjure.repository.FulfillmentRepository;
import com.venjure.service.criteria.FulfillmentCriteria;
import com.venjure.service.dto.FulfillmentDTO;
import com.venjure.service.mapper.FulfillmentMapper;
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
 * Service for executing complex queries for {@link Fulfillment} entities in the database.
 * The main input is a {@link FulfillmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FulfillmentDTO} or a {@link Page} of {@link FulfillmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FulfillmentQueryService extends QueryService<Fulfillment> {

    private final Logger log = LoggerFactory.getLogger(FulfillmentQueryService.class);

    private final FulfillmentRepository fulfillmentRepository;

    private final FulfillmentMapper fulfillmentMapper;

    public FulfillmentQueryService(FulfillmentRepository fulfillmentRepository, FulfillmentMapper fulfillmentMapper) {
        this.fulfillmentRepository = fulfillmentRepository;
        this.fulfillmentMapper = fulfillmentMapper;
    }

    /**
     * Return a {@link List} of {@link FulfillmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FulfillmentDTO> findByCriteria(FulfillmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fulfillment> specification = createSpecification(criteria);
        return fulfillmentMapper.toDto(fulfillmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FulfillmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FulfillmentDTO> findByCriteria(FulfillmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fulfillment> specification = createSpecification(criteria);
        return fulfillmentRepository.findAll(specification, page).map(fulfillmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FulfillmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fulfillment> specification = createSpecification(criteria);
        return fulfillmentRepository.count(specification);
    }

    /**
     * Function to convert {@link FulfillmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fulfillment> createSpecification(FulfillmentCriteria criteria) {
        Specification<Fulfillment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fulfillment_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Fulfillment_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Fulfillment_.updatedat));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Fulfillment_.state));
            }
            if (criteria.getTrackingcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingcode(), Fulfillment_.trackingcode));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMethod(), Fulfillment_.method));
            }
            if (criteria.getHandlercode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHandlercode(), Fulfillment_.handlercode));
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(Fulfillment_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
