package com.venjure.service;

import com.venjure.domain.ShippingMethod;
import com.venjure.repository.ShippingMethodRepository;
import com.venjure.service.dto.ShippingMethodDTO;
import com.venjure.service.mapper.ShippingMethodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShippingMethod}.
 */
@Service
@Transactional
public class ShippingMethodService {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodService.class);

    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMethodMapper shippingMethodMapper;

    public ShippingMethodService(ShippingMethodRepository shippingMethodRepository, ShippingMethodMapper shippingMethodMapper) {
        this.shippingMethodRepository = shippingMethodRepository;
        this.shippingMethodMapper = shippingMethodMapper;
    }

    /**
     * Save a shippingMethod.
     *
     * @param shippingMethodDTO the entity to save.
     * @return the persisted entity.
     */
    public ShippingMethodDTO save(ShippingMethodDTO shippingMethodDTO) {
        log.debug("Request to save ShippingMethod : {}", shippingMethodDTO);
        ShippingMethod shippingMethod = shippingMethodMapper.toEntity(shippingMethodDTO);
        shippingMethod = shippingMethodRepository.save(shippingMethod);
        return shippingMethodMapper.toDto(shippingMethod);
    }

    /**
     * Partially update a shippingMethod.
     *
     * @param shippingMethodDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShippingMethodDTO> partialUpdate(ShippingMethodDTO shippingMethodDTO) {
        log.debug("Request to partially update ShippingMethod : {}", shippingMethodDTO);

        return shippingMethodRepository
            .findById(shippingMethodDTO.getId())
            .map(
                existingShippingMethod -> {
                    shippingMethodMapper.partialUpdate(existingShippingMethod, shippingMethodDTO);
                    return existingShippingMethod;
                }
            )
            .map(shippingMethodRepository::save)
            .map(shippingMethodMapper::toDto);
    }

    /**
     * Get all the shippingMethods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShippingMethods");
        return shippingMethodRepository.findAll(pageable).map(shippingMethodMapper::toDto);
    }

    /**
     * Get one shippingMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShippingMethodDTO> findOne(Long id) {
        log.debug("Request to get ShippingMethod : {}", id);
        return shippingMethodRepository.findById(id).map(shippingMethodMapper::toDto);
    }

    /**
     * Delete the shippingMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShippingMethod : {}", id);
        shippingMethodRepository.deleteById(id);
    }
}
