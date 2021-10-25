package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.CustomerGroup;
import com.venjure.repository.CustomerGroupRepository;
import com.venjure.service.criteria.CustomerGroupCriteria;
import com.venjure.service.dto.CustomerGroupDTO;
import com.venjure.service.mapper.CustomerGroupMapper;
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
 * Service for executing complex queries for {@link CustomerGroup} entities in the database.
 * The main input is a {@link CustomerGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerGroupDTO} or a {@link Page} of {@link CustomerGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerGroupQueryService extends QueryService<CustomerGroup> {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupQueryService.class);

    private final CustomerGroupRepository customerGroupRepository;

    private final CustomerGroupMapper customerGroupMapper;

    public CustomerGroupQueryService(CustomerGroupRepository customerGroupRepository, CustomerGroupMapper customerGroupMapper) {
        this.customerGroupRepository = customerGroupRepository;
        this.customerGroupMapper = customerGroupMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerGroupDTO> findByCriteria(CustomerGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerGroup> specification = createSpecification(criteria);
        return customerGroupMapper.toDto(customerGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerGroupDTO> findByCriteria(CustomerGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerGroup> specification = createSpecification(criteria);
        return customerGroupRepository.findAll(specification, page).map(customerGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerGroup> specification = createSpecification(criteria);
        return customerGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerGroup> createSpecification(CustomerGroupCriteria criteria) {
        Specification<CustomerGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerGroup_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), CustomerGroup_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), CustomerGroup_.updatedat));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CustomerGroup_.name));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(CustomerGroup_.customers, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
