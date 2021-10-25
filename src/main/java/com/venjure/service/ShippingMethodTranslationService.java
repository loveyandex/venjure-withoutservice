package com.venjure.service;

import com.venjure.domain.ShippingMethodTranslation;
import com.venjure.repository.ShippingMethodTranslationRepository;
import com.venjure.service.dto.ShippingMethodTranslationDTO;
import com.venjure.service.mapper.ShippingMethodTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShippingMethodTranslation}.
 */
@Service
@Transactional
public class ShippingMethodTranslationService {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodTranslationService.class);

    private final ShippingMethodTranslationRepository shippingMethodTranslationRepository;

    private final ShippingMethodTranslationMapper shippingMethodTranslationMapper;

    public ShippingMethodTranslationService(
        ShippingMethodTranslationRepository shippingMethodTranslationRepository,
        ShippingMethodTranslationMapper shippingMethodTranslationMapper
    ) {
        this.shippingMethodTranslationRepository = shippingMethodTranslationRepository;
        this.shippingMethodTranslationMapper = shippingMethodTranslationMapper;
    }

    /**
     * Save a shippingMethodTranslation.
     *
     * @param shippingMethodTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ShippingMethodTranslationDTO save(ShippingMethodTranslationDTO shippingMethodTranslationDTO) {
        log.debug("Request to save ShippingMethodTranslation : {}", shippingMethodTranslationDTO);
        ShippingMethodTranslation shippingMethodTranslation = shippingMethodTranslationMapper.toEntity(shippingMethodTranslationDTO);
        shippingMethodTranslation = shippingMethodTranslationRepository.save(shippingMethodTranslation);
        return shippingMethodTranslationMapper.toDto(shippingMethodTranslation);
    }

    /**
     * Partially update a shippingMethodTranslation.
     *
     * @param shippingMethodTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShippingMethodTranslationDTO> partialUpdate(ShippingMethodTranslationDTO shippingMethodTranslationDTO) {
        log.debug("Request to partially update ShippingMethodTranslation : {}", shippingMethodTranslationDTO);

        return shippingMethodTranslationRepository
            .findById(shippingMethodTranslationDTO.getId())
            .map(
                existingShippingMethodTranslation -> {
                    shippingMethodTranslationMapper.partialUpdate(existingShippingMethodTranslation, shippingMethodTranslationDTO);
                    return existingShippingMethodTranslation;
                }
            )
            .map(shippingMethodTranslationRepository::save)
            .map(shippingMethodTranslationMapper::toDto);
    }

    /**
     * Get all the shippingMethodTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingMethodTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShippingMethodTranslations");
        return shippingMethodTranslationRepository.findAll(pageable).map(shippingMethodTranslationMapper::toDto);
    }

    /**
     * Get one shippingMethodTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShippingMethodTranslationDTO> findOne(Long id) {
        log.debug("Request to get ShippingMethodTranslation : {}", id);
        return shippingMethodTranslationRepository.findById(id).map(shippingMethodTranslationMapper::toDto);
    }

    /**
     * Delete the shippingMethodTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShippingMethodTranslation : {}", id);
        shippingMethodTranslationRepository.deleteById(id);
    }
}
