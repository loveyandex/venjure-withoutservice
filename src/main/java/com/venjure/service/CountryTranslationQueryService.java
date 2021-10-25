package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.CountryTranslation;
import com.venjure.repository.CountryTranslationRepository;
import com.venjure.service.criteria.CountryTranslationCriteria;
import com.venjure.service.dto.CountryTranslationDTO;
import com.venjure.service.mapper.CountryTranslationMapper;
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
 * Service for executing complex queries for {@link CountryTranslation} entities in the database.
 * The main input is a {@link CountryTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountryTranslationDTO} or a {@link Page} of {@link CountryTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryTranslationQueryService extends QueryService<CountryTranslation> {

    private final Logger log = LoggerFactory.getLogger(CountryTranslationQueryService.class);

    private final CountryTranslationRepository countryTranslationRepository;

    private final CountryTranslationMapper countryTranslationMapper;

    public CountryTranslationQueryService(
        CountryTranslationRepository countryTranslationRepository,
        CountryTranslationMapper countryTranslationMapper
    ) {
        this.countryTranslationRepository = countryTranslationRepository;
        this.countryTranslationMapper = countryTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link CountryTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountryTranslationDTO> findByCriteria(CountryTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountryTranslation> specification = createSpecification(criteria);
        return countryTranslationMapper.toDto(countryTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountryTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryTranslationDTO> findByCriteria(CountryTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountryTranslation> specification = createSpecification(criteria);
        return countryTranslationRepository.findAll(specification, page).map(countryTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountryTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountryTranslation> specification = createSpecification(criteria);
        return countryTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link CountryTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountryTranslation> createSpecification(CountryTranslationCriteria criteria) {
        Specification<CountryTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountryTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), CountryTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), CountryTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguagecode(), CountryTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CountryTranslation_.name));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(CountryTranslation_.base, JoinType.LEFT).get(Country_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
