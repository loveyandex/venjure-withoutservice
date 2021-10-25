package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.FacetValueTranslation;
import com.venjure.repository.FacetValueTranslationRepository;
import com.venjure.service.criteria.FacetValueTranslationCriteria;
import com.venjure.service.dto.FacetValueTranslationDTO;
import com.venjure.service.mapper.FacetValueTranslationMapper;
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
 * Service for executing complex queries for {@link FacetValueTranslation} entities in the database.
 * The main input is a {@link FacetValueTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacetValueTranslationDTO} or a {@link Page} of {@link FacetValueTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacetValueTranslationQueryService extends QueryService<FacetValueTranslation> {

    private final Logger log = LoggerFactory.getLogger(FacetValueTranslationQueryService.class);

    private final FacetValueTranslationRepository facetValueTranslationRepository;

    private final FacetValueTranslationMapper facetValueTranslationMapper;

    public FacetValueTranslationQueryService(
        FacetValueTranslationRepository facetValueTranslationRepository,
        FacetValueTranslationMapper facetValueTranslationMapper
    ) {
        this.facetValueTranslationRepository = facetValueTranslationRepository;
        this.facetValueTranslationMapper = facetValueTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link FacetValueTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacetValueTranslationDTO> findByCriteria(FacetValueTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FacetValueTranslation> specification = createSpecification(criteria);
        return facetValueTranslationMapper.toDto(facetValueTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacetValueTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetValueTranslationDTO> findByCriteria(FacetValueTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacetValueTranslation> specification = createSpecification(criteria);
        return facetValueTranslationRepository.findAll(specification, page).map(facetValueTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacetValueTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FacetValueTranslation> specification = createSpecification(criteria);
        return facetValueTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link FacetValueTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacetValueTranslation> createSpecification(FacetValueTranslationCriteria criteria) {
        Specification<FacetValueTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FacetValueTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), FacetValueTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), FacetValueTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLanguagecode(), FacetValueTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FacetValueTranslation_.name));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(FacetValueTranslation_.base, JoinType.LEFT).get(FacetValue_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
