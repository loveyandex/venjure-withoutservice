package com.venjure.service;

import com.venjure.domain.CustomerGroup;
import com.venjure.repository.CustomerGroupRepository;
import com.venjure.service.dto.CustomerGroupDTO;
import com.venjure.service.mapper.CustomerGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerGroup}.
 */
@Service
@Transactional
public class CustomerGroupService {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupService.class);

    private final CustomerGroupRepository customerGroupRepository;

    private final CustomerGroupMapper customerGroupMapper;

    public CustomerGroupService(CustomerGroupRepository customerGroupRepository, CustomerGroupMapper customerGroupMapper) {
        this.customerGroupRepository = customerGroupRepository;
        this.customerGroupMapper = customerGroupMapper;
    }

    /**
     * Save a customerGroup.
     *
     * @param customerGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerGroupDTO save(CustomerGroupDTO customerGroupDTO) {
        log.debug("Request to save CustomerGroup : {}", customerGroupDTO);
        CustomerGroup customerGroup = customerGroupMapper.toEntity(customerGroupDTO);
        customerGroup = customerGroupRepository.save(customerGroup);
        return customerGroupMapper.toDto(customerGroup);
    }

    /**
     * Partially update a customerGroup.
     *
     * @param customerGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerGroupDTO> partialUpdate(CustomerGroupDTO customerGroupDTO) {
        log.debug("Request to partially update CustomerGroup : {}", customerGroupDTO);

        return customerGroupRepository
            .findById(customerGroupDTO.getId())
            .map(
                existingCustomerGroup -> {
                    customerGroupMapper.partialUpdate(existingCustomerGroup, customerGroupDTO);
                    return existingCustomerGroup;
                }
            )
            .map(customerGroupRepository::save)
            .map(customerGroupMapper::toDto);
    }

    /**
     * Get all the customerGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerGroups");
        return customerGroupRepository.findAll(pageable).map(customerGroupMapper::toDto);
    }

    /**
     * Get one customerGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerGroupDTO> findOne(Long id) {
        log.debug("Request to get CustomerGroup : {}", id);
        return customerGroupRepository.findById(id).map(customerGroupMapper::toDto);
    }

    /**
     * Delete the customerGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerGroup : {}", id);
        customerGroupRepository.deleteById(id);
    }
}
