package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ShippingLine;
import com.venjure.repository.ShippingLineRepository;
import com.venjure.service.criteria.ShippingLineCriteria;
import com.venjure.service.dto.ShippingLineDTO;
import com.venjure.service.mapper.ShippingLineMapper;
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
 * Service for executing complex queries for {@link ShippingLine} entities in the database.
 * The main input is a {@link ShippingLineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShippingLineDTO} or a {@link Page} of {@link ShippingLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShippingLineQueryService extends QueryService<ShippingLine> {

    private final Logger log = LoggerFactory.getLogger(ShippingLineQueryService.class);

    private final ShippingLineRepository shippingLineRepository;

    private final ShippingLineMapper shippingLineMapper;

    public ShippingLineQueryService(ShippingLineRepository shippingLineRepository, ShippingLineMapper shippingLineMapper) {
        this.shippingLineRepository = shippingLineRepository;
        this.shippingLineMapper = shippingLineMapper;
    }

    /**
     * Return a {@link List} of {@link ShippingLineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingLineDTO> findByCriteria(ShippingLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShippingLine> specification = createSpecification(criteria);
        return shippingLineMapper.toDto(shippingLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShippingLineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingLineDTO> findByCriteria(ShippingLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShippingLine> specification = createSpecification(criteria);
        return shippingLineRepository.findAll(specification, page).map(shippingLineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShippingLineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShippingLine> specification = createSpecification(criteria);
        return shippingLineRepository.count(specification);
    }

    /**
     * Function to convert {@link ShippingLineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShippingLine> createSpecification(ShippingLineCriteria criteria) {
        Specification<ShippingLine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShippingLine_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ShippingLine_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ShippingLine_.updatedat));
            }
            if (criteria.getListprice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getListprice(), ShippingLine_.listprice));
            }
            if (criteria.getListpriceincludestax() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getListpriceincludestax(), ShippingLine_.listpriceincludestax));
            }
            if (criteria.getAdjustments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdjustments(), ShippingLine_.adjustments));
            }
            if (criteria.getTaxlines() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxlines(), ShippingLine_.taxlines));
            }
            if (criteria.getShippingmethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShippingmethodId(),
                            root -> root.join(ShippingLine_.shippingmethod, JoinType.LEFT).get(ShippingMethod_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(ShippingLine_.jorder, JoinType.LEFT).get(Jorder_.id))
                    );
            }
        }
        return specification;
    }
}
