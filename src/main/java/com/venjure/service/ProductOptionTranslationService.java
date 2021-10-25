package com.venjure.service;

import com.venjure.domain.ProductOptionTranslation;
import com.venjure.repository.ProductOptionTranslationRepository;
import com.venjure.service.dto.ProductOptionTranslationDTO;
import com.venjure.service.mapper.ProductOptionTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductOptionTranslation}.
 */
@Service
@Transactional
public class ProductOptionTranslationService {

    private final Logger log = LoggerFactory.getLogger(ProductOptionTranslationService.class);

    private final ProductOptionTranslationRepository productOptionTranslationRepository;

    private final ProductOptionTranslationMapper productOptionTranslationMapper;

    public ProductOptionTranslationService(
        ProductOptionTranslationRepository productOptionTranslationRepository,
        ProductOptionTranslationMapper productOptionTranslationMapper
    ) {
        this.productOptionTranslationRepository = productOptionTranslationRepository;
        this.productOptionTranslationMapper = productOptionTranslationMapper;
    }

    /**
     * Save a productOptionTranslation.
     *
     * @param productOptionTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductOptionTranslationDTO save(ProductOptionTranslationDTO productOptionTranslationDTO) {
        log.debug("Request to save ProductOptionTranslation : {}", productOptionTranslationDTO);
        ProductOptionTranslation productOptionTranslation = productOptionTranslationMapper.toEntity(productOptionTranslationDTO);
        productOptionTranslation = productOptionTranslationRepository.save(productOptionTranslation);
        return productOptionTranslationMapper.toDto(productOptionTranslation);
    }

    /**
     * Partially update a productOptionTranslation.
     *
     * @param productOptionTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOptionTranslationDTO> partialUpdate(ProductOptionTranslationDTO productOptionTranslationDTO) {
        log.debug("Request to partially update ProductOptionTranslation : {}", productOptionTranslationDTO);

        return productOptionTranslationRepository
            .findById(productOptionTranslationDTO.getId())
            .map(
                existingProductOptionTranslation -> {
                    productOptionTranslationMapper.partialUpdate(existingProductOptionTranslation, productOptionTranslationDTO);
                    return existingProductOptionTranslation;
                }
            )
            .map(productOptionTranslationRepository::save)
            .map(productOptionTranslationMapper::toDto);
    }

    /**
     * Get all the productOptionTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOptionTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOptionTranslations");
        return productOptionTranslationRepository.findAll(pageable).map(productOptionTranslationMapper::toDto);
    }

    /**
     * Get one productOptionTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOptionTranslationDTO> findOne(Long id) {
        log.debug("Request to get ProductOptionTranslation : {}", id);
        return productOptionTranslationRepository.findById(id).map(productOptionTranslationMapper::toDto);
    }

    /**
     * Delete the productOptionTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOptionTranslation : {}", id);
        productOptionTranslationRepository.deleteById(id);
    }
}
