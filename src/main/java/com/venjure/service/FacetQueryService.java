package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Facet;
import com.venjure.repository.FacetRepository;
import com.venjure.service.criteria.FacetCriteria;
import com.venjure.service.dto.FacetDTO;
import com.venjure.service.mapper.FacetMapper;
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
 * Service for executing complex queries for {@link Facet} entities in the database.
 * The main input is a {@link FacetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacetDTO} or a {@link Page} of {@link FacetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacetQueryService extends QueryService<Facet> {

    private final Logger log = LoggerFactory.getLogger(FacetQueryService.class);

    private final FacetRepository facetRepository;

    private final FacetMapper facetMapper;

    public FacetQueryService(FacetRepository facetRepository, FacetMapper facetMapper) {
        this.facetRepository = facetRepository;
        this.facetMapper = facetMapper;
    }

    /**
     * Return a {@link List} of {@link FacetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacetDTO> findByCriteria(FacetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facet> specification = createSpecification(criteria);
        return facetMapper.toDto(facetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetDTO> findByCriteria(FacetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facet> specification = createSpecification(criteria);
        return facetRepository.findAll(specification, page).map(facetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facet> specification = createSpecification(criteria);
        return facetRepository.count(specification);
    }

    /**
     * Function to convert {@link FacetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facet> createSpecification(FacetCriteria criteria) {
        Specification<Facet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Facet_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Facet_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Facet_.updatedat));
            }
            if (criteria.getIsprivate() != null) {
                specification = specification.and(buildSpecification(criteria.getIsprivate(), Facet_.isprivate));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Facet_.code));
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChannelId(), root -> root.join(Facet_.channels, JoinType.LEFT).get(Channel_.id))
                    );
            }
            if (criteria.getFacetTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacetTranslationId(),
                            root -> root.join(Facet_.facetTranslations, JoinType.LEFT).get(FacetTranslation_.id)
                        )
                    );
            }
            if (criteria.getFacetValueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacetValueId(),
                            root -> root.join(Facet_.facetValues, JoinType.LEFT).get(FacetValue_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
