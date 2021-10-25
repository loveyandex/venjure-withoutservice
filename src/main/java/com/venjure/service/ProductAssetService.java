package com.venjure.service;

import com.venjure.domain.ProductAsset;
import com.venjure.repository.ProductAssetRepository;
import com.venjure.service.dto.ProductAssetDTO;
import com.venjure.service.mapper.ProductAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductAsset}.
 */
@Service
@Transactional
public class ProductAssetService {

    private final Logger log = LoggerFactory.getLogger(ProductAssetService.class);

    private final ProductAssetRepository productAssetRepository;

    private final ProductAssetMapper productAssetMapper;

    public ProductAssetService(ProductAssetRepository productAssetRepository, ProductAssetMapper productAssetMapper) {
        this.productAssetRepository = productAssetRepository;
        this.productAssetMapper = productAssetMapper;
    }

    /**
     * Save a productAsset.
     *
     * @param productAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductAssetDTO save(ProductAssetDTO productAssetDTO) {
        log.debug("Request to save ProductAsset : {}", productAssetDTO);
        ProductAsset productAsset = productAssetMapper.toEntity(productAssetDTO);
        productAsset = productAssetRepository.save(productAsset);
        return productAssetMapper.toDto(productAsset);
    }

    /**
     * Partially update a productAsset.
     *
     * @param productAssetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAssetDTO> partialUpdate(ProductAssetDTO productAssetDTO) {
        log.debug("Request to partially update ProductAsset : {}", productAssetDTO);

        return productAssetRepository
            .findById(productAssetDTO.getId())
            .map(
                existingProductAsset -> {
                    productAssetMapper.partialUpdate(existingProductAsset, productAssetDTO);
                    return existingProductAsset;
                }
            )
            .map(productAssetRepository::save)
            .map(productAssetMapper::toDto);
    }

    /**
     * Get all the productAssets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductAssetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductAssets");
        return productAssetRepository.findAll(pageable).map(productAssetMapper::toDto);
    }

    /**
     * Get one productAsset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAssetDTO> findOne(Long id) {
        log.debug("Request to get ProductAsset : {}", id);
        return productAssetRepository.findById(id).map(productAssetMapper::toDto);
    }

    /**
     * Delete the productAsset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductAsset : {}", id);
        productAssetRepository.deleteById(id);
    }
}
