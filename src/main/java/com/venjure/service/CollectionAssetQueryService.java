package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.CollectionAsset;
import com.venjure.repository.CollectionAssetRepository;
import com.venjure.service.criteria.CollectionAssetCriteria;
import com.venjure.service.dto.CollectionAssetDTO;
import com.venjure.service.mapper.CollectionAssetMapper;
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
 * Service for executing complex queries for {@link CollectionAsset} entities in the database.
 * The main input is a {@link CollectionAssetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollectionAssetDTO} or a {@link Page} of {@link CollectionAssetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollectionAssetQueryService extends QueryService<CollectionAsset> {

    private final Logger log = LoggerFactory.getLogger(CollectionAssetQueryService.class);

    private final CollectionAssetRepository collectionAssetRepository;

    private final CollectionAssetMapper collectionAssetMapper;

    public CollectionAssetQueryService(CollectionAssetRepository collectionAssetRepository, CollectionAssetMapper collectionAssetMapper) {
        this.collectionAssetRepository = collectionAssetRepository;
        this.collectionAssetMapper = collectionAssetMapper;
    }

    /**
     * Return a {@link List} of {@link CollectionAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollectionAssetDTO> findByCriteria(CollectionAssetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CollectionAsset> specification = createSpecification(criteria);
        return collectionAssetMapper.toDto(collectionAssetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollectionAssetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionAssetDTO> findByCriteria(CollectionAssetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CollectionAsset> specification = createSpecification(criteria);
        return collectionAssetRepository.findAll(specification, page).map(collectionAssetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollectionAssetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CollectionAsset> specification = createSpecification(criteria);
        return collectionAssetRepository.count(specification);
    }

    /**
     * Function to convert {@link CollectionAssetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CollectionAsset> createSpecification(CollectionAssetCriteria criteria) {
        Specification<CollectionAsset> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CollectionAsset_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), CollectionAsset_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), CollectionAsset_.updatedat));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), CollectionAsset_.position));
            }
            if (criteria.getAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAssetId(), root -> root.join(CollectionAsset_.asset, JoinType.LEFT).get(Asset_.id))
                    );
            }
            if (criteria.getCollectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCollectionId(),
                            root -> root.join(CollectionAsset_.collection, JoinType.LEFT).get(Collection_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
