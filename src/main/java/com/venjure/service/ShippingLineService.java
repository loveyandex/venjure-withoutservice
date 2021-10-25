package com.venjure.service;

import com.venjure.domain.ShippingLine;
import com.venjure.repository.ShippingLineRepository;
import com.venjure.service.dto.ShippingLineDTO;
import com.venjure.service.mapper.ShippingLineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShippingLine}.
 */
@Service
@Transactional
public class ShippingLineService {

    private final Logger log = LoggerFactory.getLogger(ShippingLineService.class);

    private final ShippingLineRepository shippingLineRepository;

    private final ShippingLineMapper shippingLineMapper;

    public ShippingLineService(ShippingLineRepository shippingLineRepository, ShippingLineMapper shippingLineMapper) {
        this.shippingLineRepository = shippingLineRepository;
        this.shippingLineMapper = shippingLineMapper;
    }

    /**
     * Save a shippingLine.
     *
     * @param shippingLineDTO the entity to save.
     * @return the persisted entity.
     */
    public ShippingLineDTO save(ShippingLineDTO shippingLineDTO) {
        log.debug("Request to save ShippingLine : {}", shippingLineDTO);
        ShippingLine shippingLine = shippingLineMapper.toEntity(shippingLineDTO);
        shippingLine = shippingLineRepository.save(shippingLine);
        return shippingLineMapper.toDto(shippingLine);
    }

    /**
     * Partially update a shippingLine.
     *
     * @param shippingLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShippingLineDTO> partialUpdate(ShippingLineDTO shippingLineDTO) {
        log.debug("Request to partially update ShippingLine : {}", shippingLineDTO);

        return shippingLineRepository
            .findById(shippingLineDTO.getId())
            .map(
                existingShippingLine -> {
                    shippingLineMapper.partialUpdate(existingShippingLine, shippingLineDTO);
                    return existingShippingLine;
                }
            )
            .map(shippingLineRepository::save)
            .map(shippingLineMapper::toDto);
    }

    /**
     * Get all the shippingLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShippingLines");
        return shippingLineRepository.findAll(pageable).map(shippingLineMapper::toDto);
    }

    /**
     * Get one shippingLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShippingLineDTO> findOne(Long id) {
        log.debug("Request to get ShippingLine : {}", id);
        return shippingLineRepository.findById(id).map(shippingLineMapper::toDto);
    }

    /**
     * Delete the shippingLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShippingLine : {}", id);
        shippingLineRepository.deleteById(id);
    }
}
