package com.venjure.service;

import com.venjure.domain.Pogt;
import com.venjure.repository.PogtRepository;
import com.venjure.service.dto.PogtDTO;
import com.venjure.service.mapper.PogtMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pogt}.
 */
@Service
@Transactional
public class PogtService {

    private final Logger log = LoggerFactory.getLogger(PogtService.class);

    private final PogtRepository pogtRepository;

    private final PogtMapper pogtMapper;

    public PogtService(PogtRepository pogtRepository, PogtMapper pogtMapper) {
        this.pogtRepository = pogtRepository;
        this.pogtMapper = pogtMapper;
    }

    /**
     * Save a pogt.
     *
     * @param pogtDTO the entity to save.
     * @return the persisted entity.
     */
    public PogtDTO save(PogtDTO pogtDTO) {
        log.debug("Request to save Pogt : {}", pogtDTO);
        Pogt pogt = pogtMapper.toEntity(pogtDTO);
        pogt = pogtRepository.save(pogt);
        return pogtMapper.toDto(pogt);
    }

    /**
     * Partially update a pogt.
     *
     * @param pogtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PogtDTO> partialUpdate(PogtDTO pogtDTO) {
        log.debug("Request to partially update Pogt : {}", pogtDTO);

        return pogtRepository
            .findById(pogtDTO.getId())
            .map(
                existingPogt -> {
                    pogtMapper.partialUpdate(existingPogt, pogtDTO);
                    return existingPogt;
                }
            )
            .map(pogtRepository::save)
            .map(pogtMapper::toDto);
    }

    /**
     * Get all the pogts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PogtDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pogts");
        return pogtRepository.findAll(pageable).map(pogtMapper::toDto);
    }

    /**
     * Get one pogt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PogtDTO> findOne(Long id) {
        log.debug("Request to get Pogt : {}", id);
        return pogtRepository.findById(id).map(pogtMapper::toDto);
    }

    /**
     * Delete the pogt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pogt : {}", id);
        pogtRepository.deleteById(id);
    }
}
