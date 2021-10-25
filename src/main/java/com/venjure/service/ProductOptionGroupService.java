package com.venjure.service;

import com.venjure.domain.ProductOptionGroup;
import com.venjure.repository.ProductOptionGroupRepository;
import com.venjure.service.dto.ProductOptionGroupDTO;
import com.venjure.service.mapper.ProductOptionGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductOptionGroup}.
 */
@Service
@Transactional
public class ProductOptionGroupService {

    private final Logger log = LoggerFactory.getLogger(ProductOptionGroupService.class);

    private final ProductOptionGroupRepository productOptionGroupRepository;

    private final ProductOptionGroupMapper productOptionGroupMapper;

    public ProductOptionGroupService(
        ProductOptionGroupRepository productOptionGroupRepository,
        ProductOptionGroupMapper productOptionGroupMapper
    ) {
        this.productOptionGroupRepository = productOptionGroupRepository;
        this.productOptionGroupMapper = productOptionGroupMapper;
    }

    /**
     * Save a productOptionGroup.
     *
     * @param productOptionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductOptionGroupDTO save(ProductOptionGroupDTO productOptionGroupDTO) {
        log.debug("Request to save ProductOptionGroup : {}", productOptionGroupDTO);
        ProductOptionGroup productOptionGroup = productOptionGroupMapper.toEntity(productOptionGroupDTO);
        productOptionGroup = productOptionGroupRepository.save(productOptionGroup);
        return productOptionGroupMapper.toDto(productOptionGroup);
    }

    /**
     * Partially update a productOptionGroup.
     *
     * @param productOptionGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOptionGroupDTO> partialUpdate(ProductOptionGroupDTO productOptionGroupDTO) {
        log.debug("Request to partially update ProductOptionGroup : {}", productOptionGroupDTO);

        return productOptionGroupRepository
            .findById(productOptionGroupDTO.getId())
            .map(
                existingProductOptionGroup -> {
                    productOptionGroupMapper.partialUpdate(existingProductOptionGroup, productOptionGroupDTO);
                    return existingProductOptionGroup;
                }
            )
            .map(productOptionGroupRepository::save)
            .map(productOptionGroupMapper::toDto);
    }

    /**
     * Get all the productOptionGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOptionGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOptionGroups");
        return productOptionGroupRepository.findAll(pageable).map(productOptionGroupMapper::toDto);
    }

    /**
     * Get one productOptionGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOptionGroupDTO> findOne(Long id) {
        log.debug("Request to get ProductOptionGroup : {}", id);
        return productOptionGroupRepository.findById(id).map(productOptionGroupMapper::toDto);
    }

    /**
     * Delete the productOptionGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOptionGroup : {}", id);
        productOptionGroupRepository.deleteById(id);
    }
}
