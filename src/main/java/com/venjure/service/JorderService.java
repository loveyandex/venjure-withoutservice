package com.venjure.service;

import com.venjure.domain.Jorder;
import com.venjure.repository.JorderRepository;
import com.venjure.service.dto.JorderDTO;
import com.venjure.service.mapper.JorderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Jorder}.
 */
@Service
@Transactional
public class JorderService {

    private final Logger log = LoggerFactory.getLogger(JorderService.class);

    private final JorderRepository jorderRepository;

    private final JorderMapper jorderMapper;

    public JorderService(JorderRepository jorderRepository, JorderMapper jorderMapper) {
        this.jorderRepository = jorderRepository;
        this.jorderMapper = jorderMapper;
    }

    /**
     * Save a jorder.
     *
     * @param jorderDTO the entity to save.
     * @return the persisted entity.
     */
    public JorderDTO save(JorderDTO jorderDTO) {
        log.debug("Request to save Jorder : {}", jorderDTO);
        Jorder jorder = jorderMapper.toEntity(jorderDTO);
        jorder = jorderRepository.save(jorder);
        return jorderMapper.toDto(jorder);
    }

    /**
     * Partially update a jorder.
     *
     * @param jorderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JorderDTO> partialUpdate(JorderDTO jorderDTO) {
        log.debug("Request to partially update Jorder : {}", jorderDTO);

        return jorderRepository
            .findById(jorderDTO.getId())
            .map(
                existingJorder -> {
                    jorderMapper.partialUpdate(existingJorder, jorderDTO);
                    return existingJorder;
                }
            )
            .map(jorderRepository::save)
            .map(jorderMapper::toDto);
    }

    /**
     * Get all the jorders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JorderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jorders");
        return jorderRepository.findAll(pageable).map(jorderMapper::toDto);
    }

    /**
     * Get all the jorders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JorderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jorderRepository.findAllWithEagerRelationships(pageable).map(jorderMapper::toDto);
    }

    /**
     * Get one jorder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JorderDTO> findOne(Long id) {
        log.debug("Request to get Jorder : {}", id);
        return jorderRepository.findOneWithEagerRelationships(id).map(jorderMapper::toDto);
    }

    /**
     * Delete the jorder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Jorder : {}", id);
        jorderRepository.deleteById(id);
    }
}
