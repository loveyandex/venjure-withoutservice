package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.HistoryEntry;
import com.venjure.repository.HistoryEntryRepository;
import com.venjure.service.criteria.HistoryEntryCriteria;
import com.venjure.service.dto.HistoryEntryDTO;
import com.venjure.service.mapper.HistoryEntryMapper;
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
 * Service for executing complex queries for {@link HistoryEntry} entities in the database.
 * The main input is a {@link HistoryEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HistoryEntryDTO} or a {@link Page} of {@link HistoryEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoryEntryQueryService extends QueryService<HistoryEntry> {

    private final Logger log = LoggerFactory.getLogger(HistoryEntryQueryService.class);

    private final HistoryEntryRepository historyEntryRepository;

    private final HistoryEntryMapper historyEntryMapper;

    public HistoryEntryQueryService(HistoryEntryRepository historyEntryRepository, HistoryEntryMapper historyEntryMapper) {
        this.historyEntryRepository = historyEntryRepository;
        this.historyEntryMapper = historyEntryMapper;
    }

    /**
     * Return a {@link List} of {@link HistoryEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HistoryEntryDTO> findByCriteria(HistoryEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HistoryEntry> specification = createSpecification(criteria);
        return historyEntryMapper.toDto(historyEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HistoryEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoryEntryDTO> findByCriteria(HistoryEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoryEntry> specification = createSpecification(criteria);
        return historyEntryRepository.findAll(specification, page).map(historyEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoryEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HistoryEntry> specification = createSpecification(criteria);
        return historyEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoryEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoryEntry> createSpecification(HistoryEntryCriteria criteria) {
        Specification<HistoryEntry> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HistoryEntry_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), HistoryEntry_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), HistoryEntry_.updatedat));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), HistoryEntry_.type));
            }
            if (criteria.getIspublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIspublic(), HistoryEntry_.ispublic));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), HistoryEntry_.data));
            }
            if (criteria.getDiscriminator() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiscriminator(), HistoryEntry_.discriminator));
            }
            if (criteria.getAdministratorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAdministratorId(),
                            root -> root.join(HistoryEntry_.administrator, JoinType.LEFT).get(Administrator_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(HistoryEntry_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(HistoryEntry_.jorder, JoinType.LEFT).get(Jorder_.id))
                    );
            }
        }
        return specification;
    }
}
