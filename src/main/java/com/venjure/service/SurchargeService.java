package com.venjure.service;

import com.venjure.domain.Surcharge;
import com.venjure.repository.SurchargeRepository;
import com.venjure.service.dto.SurchargeDTO;
import com.venjure.service.mapper.SurchargeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Surcharge}.
 */
@Service
@Transactional
public class SurchargeService {

    private final Logger log = LoggerFactory.getLogger(SurchargeService.class);

    private final SurchargeRepository surchargeRepository;

    private final SurchargeMapper surchargeMapper;

    public SurchargeService(SurchargeRepository surchargeRepository, SurchargeMapper surchargeMapper) {
        this.surchargeRepository = surchargeRepository;
        this.surchargeMapper = surchargeMapper;
    }

    /**
     * Save a surcharge.
     *
     * @param surchargeDTO the entity to save.
     * @return the persisted entity.
     */
    public SurchargeDTO save(SurchargeDTO surchargeDTO) {
        log.debug("Request to save Surcharge : {}", surchargeDTO);
        Surcharge surcharge = surchargeMapper.toEntity(surchargeDTO);
        surcharge = surchargeRepository.save(surcharge);
        return surchargeMapper.toDto(surcharge);
    }

    /**
     * Partially update a surcharge.
     *
     * @param surchargeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SurchargeDTO> partialUpdate(SurchargeDTO surchargeDTO) {
        log.debug("Request to partially update Surcharge : {}", surchargeDTO);

        return surchargeRepository
            .findById(surchargeDTO.getId())
            .map(
                existingSurcharge -> {
                    surchargeMapper.partialUpdate(existingSurcharge, surchargeDTO);
                    return existingSurcharge;
                }
            )
            .map(surchargeRepository::save)
            .map(surchargeMapper::toDto);
    }

    /**
     * Get all the surcharges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SurchargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Surcharges");
        return surchargeRepository.findAll(pageable).map(surchargeMapper::toDto);
    }

    /**
     * Get one surcharge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SurchargeDTO> findOne(Long id) {
        log.debug("Request to get Surcharge : {}", id);
        return surchargeRepository.findById(id).map(surchargeMapper::toDto);
    }

    /**
     * Delete the surcharge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Surcharge : {}", id);
        surchargeRepository.deleteById(id);
    }
}
