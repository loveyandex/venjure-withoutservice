package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.ExampleEntity;
import com.venjure.repository.ExampleEntityRepository;
import com.venjure.service.criteria.ExampleEntityCriteria;
import com.venjure.service.dto.ExampleEntityDTO;
import com.venjure.service.mapper.ExampleEntityMapper;
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
 * Service for executing complex queries for {@link ExampleEntity} entities in the database.
 * The main input is a {@link ExampleEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExampleEntityDTO} or a {@link Page} of {@link ExampleEntityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExampleEntityQueryService extends QueryService<ExampleEntity> {

    private final Logger log = LoggerFactory.getLogger(ExampleEntityQueryService.class);

    private final ExampleEntityRepository exampleEntityRepository;

    private final ExampleEntityMapper exampleEntityMapper;

    public ExampleEntityQueryService(ExampleEntityRepository exampleEntityRepository, ExampleEntityMapper exampleEntityMapper) {
        this.exampleEntityRepository = exampleEntityRepository;
        this.exampleEntityMapper = exampleEntityMapper;
    }

    /**
     * Return a {@link List} of {@link ExampleEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExampleEntityDTO> findByCriteria(ExampleEntityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExampleEntity> specification = createSpecification(criteria);
        return exampleEntityMapper.toDto(exampleEntityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExampleEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExampleEntityDTO> findByCriteria(ExampleEntityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExampleEntity> specification = createSpecification(criteria);
        return exampleEntityRepository.findAll(specification, page).map(exampleEntityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExampleEntityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExampleEntity> specification = createSpecification(criteria);
        return exampleEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link ExampleEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExampleEntity> createSpecification(ExampleEntityCriteria criteria) {
        Specification<ExampleEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExampleEntity_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), ExampleEntity_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), ExampleEntity_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ExampleEntity_.name));
            }
        }
        return specification;
    }
}
