package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.FacetValue;
import com.venjure.repository.FacetValueRepository;
import com.venjure.service.criteria.FacetValueCriteria;
import com.venjure.service.dto.FacetValueDTO;
import com.venjure.service.mapper.FacetValueMapper;
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
 * Service for executing complex queries for {@link FacetValue} entities in the database.
 * The main input is a {@link FacetValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacetValueDTO} or a {@link Page} of {@link FacetValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacetValueQueryService extends QueryService<FacetValue> {

    private final Logger log = LoggerFactory.getLogger(FacetValueQueryService.class);

    private final FacetValueRepository facetValueRepository;

    private final FacetValueMapper facetValueMapper;

    public FacetValueQueryService(FacetValueRepository facetValueRepository, FacetValueMapper facetValueMapper) {
        this.facetValueRepository = facetValueRepository;
        this.facetValueMapper = facetValueMapper;
    }

    /**
     * Return a {@link List} of {@link FacetValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacetValueDTO> findByCriteria(FacetValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FacetValue> specification = createSpecification(criteria);
        return facetValueMapper.toDto(facetValueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacetValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetValueDTO> findByCriteria(FacetValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacetValue> specification = createSpecification(criteria);
        return facetValueRepository.findAll(specification, page).map(facetValueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacetValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FacetValue> specification = createSpecification(criteria);
        return facetValueRepository.count(specification);
    }

    /**
     * Function to convert {@link FacetValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacetValue> createSpecification(FacetValueCriteria criteria) {
        Specification<FacetValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FacetValue_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), FacetValue_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), FacetValue_.updatedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), FacetValue_.code));
            }
            if (criteria.getFacetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacetId(), root -> root.join(FacetValue_.facet, JoinType.LEFT).get(Facet_.id))
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChannelId(), root -> root.join(FacetValue_.channels, JoinType.LEFT).get(Channel_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(FacetValue_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getFacetValueTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacetValueTranslationId(),
                            root -> root.join(FacetValue_.facetValueTranslations, JoinType.LEFT).get(FacetValueTranslation_.id)
                        )
                    );
            }
            if (criteria.getProductVariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantId(),
                            root -> root.join(FacetValue_.productVariants, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
