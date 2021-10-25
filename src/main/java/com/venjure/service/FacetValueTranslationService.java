package com.venjure.service;

import com.venjure.domain.FacetValueTranslation;
import com.venjure.repository.FacetValueTranslationRepository;
import com.venjure.service.dto.FacetValueTranslationDTO;
import com.venjure.service.mapper.FacetValueTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacetValueTranslation}.
 */
@Service
@Transactional
public class FacetValueTranslationService {

    private final Logger log = LoggerFactory.getLogger(FacetValueTranslationService.class);

    private final FacetValueTranslationRepository facetValueTranslationRepository;

    private final FacetValueTranslationMapper facetValueTranslationMapper;

    public FacetValueTranslationService(
        FacetValueTranslationRepository facetValueTranslationRepository,
        FacetValueTranslationMapper facetValueTranslationMapper
    ) {
        this.facetValueTranslationRepository = facetValueTranslationRepository;
        this.facetValueTranslationMapper = facetValueTranslationMapper;
    }

    /**
     * Save a facetValueTranslation.
     *
     * @param facetValueTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public FacetValueTranslationDTO save(FacetValueTranslationDTO facetValueTranslationDTO) {
        log.debug("Request to save FacetValueTranslation : {}", facetValueTranslationDTO);
        FacetValueTranslation facetValueTranslation = facetValueTranslationMapper.toEntity(facetValueTranslationDTO);
        facetValueTranslation = facetValueTranslationRepository.save(facetValueTranslation);
        return facetValueTranslationMapper.toDto(facetValueTranslation);
    }

    /**
     * Partially update a facetValueTranslation.
     *
     * @param facetValueTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacetValueTranslationDTO> partialUpdate(FacetValueTranslationDTO facetValueTranslationDTO) {
        log.debug("Request to partially update FacetValueTranslation : {}", facetValueTranslationDTO);

        return facetValueTranslationRepository
            .findById(facetValueTranslationDTO.getId())
            .map(
                existingFacetValueTranslation -> {
                    facetValueTranslationMapper.partialUpdate(existingFacetValueTranslation, facetValueTranslationDTO);
                    return existingFacetValueTranslation;
                }
            )
            .map(facetValueTranslationRepository::save)
            .map(facetValueTranslationMapper::toDto);
    }

    /**
     * Get all the facetValueTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetValueTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FacetValueTranslations");
        return facetValueTranslationRepository.findAll(pageable).map(facetValueTranslationMapper::toDto);
    }

    /**
     * Get one facetValueTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacetValueTranslationDTO> findOne(Long id) {
        log.debug("Request to get FacetValueTranslation : {}", id);
        return facetValueTranslationRepository.findById(id).map(facetValueTranslationMapper::toDto);
    }

    /**
     * Delete the facetValueTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FacetValueTranslation : {}", id);
        facetValueTranslationRepository.deleteById(id);
    }
}
