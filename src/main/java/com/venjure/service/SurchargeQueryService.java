package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Surcharge;
import com.venjure.repository.SurchargeRepository;
import com.venjure.service.criteria.SurchargeCriteria;
import com.venjure.service.dto.SurchargeDTO;
import com.venjure.service.mapper.SurchargeMapper;
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
 * Service for executing complex queries for {@link Surcharge} entities in the database.
 * The main input is a {@link SurchargeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SurchargeDTO} or a {@link Page} of {@link SurchargeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SurchargeQueryService extends QueryService<Surcharge> {

    private final Logger log = LoggerFactory.getLogger(SurchargeQueryService.class);

    private final SurchargeRepository surchargeRepository;

    private final SurchargeMapper surchargeMapper;

    public SurchargeQueryService(SurchargeRepository surchargeRepository, SurchargeMapper surchargeMapper) {
        this.surchargeRepository = surchargeRepository;
        this.surchargeMapper = surchargeMapper;
    }

    /**
     * Return a {@link List} of {@link SurchargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SurchargeDTO> findByCriteria(SurchargeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Surcharge> specification = createSpecification(criteria);
        return surchargeMapper.toDto(surchargeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SurchargeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SurchargeDTO> findByCriteria(SurchargeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Surcharge> specification = createSpecification(criteria);
        return surchargeRepository.findAll(specification, page).map(surchargeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SurchargeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Surcharge> specification = createSpecification(criteria);
        return surchargeRepository.count(specification);
    }

    /**
     * Function to convert {@link SurchargeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Surcharge> createSpecification(SurchargeCriteria criteria) {
        Specification<Surcharge> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Surcharge_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Surcharge_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Surcharge_.updatedat));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Surcharge_.description));
            }
            if (criteria.getListprice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getListprice(), Surcharge_.listprice));
            }
            if (criteria.getListpriceincludestax() != null) {
                specification = specification.and(buildSpecification(criteria.getListpriceincludestax(), Surcharge_.listpriceincludestax));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), Surcharge_.sku));
            }
            if (criteria.getTaxlines() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxlines(), Surcharge_.taxlines));
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(Surcharge_.jorder, JoinType.LEFT).get(Jorder_.id))
                    );
            }
            if (criteria.getOrdermodificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrdermodificationId(),
                            root -> root.join(Surcharge_.ordermodification, JoinType.LEFT).get(OrderModification_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
