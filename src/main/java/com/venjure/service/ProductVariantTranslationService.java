package com.venjure.service;

import com.venjure.domain.ProductVariantTranslation;
import com.venjure.repository.ProductVariantTranslationRepository;
import com.venjure.service.dto.ProductVariantTranslationDTO;
import com.venjure.service.mapper.ProductVariantTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductVariantTranslation}.
 */
@Service
@Transactional
public class ProductVariantTranslationService {

    private final Logger log = LoggerFactory.getLogger(ProductVariantTranslationService.class);

    private final ProductVariantTranslationRepository productVariantTranslationRepository;

    private final ProductVariantTranslationMapper productVariantTranslationMapper;

    public ProductVariantTranslationService(
        ProductVariantTranslationRepository productVariantTranslationRepository,
        ProductVariantTranslationMapper productVariantTranslationMapper
    ) {
        this.productVariantTranslationRepository = productVariantTranslationRepository;
        this.productVariantTranslationMapper = productVariantTranslationMapper;
    }

    /**
     * Save a productVariantTranslation.
     *
     * @param productVariantTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductVariantTranslationDTO save(ProductVariantTranslationDTO productVariantTranslationDTO) {
        log.debug("Request to save ProductVariantTranslation : {}", productVariantTranslationDTO);
        ProductVariantTranslation productVariantTranslation = productVariantTranslationMapper.toEntity(productVariantTranslationDTO);
        productVariantTranslation = productVariantTranslationRepository.save(productVariantTranslation);
        return productVariantTranslationMapper.toDto(productVariantTranslation);
    }

    /**
     * Partially update a productVariantTranslation.
     *
     * @param productVariantTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductVariantTranslationDTO> partialUpdate(ProductVariantTranslationDTO productVariantTranslationDTO) {
        log.debug("Request to partially update ProductVariantTranslation : {}", productVariantTranslationDTO);

        return productVariantTranslationRepository
            .findById(productVariantTranslationDTO.getId())
            .map(
                existingProductVariantTranslation -> {
                    productVariantTranslationMapper.partialUpdate(existingProductVariantTranslation, productVariantTranslationDTO);
                    return existingProductVariantTranslation;
                }
            )
            .map(productVariantTranslationRepository::save)
            .map(productVariantTranslationMapper::toDto);
    }

    /**
     * Get all the productVariantTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductVariantTranslations");
        return productVariantTranslationRepository.findAll(pageable).map(productVariantTranslationMapper::toDto);
    }

    /**
     * Get one productVariantTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductVariantTranslationDTO> findOne(Long id) {
        log.debug("Request to get ProductVariantTranslation : {}", id);
        return productVariantTranslationRepository.findById(id).map(productVariantTranslationMapper::toDto);
    }

    /**
     * Delete the productVariantTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductVariantTranslation : {}", id);
        productVariantTranslationRepository.deleteById(id);
    }
}
