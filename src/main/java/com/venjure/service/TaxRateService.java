package com.venjure.service;

import com.venjure.domain.TaxRate;
import com.venjure.repository.TaxRateRepository;
import com.venjure.service.dto.TaxRateDTO;
import com.venjure.service.mapper.TaxRateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaxRate}.
 */
@Service
@Transactional
public class TaxRateService {

    private final Logger log = LoggerFactory.getLogger(TaxRateService.class);

    private final TaxRateRepository taxRateRepository;

    private final TaxRateMapper taxRateMapper;

    public TaxRateService(TaxRateRepository taxRateRepository, TaxRateMapper taxRateMapper) {
        this.taxRateRepository = taxRateRepository;
        this.taxRateMapper = taxRateMapper;
    }

    /**
     * Save a taxRate.
     *
     * @param taxRateDTO the entity to save.
     * @return the persisted entity.
     */
    public TaxRateDTO save(TaxRateDTO taxRateDTO) {
        log.debug("Request to save TaxRate : {}", taxRateDTO);
        TaxRate taxRate = taxRateMapper.toEntity(taxRateDTO);
        taxRate = taxRateRepository.save(taxRate);
        return taxRateMapper.toDto(taxRate);
    }

    /**
     * Partially update a taxRate.
     *
     * @param taxRateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaxRateDTO> partialUpdate(TaxRateDTO taxRateDTO) {
        log.debug("Request to partially update TaxRate : {}", taxRateDTO);

        return taxRateRepository
            .findById(taxRateDTO.getId())
            .map(
                existingTaxRate -> {
                    taxRateMapper.partialUpdate(existingTaxRate, taxRateDTO);
                    return existingTaxRate;
                }
            )
            .map(taxRateRepository::save)
            .map(taxRateMapper::toDto);
    }

    /**
     * Get all the taxRates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxRateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxRates");
        return taxRateRepository.findAll(pageable).map(taxRateMapper::toDto);
    }

    /**
     * Get one taxRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaxRateDTO> findOne(Long id) {
        log.debug("Request to get TaxRate : {}", id);
        return taxRateRepository.findById(id).map(taxRateMapper::toDto);
    }

    /**
     * Delete the taxRate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TaxRate : {}", id);
        taxRateRepository.deleteById(id);
    }
}
