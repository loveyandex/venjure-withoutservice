package com.venjure.service;

import com.venjure.domain.ProductVariant;
import com.venjure.repository.ProductVariantRepository;
import com.venjure.service.dto.ProductVariantDTO;
import com.venjure.service.mapper.ProductVariantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductVariant}.
 */
@Service
@Transactional
public class ProductVariantService {

    private final Logger log = LoggerFactory.getLogger(ProductVariantService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    /**
     * Save a productVariant.
     *
     * @param productVariantDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductVariantDTO save(ProductVariantDTO productVariantDTO) {
        log.debug("Request to save ProductVariant : {}", productVariantDTO);
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.toDto(productVariant);
    }

    /**
     * Partially update a productVariant.
     *
     * @param productVariantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductVariantDTO> partialUpdate(ProductVariantDTO productVariantDTO) {
        log.debug("Request to partially update ProductVariant : {}", productVariantDTO);

        return productVariantRepository
            .findById(productVariantDTO.getId())
            .map(
                existingProductVariant -> {
                    productVariantMapper.partialUpdate(existingProductVariant, productVariantDTO);
                    return existingProductVariant;
                }
            )
            .map(productVariantRepository::save)
            .map(productVariantMapper::toDto);
    }

    /**
     * Get all the productVariants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductVariants");
        return productVariantRepository.findAll(pageable).map(productVariantMapper::toDto);
    }

    /**
     * Get all the productVariants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductVariantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productVariantRepository.findAllWithEagerRelationships(pageable).map(productVariantMapper::toDto);
    }

    /**
     * Get one productVariant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductVariantDTO> findOne(Long id) {
        log.debug("Request to get ProductVariant : {}", id);
        return productVariantRepository.findOneWithEagerRelationships(id).map(productVariantMapper::toDto);
    }

    /**
     * Delete the productVariant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductVariant : {}", id);
        productVariantRepository.deleteById(id);
    }
}
