package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Address;
import com.venjure.repository.AddressRepository;
import com.venjure.service.criteria.AddressCriteria;
import com.venjure.service.dto.AddressDTO;
import com.venjure.service.mapper.AddressMapper;
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
 * Service for executing complex queries for {@link Address} entities in the database.
 * The main input is a {@link AddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddressDTO} or a {@link Page} of {@link AddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressQueryService extends QueryService<Address> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    public AddressQueryService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Return a {@link List} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> findByCriteria(AddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Address> specification = createSpecification(criteria);
        return addressMapper.toDto(addressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findByCriteria(AddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.findAll(specification, page).map(addressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.count(specification);
    }

    /**
     * Function to convert {@link AddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Address> createSpecification(AddressCriteria criteria) {
        Specification<Address> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Address_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Address_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Address_.updatedat));
            }
            if (criteria.getFullname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullname(), Address_.fullname));
            }
            if (criteria.getCompany() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompany(), Address_.company));
            }
            if (criteria.getStreetline1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetline1(), Address_.streetline1));
            }
            if (criteria.getStreetline2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetline2(), Address_.streetline2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Address_.city));
            }
            if (criteria.getProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvince(), Address_.province));
            }
            if (criteria.getPostalcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalcode(), Address_.postalcode));
            }
            if (criteria.getPhonenumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhonenumber(), Address_.phonenumber));
            }
            if (criteria.getDefaultshippingaddress() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDefaultshippingaddress(), Address_.defaultshippingaddress));
            }
            if (criteria.getDefaultbillingaddress() != null) {
                specification = specification.and(buildSpecification(criteria.getDefaultbillingaddress(), Address_.defaultbillingaddress));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Address_.customer, JoinType.LEFT).get(Customer_.id))
                    );
            }
            if (criteria.getCountryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountryId(), root -> root.join(Address_.country, JoinType.LEFT).get(Country_.id))
                    );
            }
        }
        return specification;
    }
}
