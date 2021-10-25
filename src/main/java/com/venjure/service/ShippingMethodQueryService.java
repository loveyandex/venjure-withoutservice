package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ShippingMethod;
import com.venjure.repository.ShippingMethodRepository;
import com.venjure.service.criteria.ShippingMethodCriteria;
import com.venjure.service.dto.ShippingMethodDTO;
import com.venjure.service.mapper.ShippingMethodMapper;
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
 * Service for executing complex queries for {@link ShippingMethod} entities in the database.
 * The main input is a {@link ShippingMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShippingMethodDTO} or a {@link Page} of {@link ShippingMethodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShippingMethodQueryService extends QueryService<ShippingMethod> {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodQueryService.class);

    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMethodMapper shippingMethodMapper;

    public ShippingMethodQueryService(ShippingMethodRepository shippingMethodRepository, ShippingMethodMapper shippingMethodMapper) {
        this.shippingMethodRepository = shippingMethodRepository;
        this.shippingMethodMapper = shippingMethodMapper;
    }

    /**
     * Return a {@link List} of {@link ShippingMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingMethodDTO> findByCriteria(ShippingMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShippingMethod> specification = createSpecification(criteria);
        return shippingMethodMapper.toDto(shippingMethodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShippingMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingMethodDTO> findByCriteria(ShippingMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShippingMethod> specification = createSpecification(criteria);
        return shippingMethodRepository.findAll(specification, page).map(shippingMethodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShippingMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShippingMethod> specification = createSpecification(criteria);
        return shippingMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link ShippingMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShippingMethod> createSpecification(ShippingMethodCriteria criteria) {
        Specification<ShippingMethod> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShippingMethod_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ShippingMethod_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ShippingMethod_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), ShippingMethod_.deletedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ShippingMethod_.code));
            }
            if (criteria.getChecker() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChecker(), ShippingMethod_.checker));
            }
            if (criteria.getCalculator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCalculator(), ShippingMethod_.calculator));
            }
            if (criteria.getFulfillmenthandlercode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFulfillmenthandlercode(), ShippingMethod_.fulfillmenthandlercode)
                    );
            }
            if (criteria.getShippingMethodTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShippingMethodTranslationId(),
                            root -> root.join(ShippingMethod_.shippingMethodTranslations, JoinType.LEFT).get(ShippingMethodTranslation_.id)
                        )
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChannelId(),
                            root -> root.join(ShippingMethod_.channels, JoinType.LEFT).get(Channel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
