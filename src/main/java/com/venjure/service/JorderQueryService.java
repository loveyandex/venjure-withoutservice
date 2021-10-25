package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Jorder;
import com.venjure.repository.JorderRepository;
import com.venjure.service.criteria.JorderCriteria;
import com.venjure.service.dto.JorderDTO;
import com.venjure.service.mapper.JorderMapper;
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
 * Service for executing complex queries for {@link Jorder} entities in the database.
 * The main input is a {@link JorderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JorderDTO} or a {@link Page} of {@link JorderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JorderQueryService extends QueryService<Jorder> {

    private final Logger log = LoggerFactory.getLogger(JorderQueryService.class);

    private final JorderRepository jorderRepository;

    private final JorderMapper jorderMapper;

    public JorderQueryService(JorderRepository jorderRepository, JorderMapper jorderMapper) {
        this.jorderRepository = jorderRepository;
        this.jorderMapper = jorderMapper;
    }

    /**
     * Return a {@link List} of {@link JorderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JorderDTO> findByCriteria(JorderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Jorder> specification = createSpecification(criteria);
        return jorderMapper.toDto(jorderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JorderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JorderDTO> findByCriteria(JorderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Jorder> specification = createSpecification(criteria);
        return jorderRepository.findAll(specification, page).map(jorderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JorderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Jorder> specification = createSpecification(criteria);
        return jorderRepository.count(specification);
    }

    /**
     * Function to convert {@link JorderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Jorder> createSpecification(JorderCriteria criteria) {
        Specification<Jorder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Jorder_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Jorder_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Jorder_.updatedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Jorder_.code));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Jorder_.state));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Jorder_.active));
            }
            if (criteria.getOrderplacedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderplacedat(), Jorder_.orderplacedat));
            }
            if (criteria.getCouponcodes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCouponcodes(), Jorder_.couponcodes));
            }
            if (criteria.getShippingaddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShippingaddress(), Jorder_.shippingaddress));
            }
            if (criteria.getBillingaddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillingaddress(), Jorder_.billingaddress));
            }
            if (criteria.getCurrencycode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencycode(), Jorder_.currencycode));
            }
            if (criteria.getSubtotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubtotal(), Jorder_.subtotal));
            }
            if (criteria.getSubtotalwithtax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubtotalwithtax(), Jorder_.subtotalwithtax));
            }
            if (criteria.getShipping() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShipping(), Jorder_.shipping));
            }
            if (criteria.getShippingwithtax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippingwithtax(), Jorder_.shippingwithtax));
            }
            if (criteria.getTaxzoneid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxzoneid(), Jorder_.taxzoneid));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Jorder_.customer, JoinType.LEFT).get(Customer_.id))
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChannelId(), root -> root.join(Jorder_.channels, JoinType.LEFT).get(Channel_.id))
                    );
            }
            if (criteria.getPromotionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPromotionId(),
                            root -> root.join(Jorder_.promotions, JoinType.LEFT).get(Promotion_.id)
                        )
                    );
            }
            if (criteria.getHistoryEntryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoryEntryId(),
                            root -> root.join(Jorder_.historyEntries, JoinType.LEFT).get(HistoryEntry_.id)
                        )
                    );
            }
            if (criteria.getOrderLineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderLineId(),
                            root -> root.join(Jorder_.orderLines, JoinType.LEFT).get(OrderLine_.id)
                        )
                    );
            }
            if (criteria.getOrderModificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderModificationId(),
                            root -> root.join(Jorder_.orderModifications, JoinType.LEFT).get(OrderModification_.id)
                        )
                    );
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPaymentId(), root -> root.join(Jorder_.payments, JoinType.LEFT).get(Payment_.id))
                    );
            }
            if (criteria.getShippingLineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShippingLineId(),
                            root -> root.join(Jorder_.shippingLines, JoinType.LEFT).get(ShippingLine_.id)
                        )
                    );
            }
            if (criteria.getSurchargeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSurchargeId(),
                            root -> root.join(Jorder_.surcharges, JoinType.LEFT).get(Surcharge_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
