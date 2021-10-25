package com.venjure.service;

import com.venjure.domain.FacetValue;
import com.venjure.repository.FacetValueRepository;
import com.venjure.service.dto.FacetValueDTO;
import com.venjure.service.mapper.FacetValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacetValue}.
 */
@Service
@Transactional
public class FacetValueService {

    private final Logger log = LoggerFactory.getLogger(FacetValueService.class);

    private final FacetValueRepository facetValueRepository;

    private final FacetValueMapper facetValueMapper;

    public FacetValueService(FacetValueRepository facetValueRepository, FacetValueMapper facetValueMapper) {
        this.facetValueRepository = facetValueRepository;
        this.facetValueMapper = facetValueMapper;
    }

    /**
     * Save a facetValue.
     *
     * @param facetValueDTO the entity to save.
     * @return the persisted entity.
     */
    public FacetValueDTO save(FacetValueDTO facetValueDTO) {
        log.debug("Request to save FacetValue : {}", facetValueDTO);
        FacetValue facetValue = facetValueMapper.toEntity(facetValueDTO);
        facetValue = facetValueRepository.save(facetValue);
        return facetValueMapper.toDto(facetValue);
    }

    /**
     * Partially update a facetValue.
     *
     * @param facetValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacetValueDTO> partialUpdate(FacetValueDTO facetValueDTO) {
        log.debug("Request to partially update FacetValue : {}", facetValueDTO);

        return facetValueRepository
            .findById(facetValueDTO.getId())
            .map(
                existingFacetValue -> {
                    facetValueMapper.partialUpdate(existingFacetValue, facetValueDTO);
                    return existingFacetValue;
                }
            )
            .map(facetValueRepository::save)
            .map(facetValueMapper::toDto);
    }

    /**
     * Get all the facetValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FacetValues");
        return facetValueRepository.findAll(pageable).map(facetValueMapper::toDto);
    }

    /**
     * Get all the facetValues with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacetValueDTO> findAllWithEagerRelationships(Pageable pageable) {
        return facetValueRepository.findAllWithEagerRelationships(pageable).map(facetValueMapper::toDto);
    }

    /**
     * Get one facetValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacetValueDTO> findOne(Long id) {
        log.debug("Request to get FacetValue : {}", id);
        return facetValueRepository.findOneWithEagerRelationships(id).map(facetValueMapper::toDto);
    }

    /**
     * Delete the facetValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FacetValue : {}", id);
        facetValueRepository.deleteById(id);
    }
}
