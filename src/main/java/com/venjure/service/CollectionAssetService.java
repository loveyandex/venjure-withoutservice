package com.venjure.service;

import com.venjure.domain.CollectionAsset;
import com.venjure.repository.CollectionAssetRepository;
import com.venjure.service.dto.CollectionAssetDTO;
import com.venjure.service.mapper.CollectionAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CollectionAsset}.
 */
@Service
@Transactional
public class CollectionAssetService {

    private final Logger log = LoggerFactory.getLogger(CollectionAssetService.class);

    private final CollectionAssetRepository collectionAssetRepository;

    private final CollectionAssetMapper collectionAssetMapper;

    public CollectionAssetService(CollectionAssetRepository collectionAssetRepository, CollectionAssetMapper collectionAssetMapper) {
        this.collectionAssetRepository = collectionAssetRepository;
        this.collectionAssetMapper = collectionAssetMapper;
    }

    /**
     * Save a collectionAsset.
     *
     * @param collectionAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectionAssetDTO save(CollectionAssetDTO collectionAssetDTO) {
        log.debug("Request to save CollectionAsset : {}", collectionAssetDTO);
        CollectionAsset collectionAsset = collectionAssetMapper.toEntity(collectionAssetDTO);
        collectionAsset = collectionAssetRepository.save(collectionAsset);
        return collectionAssetMapper.toDto(collectionAsset);
    }

    /**
     * Partially update a collectionAsset.
     *
     * @param collectionAssetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CollectionAssetDTO> partialUpdate(CollectionAssetDTO collectionAssetDTO) {
        log.debug("Request to partially update CollectionAsset : {}", collectionAssetDTO);

        return collectionAssetRepository
            .findById(collectionAssetDTO.getId())
            .map(
                existingCollectionAsset -> {
                    collectionAssetMapper.partialUpdate(existingCollectionAsset, collectionAssetDTO);
                    return existingCollectionAsset;
                }
            )
            .map(collectionAssetRepository::save)
            .map(collectionAssetMapper::toDto);
    }

    /**
     * Get all the collectionAssets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionAssetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollectionAssets");
        return collectionAssetRepository.findAll(pageable).map(collectionAssetMapper::toDto);
    }

    /**
     * Get one collectionAsset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectionAssetDTO> findOne(Long id) {
        log.debug("Request to get CollectionAsset : {}", id);
        return collectionAssetRepository.findById(id).map(collectionAssetMapper::toDto);
    }

    /**
     * Delete the collectionAsset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CollectionAsset : {}", id);
        collectionAssetRepository.deleteById(id);
    }
}
