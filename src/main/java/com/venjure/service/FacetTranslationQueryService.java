package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.FacetTranslation;
import com.venjure.repository.FacetTranslationRepository;
import com.venjure.service.criteria.FacetTranslationCriteria;
import com.venjure.service.dto.FacetTranslationDTO;
import com.venjure.service.mapper.FacetTranslationMapper;
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
 * Service for executing complex queries for {@link FacetTranslation} entities in the database.
 * The main input is a {@link FacetTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacetTranslationDTO} or a {@link Page} of {@link FacetTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacetTranslationQueryService extends QueryService<FacetTranslation> {

    private final Logger log = LoggerFactory.getLogger(FacetTranslationQueryService.class);

    private final FacetTranslationRepository facetTranslationRepository;

    private final FacetTranslationMapper facetTranslationMapper;

    public FacetTranslationQueryService(
        FacetTranslationRepository facetTranslationRepository,
        FacetTranslationMapper facetTranslationMapper
    ) {
        this.facetTranslationRepository = facetTranslationRepository;
        this.facetTranslationMapper = facetTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link FacetTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacetTranslationDTO> findByCriteria(FacetTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FacetTranslation> specification = createSpecification(criteria);
        return facetTranslationMapper.toDto(facetTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacetTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetTranslationDTO> findByCriteria(FacetTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacetTranslation> specification = createSpecification(criteria);
        return facetTranslationRepository.findAll(specification, page).map(facetTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacetTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FacetTranslation> specification = createSpecification(criteria);
        return facetTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link FacetTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacetTranslation> createSpecification(FacetTranslationCriteria criteria) {
        Specification<FacetTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FacetTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), FacetTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), FacetTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguagecode(), FacetTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FacetTranslation_.name));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBaseId(), root -> root.join(FacetTranslation_.base, JoinType.LEFT).get(Facet_.id))
                    );
            }
        }
        return specification;
    }
}
