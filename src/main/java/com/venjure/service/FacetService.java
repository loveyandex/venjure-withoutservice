package com.venjure.service;

import com.venjure.domain.Facet;
import com.venjure.repository.FacetRepository;
import com.venjure.service.dto.FacetDTO;
import com.venjure.service.mapper.FacetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Facet}.
 */
@Service
@Transactional
public class FacetService {

    private final Logger log = LoggerFactory.getLogger(FacetService.class);

    private final FacetRepository facetRepository;

    private final FacetMapper facetMapper;

    public FacetService(FacetRepository facetRepository, FacetMapper facetMapper) {
        this.facetRepository = facetRepository;
        this.facetMapper = facetMapper;
    }

    /**
     * Save a facet.
     *
     * @param facetDTO the entity to save.
     * @return the persisted entity.
     */
    public FacetDTO save(FacetDTO facetDTO) {
        log.debug("Request to save Facet : {}", facetDTO);
        Facet facet = facetMapper.toEntity(facetDTO);
        facet = facetRepository.save(facet);
        return facetMapper.toDto(facet);
    }

    /**
     * Partially update a facet.
     *
     * @param facetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacetDTO> partialUpdate(FacetDTO facetDTO) {
        log.debug("Request to partially update Facet : {}", facetDTO);

        return facetRepository
            .findById(facetDTO.getId())
            .map(
                existingFacet -> {
                    facetMapper.partialUpdate(existingFacet, facetDTO);
                    return existingFacet;
                }
            )
            .map(facetRepository::save)
            .map(facetMapper::toDto);
    }

    /**
     * Get all the facets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facets");
        return facetRepository.findAll(pageable).map(facetMapper::toDto);
    }

    /**
     * Get all the facets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return facetRepository.findAllWithEagerRelationships(pageable).map(facetMapper::toDto);
    }

    /**
     * Get one facet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacetDTO> findOne(Long id) {
        log.debug("Request to get Facet : {}", id);
        return facetRepository.findOneWithEagerRelationships(id).map(facetMapper::toDto);
    }

    /**
     * Delete the facet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Facet : {}", id);
        facetRepository.deleteById(id);
    }
}
