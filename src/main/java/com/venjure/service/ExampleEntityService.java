package com.venjure.service;

import com.venjure.domain.ExampleEntity;
import com.venjure.repository.ExampleEntityRepository;
import com.venjure.service.dto.ExampleEntityDTO;
import com.venjure.service.mapper.ExampleEntityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExampleEntity}.
 */
@Service
@Transactional
public class ExampleEntityService {

    private final Logger log = LoggerFactory.getLogger(ExampleEntityService.class);

    private final ExampleEntityRepository exampleEntityRepository;

    private final ExampleEntityMapper exampleEntityMapper;

    public ExampleEntityService(ExampleEntityRepository exampleEntityRepository, ExampleEntityMapper exampleEntityMapper) {
        this.exampleEntityRepository = exampleEntityRepository;
        this.exampleEntityMapper = exampleEntityMapper;
    }

    /**
     * Save a exampleEntity.
     *
     * @param exampleEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public ExampleEntityDTO save(ExampleEntityDTO exampleEntityDTO) {
        log.debug("Request to save ExampleEntity : {}", exampleEntityDTO);
        ExampleEntity exampleEntity = exampleEntityMapper.toEntity(exampleEntityDTO);
        exampleEntity = exampleEntityRepository.save(exampleEntity);
        return exampleEntityMapper.toDto(exampleEntity);
    }

    /**
     * Partially update a exampleEntity.
     *
     * @param exampleEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExampleEntityDTO> partialUpdate(ExampleEntityDTO exampleEntityDTO) {
        log.debug("Request to partially update ExampleEntity : {}", exampleEntityDTO);

        return exampleEntityRepository
            .findById(exampleEntityDTO.getId())
            .map(
                existingExampleEntity -> {
                    exampleEntityMapper.partialUpdate(existingExampleEntity, exampleEntityDTO);
                    return existingExampleEntity;
                }
            )
            .map(exampleEntityRepository::save)
            .map(exampleEntityMapper::toDto);
    }

    /**
     * Get all the exampleEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExampleEntityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExampleEntities");
        return exampleEntityRepository.findAll(pageable).map(exampleEntityMapper::toDto);
    }

    /**
     * Get one exampleEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExampleEntityDTO> findOne(Long id) {
        log.debug("Request to get ExampleEntity : {}", id);
        return exampleEntityRepository.findById(id).map(exampleEntityMapper::toDto);
    }

    /**
     * Delete the exampleEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExampleEntity : {}", id);
        exampleEntityRepository.deleteById(id);
    }
}
