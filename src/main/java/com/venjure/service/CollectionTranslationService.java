package com.venjure.service;

import com.venjure.domain.CollectionTranslation;
import com.venjure.repository.CollectionTranslationRepository;
import com.venjure.service.dto.CollectionTranslationDTO;
import com.venjure.service.mapper.CollectionTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CollectionTranslation}.
 */
@Service
@Transactional
public class CollectionTranslationService {

    private final Logger log = LoggerFactory.getLogger(CollectionTranslationService.class);

    private final CollectionTranslationRepository collectionTranslationRepository;

    private final CollectionTranslationMapper collectionTranslationMapper;

    public CollectionTranslationService(
        CollectionTranslationRepository collectionTranslationRepository,
        CollectionTranslationMapper collectionTranslationMapper
    ) {
        this.collectionTranslationRepository = collectionTranslationRepository;
        this.collectionTranslationMapper = collectionTranslationMapper;
    }

    /**
     * Save a collectionTranslation.
     *
     * @param collectionTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectionTranslationDTO save(CollectionTranslationDTO collectionTranslationDTO) {
        log.debug("Request to save CollectionTranslation : {}", collectionTranslationDTO);
        CollectionTranslation collectionTranslation = collectionTranslationMapper.toEntity(collectionTranslationDTO);
        collectionTranslation = collectionTranslationRepository.save(collectionTranslation);
        return collectionTranslationMapper.toDto(collectionTranslation);
    }

    /**
     * Partially update a collectionTranslation.
     *
     * @param collectionTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CollectionTranslationDTO> partialUpdate(CollectionTranslationDTO collectionTranslationDTO) {
        log.debug("Request to partially update CollectionTranslation : {}", collectionTranslationDTO);

        return collectionTranslationRepository
            .findById(collectionTranslationDTO.getId())
            .map(
                existingCollectionTranslation -> {
                    collectionTranslationMapper.partialUpdate(existingCollectionTranslation, collectionTranslationDTO);
                    return existingCollectionTranslation;
                }
            )
            .map(collectionTranslationRepository::save)
            .map(collectionTranslationMapper::toDto);
    }

    /**
     * Get all the collectionTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollectionTranslations");
        return collectionTranslationRepository.findAll(pageable).map(collectionTranslationMapper::toDto);
    }

    /**
     * Get one collectionTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectionTranslationDTO> findOne(Long id) {
        log.debug("Request to get CollectionTranslation : {}", id);
        return collectionTranslationRepository.findById(id).map(collectionTranslationMapper::toDto);
    }

    /**
     * Delete the collectionTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CollectionTranslation : {}", id);
        collectionTranslationRepository.deleteById(id);
    }
}
