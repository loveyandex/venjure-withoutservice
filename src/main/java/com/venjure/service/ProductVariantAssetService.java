package com.venjure.service;

import com.venjure.domain.ProductVariantAsset;
import com.venjure.repository.ProductVariantAssetRepository;
import com.venjure.service.dto.ProductVariantAssetDTO;
import com.venjure.service.mapper.ProductVariantAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductVariantAsset}.
 */
@Service
@Transactional
public class ProductVariantAssetService {

    private final Logger log = LoggerFactory.getLogger(ProductVariantAssetService.class);

    private final ProductVariantAssetRepository productVariantAssetRepository;

    private final ProductVariantAssetMapper productVariantAssetMapper;

    public ProductVariantAssetService(
        ProductVariantAssetRepository productVariantAssetRepository,
        ProductVariantAssetMapper productVariantAssetMapper
    ) {
        this.productVariantAssetRepository = productVariantAssetRepository;
        this.productVariantAssetMapper = productVariantAssetMapper;
    }

    /**
     * Save a productVariantAsset.
     *
     * @param productVariantAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductVariantAssetDTO save(ProductVariantAssetDTO productVariantAssetDTO) {
        log.debug("Request to save ProductVariantAsset : {}", productVariantAssetDTO);
        ProductVariantAsset productVariantAsset = productVariantAssetMapper.toEntity(productVariantAssetDTO);
        productVariantAsset = productVariantAssetRepository.save(productVariantAsset);
        return productVariantAssetMapper.toDto(productVariantAsset);
    }

    /**
     * Partially update a productVariantAsset.
     *
     * @param productVariantAssetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductVariantAssetDTO> partialUpdate(ProductVariantAssetDTO productVariantAssetDTO) {
        log.debug("Request to partially update ProductVariantAsset : {}", productVariantAssetDTO);

        return productVariantAssetRepository
            .findById(productVariantAssetDTO.getId())
            .map(
                existingProductVariantAsset -> {
                    productVariantAssetMapper.partialUpdate(existingProductVariantAsset, productVariantAssetDTO);
                    return existingProductVariantAsset;
                }
            )
            .map(productVariantAssetRepository::save)
            .map(productVariantAssetMapper::toDto);
    }

    /**
     * Get all the productVariantAssets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantAssetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductVariantAssets");
        return productVariantAssetRepository.findAll(pageable).map(productVariantAssetMapper::toDto);
    }

    /**
     * Get one productVariantAsset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductVariantAssetDTO> findOne(Long id) {
        log.debug("Request to get ProductVariantAsset : {}", id);
        return productVariantAssetRepository.findById(id).map(productVariantAssetMapper::toDto);
    }

    /**
     * Delete the productVariantAsset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductVariantAsset : {}", id);
        productVariantAssetRepository.deleteById(id);
    }
}
