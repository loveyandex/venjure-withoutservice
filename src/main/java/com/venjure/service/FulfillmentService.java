package com.venjure.service;

import com.venjure.domain.Fulfillment;
import com.venjure.repository.FulfillmentRepository;
import com.venjure.service.dto.FulfillmentDTO;
import com.venjure.service.mapper.FulfillmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fulfillment}.
 */
@Service
@Transactional
public class FulfillmentService {

    private final Logger log = LoggerFactory.getLogger(FulfillmentService.class);

    private final FulfillmentRepository fulfillmentRepository;

    private final FulfillmentMapper fulfillmentMapper;

    public FulfillmentService(FulfillmentRepository fulfillmentRepository, FulfillmentMapper fulfillmentMapper) {
        this.fulfillmentRepository = fulfillmentRepository;
        this.fulfillmentMapper = fulfillmentMapper;
    }

    /**
     * Save a fulfillment.
     *
     * @param fulfillmentDTO the entity to save.
     * @return the persisted entity.
     */
    public FulfillmentDTO save(FulfillmentDTO fulfillmentDTO) {
        log.debug("Request to save Fulfillment : {}", fulfillmentDTO);
        Fulfillment fulfillment = fulfillmentMapper.toEntity(fulfillmentDTO);
        fulfillment = fulfillmentRepository.save(fulfillment);
        return fulfillmentMapper.toDto(fulfillment);
    }

    /**
     * Partially update a fulfillment.
     *
     * @param fulfillmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FulfillmentDTO> partialUpdate(FulfillmentDTO fulfillmentDTO) {
        log.debug("Request to partially update Fulfillment : {}", fulfillmentDTO);

        return fulfillmentRepository
            .findById(fulfillmentDTO.getId())
            .map(
                existingFulfillment -> {
                    fulfillmentMapper.partialUpdate(existingFulfillment, fulfillmentDTO);
                    return existingFulfillment;
                }
            )
            .map(fulfillmentRepository::save)
            .map(fulfillmentMapper::toDto);
    }

    /**
     * Get all the fulfillments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FulfillmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fulfillments");
        return fulfillmentRepository.findAll(pageable).map(fulfillmentMapper::toDto);
    }

    /**
     * Get one fulfillment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FulfillmentDTO> findOne(Long id) {
        log.debug("Request to get Fulfillment : {}", id);
        return fulfillmentRepository.findById(id).map(fulfillmentMapper::toDto);
    }

    /**
     * Delete the fulfillment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fulfillment : {}", id);
        fulfillmentRepository.deleteById(id);
    }
}
