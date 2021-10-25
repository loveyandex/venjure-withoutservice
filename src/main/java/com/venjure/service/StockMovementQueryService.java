package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.StockMovement;
import com.venjure.repository.StockMovementRepository;
import com.venjure.service.criteria.StockMovementCriteria;
import com.venjure.service.dto.StockMovementDTO;
import com.venjure.service.mapper.StockMovementMapper;
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
 * Service for executing complex queries for {@link StockMovement} entities in the database.
 * The main input is a {@link StockMovementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockMovementDTO} or a {@link Page} of {@link StockMovementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockMovementQueryService extends QueryService<StockMovement> {

    private final Logger log = LoggerFactory.getLogger(StockMovementQueryService.class);

    private final StockMovementRepository stockMovementRepository;

    private final StockMovementMapper stockMovementMapper;

    public StockMovementQueryService(StockMovementRepository stockMovementRepository, StockMovementMapper stockMovementMapper) {
        this.stockMovementRepository = stockMovementRepository;
        this.stockMovementMapper = stockMovementMapper;
    }

    /**
     * Return a {@link List} of {@link StockMovementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockMovementDTO> findByCriteria(StockMovementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockMovement> specification = createSpecification(criteria);
        return stockMovementMapper.toDto(stockMovementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockMovementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockMovementDTO> findByCriteria(StockMovementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockMovement> specification = createSpecification(criteria);
        return stockMovementRepository.findAll(specification, page).map(stockMovementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockMovementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockMovement> specification = createSpecification(criteria);
        return stockMovementRepository.count(specification);
    }

    /**
     * Function to convert {@link StockMovementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StockMovement> createSpecification(StockMovementCriteria criteria) {
        Specification<StockMovement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StockMovement_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), StockMovement_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), StockMovement_.updatedat));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), StockMovement_.type));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), StockMovement_.quantity));
            }
            if (criteria.getDiscriminator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiscriminator(), StockMovement_.discriminator));
            }
            if (criteria.getProductvariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductvariantId(),
                            root -> root.join(StockMovement_.productvariant, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
            if (criteria.getOrderitemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderitemId(),
                            root -> root.join(StockMovement_.orderitem, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getOrderlineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderlineId(),
                            root -> root.join(StockMovement_.orderline, JoinType.LEFT).get(OrderLine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
