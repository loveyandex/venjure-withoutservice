package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.CollectionTranslation;
import com.venjure.repository.CollectionTranslationRepository;
import com.venjure.service.criteria.CollectionTranslationCriteria;
import com.venjure.service.dto.CollectionTranslationDTO;
import com.venjure.service.mapper.CollectionTranslationMapper;
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
 * Service for executing complex queries for {@link CollectionTranslation} entities in the database.
 * The main input is a {@link CollectionTranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollectionTranslationDTO} or a {@link Page} of {@link CollectionTranslationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollectionTranslationQueryService extends QueryService<CollectionTranslation> {

    private final Logger log = LoggerFactory.getLogger(CollectionTranslationQueryService.class);

    private final CollectionTranslationRepository collectionTranslationRepository;

    private final CollectionTranslationMapper collectionTranslationMapper;

    public CollectionTranslationQueryService(
        CollectionTranslationRepository collectionTranslationRepository,
        CollectionTranslationMapper collectionTranslationMapper
    ) {
        this.collectionTranslationRepository = collectionTranslationRepository;
        this.collectionTranslationMapper = collectionTranslationMapper;
    }

    /**
     * Return a {@link List} of {@link CollectionTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollectionTranslationDTO> findByCriteria(CollectionTranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CollectionTranslation> specification = createSpecification(criteria);
        return collectionTranslationMapper.toDto(collectionTranslationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollectionTranslationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionTranslationDTO> findByCriteria(CollectionTranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CollectionTranslation> specification = createSpecification(criteria);
        return collectionTranslationRepository.findAll(specification, page).map(collectionTranslationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollectionTranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CollectionTranslation> specification = createSpecification(criteria);
        return collectionTranslationRepository.count(specification);
    }

    /**
     * Function to convert {@link CollectionTranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CollectionTranslation> createSpecification(CollectionTranslationCriteria criteria) {
        Specification<CollectionTranslation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CollectionTranslation_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), CollectionTranslation_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), CollectionTranslation_.updatedat));
            }
            if (criteria.getLanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLanguagecode(), CollectionTranslation_.languagecode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CollectionTranslation_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), CollectionTranslation_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CollectionTranslation_.description));
            }
            if (criteria.getBaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBaseId(),
                            root -> root.join(CollectionTranslation_.base, JoinType.LEFT).get(Collection_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
