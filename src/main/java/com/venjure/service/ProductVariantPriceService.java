package com.venjure.service;

import com.venjure.domain.ProductVariantPrice;
import com.venjure.repository.ProductVariantPriceRepository;
import com.venjure.service.dto.ProductVariantPriceDTO;
import com.venjure.service.mapper.ProductVariantPriceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductVariantPrice}.
 */
@Service
@Transactional
public class ProductVariantPriceService {

    private final Logger log = LoggerFactory.getLogger(ProductVariantPriceService.class);

    private final ProductVariantPriceRepository productVariantPriceRepository;

    private final ProductVariantPriceMapper productVariantPriceMapper;

    public ProductVariantPriceService(
        ProductVariantPriceRepository productVariantPriceRepository,
        ProductVariantPriceMapper productVariantPriceMapper
    ) {
        this.productVariantPriceRepository = productVariantPriceRepository;
        this.productVariantPriceMapper = productVariantPriceMapper;
    }

    /**
     * Save a productVariantPrice.
     *
     * @param productVariantPriceDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductVariantPriceDTO save(ProductVariantPriceDTO productVariantPriceDTO) {
        log.debug("Request to save ProductVariantPrice : {}", productVariantPriceDTO);
        ProductVariantPrice productVariantPrice = productVariantPriceMapper.toEntity(productVariantPriceDTO);
        productVariantPrice = productVariantPriceRepository.save(productVariantPrice);
        return productVariantPriceMapper.toDto(productVariantPrice);
    }

    /**
     * Partially update a productVariantPrice.
     *
     * @param productVariantPriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductVariantPriceDTO> partialUpdate(ProductVariantPriceDTO productVariantPriceDTO) {
        log.debug("Request to partially update ProductVariantPrice : {}", productVariantPriceDTO);

        return productVariantPriceRepository
            .findById(productVariantPriceDTO.getId())
            .map(
                existingProductVariantPrice -> {
                    productVariantPriceMapper.partialUpdate(existingProductVariantPrice, productVariantPriceDTO);
                    return existingProductVariantPrice;
                }
            )
            .map(productVariantPriceRepository::save)
            .map(productVariantPriceMapper::toDto);
    }

    /**
     * Get all the productVariantPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantPriceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductVariantPrices");
        return productVariantPriceRepository.findAll(pageable).map(productVariantPriceMapper::toDto);
    }

    /**
     * Get one productVariantPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductVariantPriceDTO> findOne(Long id) {
        log.debug("Request to get ProductVariantPrice : {}", id);
        return productVariantPriceRepository.findById(id).map(productVariantPriceMapper::toDto);
    }

    /**
     * Delete the productVariantPrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductVariantPrice : {}", id);
        productVariantPriceRepository.deleteById(id);
    }
}
