package com.venjure.service;

import com.venjure.domain.ProductTranslation;
import com.venjure.repository.ProductTranslationRepository;
import com.venjure.service.dto.ProductTranslationDTO;
import com.venjure.service.mapper.ProductTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductTranslation}.
 */
@Service
@Transactional
public class ProductTranslationService {

    private final Logger log = LoggerFactory.getLogger(ProductTranslationService.class);

    private final ProductTranslationRepository productTranslationRepository;

    private final ProductTranslationMapper productTranslationMapper;

    public ProductTranslationService(
        ProductTranslationRepository productTranslationRepository,
        ProductTranslationMapper productTranslationMapper
    ) {
        this.productTranslationRepository = productTranslationRepository;
        this.productTranslationMapper = productTranslationMapper;
    }

    /**
     * Save a productTranslation.
     *
     * @param productTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductTranslationDTO save(ProductTranslationDTO productTranslationDTO) {
        log.debug("Request to save ProductTranslation : {}", productTranslationDTO);
        ProductTranslation productTranslation = productTranslationMapper.toEntity(productTranslationDTO);
        productTranslation = productTranslationRepository.save(productTranslation);
        return productTranslationMapper.toDto(productTranslation);
    }

    /**
     * Partially update a productTranslation.
     *
     * @param productTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductTranslationDTO> partialUpdate(ProductTranslationDTO productTranslationDTO) {
        log.debug("Request to partially update ProductTranslation : {}", productTranslationDTO);

        return productTranslationRepository
            .findById(productTranslationDTO.getId())
            .map(
                existingProductTranslation -> {
                    productTranslationMapper.partialUpdate(existingProductTranslation, productTranslationDTO);
                    return existingProductTranslation;
                }
            )
            .map(productTranslationRepository::save)
            .map(productTranslationMapper::toDto);
    }

    /**
     * Get all the productTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductTranslations");
        return productTranslationRepository.findAll(pageable).map(productTranslationMapper::toDto);
    }

    /**
     * Get one productTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductTranslationDTO> findOne(Long id) {
        log.debug("Request to get ProductTranslation : {}", id);
        return productTranslationRepository.findById(id).map(productTranslationMapper::toDto);
    }

    /**
     * Delete the productTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductTranslation : {}", id);
        productTranslationRepository.deleteById(id);
    }
}
