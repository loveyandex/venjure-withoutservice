package com.venjure.service;

import com.venjure.domain.TaxCategory;
import com.venjure.repository.TaxCategoryRepository;
import com.venjure.service.dto.TaxCategoryDTO;
import com.venjure.service.mapper.TaxCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaxCategory}.
 */
@Service
@Transactional
public class TaxCategoryService {

    private final Logger log = LoggerFactory.getLogger(TaxCategoryService.class);

    private final TaxCategoryRepository taxCategoryRepository;

    private final TaxCategoryMapper taxCategoryMapper;

    public TaxCategoryService(TaxCategoryRepository taxCategoryRepository, TaxCategoryMapper taxCategoryMapper) {
        this.taxCategoryRepository = taxCategoryRepository;
        this.taxCategoryMapper = taxCategoryMapper;
    }

    /**
     * Save a taxCategory.
     *
     * @param taxCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public TaxCategoryDTO save(TaxCategoryDTO taxCategoryDTO) {
        log.debug("Request to save TaxCategory : {}", taxCategoryDTO);
        TaxCategory taxCategory = taxCategoryMapper.toEntity(taxCategoryDTO);
        taxCategory = taxCategoryRepository.save(taxCategory);
        return taxCategoryMapper.toDto(taxCategory);
    }

    /**
     * Partially update a taxCategory.
     *
     * @param taxCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaxCategoryDTO> partialUpdate(TaxCategoryDTO taxCategoryDTO) {
        log.debug("Request to partially update TaxCategory : {}", taxCategoryDTO);

        return taxCategoryRepository
            .findById(taxCategoryDTO.getId())
            .map(
                existingTaxCategory -> {
                    taxCategoryMapper.partialUpdate(existingTaxCategory, taxCategoryDTO);
                    return existingTaxCategory;
                }
            )
            .map(taxCategoryRepository::save)
            .map(taxCategoryMapper::toDto);
    }

    /**
     * Get all the taxCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxCategories");
        return taxCategoryRepository.findAll(pageable).map(taxCategoryMapper::toDto);
    }

    /**
     * Get one taxCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaxCategoryDTO> findOne(Long id) {
        log.debug("Request to get TaxCategory : {}", id);
        return taxCategoryRepository.findById(id).map(taxCategoryMapper::toDto);
    }

    /**
     * Delete the taxCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TaxCategory : {}", id);
        taxCategoryRepository.deleteById(id);
    }
}
