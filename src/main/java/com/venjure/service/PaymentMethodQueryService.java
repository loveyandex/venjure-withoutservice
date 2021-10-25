package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.PaymentMethod;
import com.venjure.repository.PaymentMethodRepository;
import com.venjure.service.criteria.PaymentMethodCriteria;
import com.venjure.service.dto.PaymentMethodDTO;
import com.venjure.service.mapper.PaymentMethodMapper;
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
 * Service for executing complex queries for {@link PaymentMethod} entities in the database.
 * The main input is a {@link PaymentMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethodDTO} or a {@link Page} of {@link PaymentMethodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodQueryService extends QueryService<PaymentMethod> {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodQueryService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodQueryService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    /**
     * Return a {@link List} of {@link PaymentMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findByCriteria(PaymentMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodMapper.toDto(paymentMethodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> findByCriteria(PaymentMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.findAll(specification, page).map(paymentMethodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentMethod> createSpecification(PaymentMethodCriteria criteria) {
        Specification<PaymentMethod> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentMethod_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), PaymentMethod_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), PaymentMethod_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PaymentMethod_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PaymentMethod_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PaymentMethod_.description));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), PaymentMethod_.enabled));
            }
            if (criteria.getChecker() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChecker(), PaymentMethod_.checker));
            }
            if (criteria.getHandler() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHandler(), PaymentMethod_.handler));
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChannelId(),
                            root -> root.join(PaymentMethod_.channels, JoinType.LEFT).get(Channel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
