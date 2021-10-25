package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Collection;
import com.venjure.repository.CollectionRepository;
import com.venjure.service.criteria.CollectionCriteria;
import com.venjure.service.dto.CollectionDTO;
import com.venjure.service.mapper.CollectionMapper;
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
 * Service for executing complex queries for {@link Collection} entities in the database.
 * The main input is a {@link CollectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollectionDTO} or a {@link Page} of {@link CollectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollectionQueryService extends QueryService<Collection> {

    private final Logger log = LoggerFactory.getLogger(CollectionQueryService.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    public CollectionQueryService(CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
    }

    /**
     * Return a {@link List} of {@link CollectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollectionDTO> findByCriteria(CollectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Collection> specification = createSpecification(criteria);
        return collectionMapper.toDto(collectionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionDTO> findByCriteria(CollectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Collection> specification = createSpecification(criteria);
        return collectionRepository.findAll(specification, page).map(collectionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Collection> specification = createSpecification(criteria);
        return collectionRepository.count(specification);
    }

    /**
     * Function to convert {@link CollectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Collection> createSpecification(CollectionCriteria criteria) {
        Specification<Collection> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Collection_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Collection_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Collection_.updatedat));
            }
            if (criteria.getIsroot() != null) {
                specification = specification.and(buildSpecification(criteria.getIsroot(), Collection_.isroot));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), Collection_.position));
            }
            if (criteria.getIsprivate() != null) {
                specification = specification.and(buildSpecification(criteria.getIsprivate(), Collection_.isprivate));
            }
            if (criteria.getFilters() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilters(), Collection_.filters));
            }
            if (criteria.getFeaturedassetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFeaturedassetId(),
                            root -> root.join(Collection_.featuredasset, JoinType.LEFT).get(Asset_.id)
                        )
                    );
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParentId(), root -> root.join(Collection_.parent, JoinType.LEFT).get(Collection_.id))
                    );
            }
            if (criteria.getCollectionTranslationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCollectionTranslationId(),
                            root -> root.join(Collection_.collectionTranslations, JoinType.LEFT).get(CollectionTranslation_.id)
                        )
                    );
            }
            if (criteria.getProductvariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductvariantId(),
                            root -> root.join(Collection_.productvariants, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
