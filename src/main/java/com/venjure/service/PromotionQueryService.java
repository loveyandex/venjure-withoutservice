package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Promotion;
import com.venjure.repository.PromotionRepository;
import com.venjure.service.criteria.PromotionCriteria;
import com.venjure.service.dto.PromotionDTO;
import com.venjure.service.mapper.PromotionMapper;
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
 * Service for executing complex queries for {@link Promotion} entities in the database.
 * The main input is a {@link PromotionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromotionDTO} or a {@link Page} of {@link PromotionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromotionQueryService extends QueryService<Promotion> {

    private final Logger log = LoggerFactory.getLogger(PromotionQueryService.class);

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    public PromotionQueryService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    /**
     * Return a {@link List} of {@link PromotionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromotionDTO> findByCriteria(PromotionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Promotion> specification = createSpecification(criteria);
        return promotionMapper.toDto(promotionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromotionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromotionDTO> findByCriteria(PromotionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Promotion> specification = createSpecification(criteria);
        return promotionRepository.findAll(specification, page).map(promotionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromotionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Promotion> specification = createSpecification(criteria);
        return promotionRepository.count(specification);
    }

    /**
     * Function to convert {@link PromotionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Promotion> createSpecification(PromotionCriteria criteria) {
        Specification<Promotion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Promotion_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Promotion_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Promotion_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), Promotion_.deletedat));
            }
            if (criteria.getStartsat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartsat(), Promotion_.startsat));
            }
            if (criteria.getEndsat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndsat(), Promotion_.endsat));
            }
            if (criteria.getCouponcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCouponcode(), Promotion_.couponcode));
            }
            if (criteria.getPercustomerusagelimit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPercustomerusagelimit(), Promotion_.percustomerusagelimit));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Promotion_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Promotion_.enabled));
            }
            if (criteria.getConditions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConditions(), Promotion_.conditions));
            }
            if (criteria.getActions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActions(), Promotion_.actions));
            }
            if (criteria.getPriorityscore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriorityscore(), Promotion_.priorityscore));
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(Promotion_.jorders, JoinType.LEFT).get(Jorder_.id))
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChannelId(), root -> root.join(Promotion_.channels, JoinType.LEFT).get(Channel_.id))
                    );
            }
        }
        return specification;
    }
}
