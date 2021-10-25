package com.venjure.service;

import com.venjure.domain.Collection;
import com.venjure.repository.CollectionRepository;
import com.venjure.service.dto.CollectionDTO;
import com.venjure.service.mapper.CollectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Collection}.
 */
@Service
@Transactional
public class CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionService.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    public CollectionService(CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
    }

    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectionDTO save(CollectionDTO collectionDTO) {
        log.debug("Request to save Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    /**
     * Partially update a collection.
     *
     * @param collectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CollectionDTO> partialUpdate(CollectionDTO collectionDTO) {
        log.debug("Request to partially update Collection : {}", collectionDTO);

        return collectionRepository
            .findById(collectionDTO.getId())
            .map(
                existingCollection -> {
                    collectionMapper.partialUpdate(existingCollection, collectionDTO);
                    return existingCollection;
                }
            )
            .map(collectionRepository::save)
            .map(collectionMapper::toDto);
    }

    /**
     * Get all the collections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Collections");
        return collectionRepository.findAll(pageable).map(collectionMapper::toDto);
    }

    /**
     * Get one collection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectionDTO> findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        return collectionRepository.findById(id).map(collectionMapper::toDto);
    }

    /**
     * Delete the collection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.deleteById(id);
    }
}
