package com.venjure.web.rest;

import com.venjure.repository.FacetValueRepository;
import com.venjure.service.FacetValueQueryService;
import com.venjure.service.FacetValueService;
import com.venjure.service.criteria.FacetValueCriteria;
import com.venjure.service.dto.FacetValueDTO;
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
 * REST controller for managing {@link com.venjure.domain.FacetValue}.
 */
@RestController
@RequestMapping("/api")
public class FacetValueResource {

    private final Logger log = LoggerFactory.getLogger(FacetValueResource.class);

    private static final String ENTITY_NAME = "facetValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacetValueService facetValueService;

    private final FacetValueRepository facetValueRepository;

    private final FacetValueQueryService facetValueQueryService;

    public FacetValueResource(
        FacetValueService facetValueService,
        FacetValueRepository facetValueRepository,
        FacetValueQueryService facetValueQueryService
    ) {
        this.facetValueService = facetValueService;
        this.facetValueRepository = facetValueRepository;
        this.facetValueQueryService = facetValueQueryService;
    }

    /**
     * {@code POST  /facet-values} : Create a new facetValue.
     *
     * @param facetValueDTO the facetValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facetValueDTO, or with status {@code 400 (Bad Request)} if the facetValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facet-values")
    public ResponseEntity<FacetValueDTO> createFacetValue(@Valid @RequestBody FacetValueDTO facetValueDTO) throws URISyntaxException {
        log.debug("REST request to save FacetValue : {}", facetValueDTO);
        if (facetValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new facetValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacetValueDTO result = facetValueService.save(facetValueDTO);
        return ResponseEntity
            .created(new URI("/api/facet-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facet-values/:id} : Updates an existing facetValue.
     *
     * @param id the id of the facetValueDTO to save.
     * @param facetValueDTO the facetValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetValueDTO,
     * or with status {@code 400 (Bad Request)} if the facetValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facetValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facet-values/{id}")
    public ResponseEntity<FacetValueDTO> updateFacetValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacetValueDTO facetValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacetValue : {}, {}", id, facetValueDTO);
        if (facetValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacetValueDTO result = facetValueService.save(facetValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facet-values/:id} : Partial updates given fields of an existing facetValue, field will ignore if it is null
     *
     * @param id the id of the facetValueDTO to save.
     * @param facetValueDTO the facetValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetValueDTO,
     * or with status {@code 400 (Bad Request)} if the facetValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facetValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facetValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facet-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacetValueDTO> partialUpdateFacetValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacetValueDTO facetValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacetValue partially : {}, {}", id, facetValueDTO);
        if (facetValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacetValueDTO> result = facetValueService.partialUpdate(facetValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facet-values} : get all the facetValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facetValues in body.
     */
    @GetMapping("/facet-values")
    public ResponseEntity<List<FacetValueDTO>> getAllFacetValues(FacetValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FacetValues by criteria: {}", criteria);
        Page<FacetValueDTO> page = facetValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facet-values/count} : count all the facetValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/facet-values/count")
    public ResponseEntity<Long> countFacetValues(FacetValueCriteria criteria) {
        log.debug("REST request to count FacetValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(facetValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facet-values/:id} : get the "id" facetValue.
     *
     * @param id the id of the facetValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facetValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facet-values/{id}")
    public ResponseEntity<FacetValueDTO> getFacetValue(@PathVariable Long id) {
        log.debug("REST request to get FacetValue : {}", id);
        Optional<FacetValueDTO> facetValueDTO = facetValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facetValueDTO);
    }

    /**
     * {@code DELETE  /facet-values/:id} : delete the "id" facetValue.
     *
     * @param id the id of the facetValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facet-values/{id}")
    public ResponseEntity<Void> deleteFacetValue(@PathVariable Long id) {
        log.debug("REST request to delete FacetValue : {}", id);
        facetValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
