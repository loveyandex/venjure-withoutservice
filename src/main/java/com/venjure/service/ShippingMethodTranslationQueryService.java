package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ShippingMethodTranslation;
import com.venjure.repository.ShippingMethodTranslationRepository;
import com.venjure.service.criteria.ShippingMethodTranslationCriteria;
import com.venjure.service.dto.ShippingMethodTranslationDTO;
import com.venjure.service.mapper.ShippingMethodTranslationMapper;
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
 * Service for executing complex queries for {@link ShippingMethodTranslation} entities in the database.
 * The main input is a {@link ShippingMethodTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShippingMethodTranslationDTO} or a {@link Page} of {@link ShippingMethodTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShippingMethodTranslationQueryService extends QueryService<ShippingMethodTranslation> {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodTranslationQueryService.class);

    private final ShippingMethodTranslationRepository shippingMethodTranslationRepository;

    private final ShippingMethodTranslationMapper shippingMethodTranslationMapper;

    public ShippingMethodTranslationQueryService(
        ShippingMethodTranslationRepository shippingMethodTranslationRepository,
        ShippingMethodTranslationMapper shippingMethodTranslationMapper
    ) {
        this.shippingMethodTranslationRepository = shippingMethodTranslationRepository;
        this.shippingMethodTranslationMapper = shippingMethodTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link ShippingMethodTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingMethodTranslationDTO> findByCriteria(ShippingMethodTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShippingMethodTranslation> specification = createSpecification(criteria);
        return shippingMethodTranslationMapper.toDto(shippingMethodTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShippingMethodTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingMethodTranslationDTO> findByCriteria(ShippingMethodTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShippingMethodTranslation> specification = createSpecification(criteria);
        return shippingMethodTranslationRepository.findAll(specification, page).map(shippingMethodTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShippingMethodTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShippingMethodTranslation> specification = createSpecification(criteria);
        return shippingMethodTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link ShippingMethodTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShippingMethodTranslation> createSpecification(ShippingMethodTranslationCriteria criteria) {
        Specification<ShippingMethodTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShippingMethodTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ShippingMethodTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ShippingMethodTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLanguagecode(), ShippingMethodTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ShippingMethodTranslation_.name));
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), ShippingMethodTranslation_.description));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(ShippingMethodTranslation_.base, JoinType.LEFT).get(ShippingMethod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
