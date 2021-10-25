package com.venjure.service;

import com.venjure.domain.FacetTranslation;
import com.venjure.repository.FacetTranslationRepository;
import com.venjure.service.dto.FacetTranslationDTO;
import com.venjure.service.mapper.FacetTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacetTranslation}.
 */
@Service
@Transactional
public class FacetTranslationService {

    private final Logger log = LoggerFactory.getLogger(FacetTranslationService.class);

    private final FacetTranslationRepository facetTranslationRepository;

    private final FacetTranslationMapper facetTranslationMapper;

    public FacetTranslationService(FacetTranslationRepository facetTranslationRepository, FacetTranslationMapper facetTranslationMapper) {
        this.facetTranslationRepository = facetTranslationRepository;
        this.facetTranslationMapper = facetTranslationMapper;
    }

    /**
     * Save a facetTranslation.
     *
     * @param facetTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public FacetTranslationDTO save(FacetTranslationDTO facetTranslationDTO) {
        log.debug("Request to save FacetTranslation : {}", facetTranslationDTO);
        FacetTranslation facetTranslation = facetTranslationMapper.toEntity(facetTranslationDTO);
        facetTranslation = facetTranslationRepository.save(facetTranslation);
        return facetTranslationMapper.toDto(facetTranslation);
    }

    /**
     * Partially update a facetTranslation.
     *
     * @param facetTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacetTranslationDTO> partialUpdate(FacetTranslationDTO facetTranslationDTO) {
        log.debug("Request to partially update FacetTranslation : {}", facetTranslationDTO);

        return facetTranslationRepository
            .findById(facetTranslationDTO.getId())
            .map(
                existingFacetTranslation -> {
                    facetTranslationMapper.partialUpdate(existingFacetTranslation, facetTranslationDTO);
                    return existingFacetTranslation;
                }
            )
            .map(facetTranslationRepository::save)
            .map(facetTranslationMapper::toDto);
    }

    /**
     * Get all the facetTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetTranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FacetTranslations");
        return facetTranslationRepository.findAll(pageable).map(facetTranslationMapper::toDto);
    }

    /**
     * Get one facetTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacetTranslationDTO> findOne(Long id) {
        log.debug("Request to get FacetTranslation : {}", id);
        return facetTranslationRepository.findById(id).map(facetTranslationMapper::toDto);
    }

    /**
     * Delete the facetTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FacetTranslation : {}", id);
        facetTranslationRepository.deleteById(id);
    }
}
