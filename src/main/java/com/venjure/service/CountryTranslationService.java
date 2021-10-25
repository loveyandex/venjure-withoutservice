package com.venjure.service;

import com.venjure.domain.CountryTranslation;
import com.venjure.repository.CountryTranslationRepository;
import com.venjure.service.dto.CountryTranslationDTO;
import com.venjure.service.mapper.CountryTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountryTranslation}.
 */
@Service
@Transactional
public class CountryTranslationService {

    private final Logger log = LoggerFactory.getLogger(CountryTranslationService.class);

    private final CountryTranslationRepository countryTranslationRepository;

    private final CountryTranslationMapper countryTranslationMapper;

    public CountryTranslationService(
        CountryTranslationRepository countryTranslationRepository,
        CountryTranslationMapper countryTranslationMapper
    ) {
        this.countryTranslationRepository = countryTranslationRepository;
        this.countryTranslationMapper = countryTranslationMapper;
    }

    /**
     * Save a countryTranslation.
     *
     * @param countryTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryTranslationDTO save(CountryTranslationDTO countryTranslationDTO) {
        log.debug("Request to save CountryTranslation : {}", countryTranslationDTO);
        CountryTranslation countryTranslation = countryTranslationMapper.toEntity(countryTranslationDTO);
        countryTranslation = countryTranslationRepository.save(countryTranslation);
        return countryTranslationMapper.toDto(countryTranslation);
    }

    /**
     * Partially update a countryTranslation.
     *
     * @param countryTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountryTranslationDTO> partialUpdate(CountryTranslationDTO countryTranslationDTO) {
        log.debug("Request to partially update CountryTranslation : {}", countryTranslationDTO);

        return countryTranslationRepository
            .findById(countryTranslationDTO.getId())
            .map(
                existingCountryTranslation -> {
                    countryTranslationMapper.partialUpdate(existingCountryTranslation, countryTranslationDTO);
                    return existingCountryTranslation;
                }
            )
            .map(countryTranslationRepository::save)
            .map(countryTranslationMapper::toDto);
    }

    /**
     * Get all the countryTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CountryTranslations");
        return countryTranslationRepository.findAll(pageable).map(countryTranslationMapper::toDto);
    }

    /**
     * Get one countryTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountryTranslationDTO> findOne(Long id) {
        log.debug("Request to get CountryTranslation : {}", id);
        return countryTranslationRepository.findById(id).map(countryTranslationMapper::toDto);
    }

    /**
     * Delete the countryTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountryTranslation : {}", id);
        countryTranslationRepository.deleteById(id);
    }
}
