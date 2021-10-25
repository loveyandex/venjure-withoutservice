package com.venjure.web.rest;

import com.venjure.repository.FacetRepository;
import com.venjure.service.FacetQueryService;
import com.venjure.service.FacetService;
import com.venjure.service.criteria.FacetCriteria;
import com.venjure.service.dto.FacetDTO;
import com.venjure.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.venjure.domain.Facet}.
 */
@RestController
@RequestMapping("/api")
public class FacetResource {

    private final Logger log = LoggerFactory.getLogger(FacetResource.class);

    private static final String ENTITY_NAME = "facet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacetService facetService;

    private final FacetRepository facetRepository;

    private final FacetQueryService facetQueryService;

    public FacetResource(FacetService facetService, FacetRepository facetRepository, FacetQueryService facetQueryService) {
        this.facetService = facetService;
        this.facetRepository = facetRepository;
        this.facetQueryService = facetQueryService;
    }

    /**
     * {@code POST  /facets} : Create a new facet.
     *
     * @param facetDTO the facetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facetDTO, or with status {@code 400 (Bad Request)} if the facet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facets")
    public ResponseEntity<FacetDTO> createFacet(@Valid @RequestBody FacetDTO facetDTO) throws URISyntaxException {
        log.debug("REST request to save Facet : {}", facetDTO);
        if (facetDTO.getId() != null) {
            throw new BadRequestAlertException("A new facet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacetDTO result = facetService.save(facetDTO);
        return ResponseEntity
            .created(new URI("/api/facets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facets/:id} : Updates an existing facet.
     *
     * @param id the id of the facetDTO to save.
     * @param facetDTO the facetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetDTO,
     * or with status {@code 400 (Bad Request)} if the facetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facets/{id}")
    public ResponseEntity<FacetDTO> updateFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacetDTO facetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facet : {}, {}", id, facetDTO);
        if (facetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacetDTO result = facetService.save(facetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facets/:id} : Partial updates given fields of an existing facet, field will ignore if it is null
     *
     * @param id the id of the facetDTO to save.
     * @param facetDTO the facetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetDTO,
     * or with status {@code 400 (Bad Request)} if the facetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacetDTO> partialUpdateFacet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacetDTO facetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facet partially : {}, {}", id, facetDTO);
        if (facetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacetDTO> result = facetService.partialUpdate(facetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facets} : get all the facets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facets in body.
     */
    @GetMapping("/facets")
    public ResponseEntity<List<FacetDTO>> getAllFacets(FacetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Facets by criteria: {}", criteria);
        Page<FacetDTO> page = facetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facets/count} : count all the facets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/facets/count")
    public ResponseEntity<Long> countFacets(FacetCriteria criteria) {
        log.debug("REST request to count Facets by criteria: {}", criteria);
        return ResponseEntity.ok().body(facetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facets/:id} : get the "id" facet.
     *
     * @param id the id of the facetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facets/{id}")
    public ResponseEntity<FacetDTO> getFacet(@PathVariable Long id) {
        log.debug("REST request to get Facet : {}", id);
        Optional<FacetDTO> facetDTO = facetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facetDTO);
    }

    /**
     * {@code DELETE  /facets/:id} : delete the "id" facet.
     *
     * @param id the id of the facetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facets/{id}")
    public ResponseEntity<Void> deleteFacet(@PathVariable Long id) {
        log.debug("REST request to delete Facet : {}", id);
        facetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
