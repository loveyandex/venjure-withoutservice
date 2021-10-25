package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Customer;
import com.venjure.repository.CustomerRepository;
import com.venjure.service.criteria.CustomerCriteria;
import com.venjure.service.dto.CustomerDTO;
import com.venjure.service.mapper.CustomerMapper;
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
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerDTO} or a {@link Page} of {@link CustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerQueryService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerMapper.toDto(customerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page).map(customerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Customer_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Customer_.updatedat));
            }
            if (criteria.getDeletedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeletedat(), Customer_.deletedat));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Customer_.title));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Customer_.firstname));
            }
            if (criteria.getLastname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastname(), Customer_.lastname));
            }
            if (criteria.getPhonenumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhonenumber(), Customer_.phonenumber));
            }
            if (criteria.getEmailaddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailaddress(), Customer_.emailaddress));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Customer_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getAvatarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAvatarId(), root -> root.join(Customer_.avatar, JoinType.LEFT).get(Asset_.id))
                    );
            }
            if (criteria.getChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChannelId(), root -> root.join(Customer_.channels, JoinType.LEFT).get(Channel_.id))
                    );
            }
            if (criteria.getCustomerGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerGroupId(),
                            root -> root.join(Customer_.customerGroups, JoinType.LEFT).get(CustomerGroup_.id)
                        )
                    );
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAddressId(), root -> root.join(Customer_.addresses, JoinType.LEFT).get(Address_.id))
                    );
            }
            if (criteria.getHistoryEntryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHistoryEntryId(),
                            root -> root.join(Customer_.historyEntries, JoinType.LEFT).get(HistoryEntry_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(Customer_.jorders, JoinType.LEFT).get(Jorder_.id))
                    );
            }
        }
        return specification;
    }
}
