package com.venjure.web.rest;

import com.venjure.repository.FacetTranslationRepository;
import com.venjure.service.FacetTranslationQueryService;
import com.venjure.service.FacetTranslationService;
import com.venjure.service.criteria.FacetTranslationCriteria;
import com.venjure.service.dto.FacetTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.FacetTranslation}.
 */
@RestController
@RequestMapping("/api")
public class FacetTranslationResource {

    private final Logger log = LoggerFactory.getLogger(FacetTranslationResource.class);

    private static final String ENTITY_NAME = "facetTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacetTranslationService facetTranslationService;

    private final FacetTranslationRepository facetTranslationRepository;

    private final FacetTranslationQueryService facetTranslationQueryService;

    public FacetTranslationResource(
        FacetTranslationService facetTranslationService,
        FacetTranslationRepository facetTranslationRepository,
        FacetTranslationQueryService facetTranslationQueryService
    ) {
        this.facetTranslationService = facetTranslationService;
        this.facetTranslationRepository = facetTranslationRepository;
        this.facetTranslationQueryService = facetTranslationQueryService;
    }

    /**
     * {@code POST  /facet-translations} : Create a new facetTranslation.
     *
     * @param facetTranslationDTO the facetTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facetTranslationDTO, or with status {@code 400 (Bad Request)} if the facetTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facet-translations")
    public ResponseEntity<FacetTranslationDTO> createFacetTranslation(@Valid @RequestBody FacetTranslationDTO facetTranslationDTO)
        throws URISyntaxException {
        log.debug("REST request to save FacetTranslation : {}", facetTranslationDTO);
        if (facetTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new facetTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacetTranslationDTO result = facetTranslationService.save(facetTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/facet-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facet-translations/:id} : Updates an existing facetTranslation.
     *
     * @param id the id of the facetTranslationDTO to save.
     * @param facetTranslationDTO the facetTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the facetTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facetTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facet-translations/{id}")
    public ResponseEntity<FacetTranslationDTO> updateFacetTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacetTranslationDTO facetTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacetTranslation : {}, {}", id, facetTranslationDTO);
        if (facetTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacetTranslationDTO result = facetTranslationService.save(facetTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facet-translations/:id} : Partial updates given fields of an existing facetTranslation, field will ignore if it is null
     *
     * @param id the id of the facetTranslationDTO to save.
     * @param facetTranslationDTO the facetTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the facetTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facetTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facetTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facet-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacetTranslationDTO> partialUpdateFacetTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacetTranslationDTO facetTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacetTranslation partially : {}, {}", id, facetTranslationDTO);
        if (facetTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacetTranslationDTO> result = facetTranslationService.partialUpdate(facetTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facet-translations} : get all the facetTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facetTranslations in body.
     */
    @GetMapping("/facet-translations")
    public ResponseEntity<List<FacetTranslationDTO>> getAllFacetTranslations(FacetTranslationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FacetTranslations by criteria: {}", criteria);
        Page<FacetTranslationDTO> page = facetTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facet-translations/count} : count all the facetTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/facet-translations/count")
    public ResponseEntity<Long> countFacetTranslations(FacetTranslationCriteria criteria) {
        log.debug("REST request to count FacetTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(facetTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facet-translations/:id} : get the "id" facetTranslation.
     *
     * @param id the id of the facetTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facetTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facet-translations/{id}")
    public ResponseEntity<FacetTranslationDTO> getFacetTranslation(@PathVariable Long id) {
        log.debug("REST request to get FacetTranslation : {}", id);
        Optional<FacetTranslationDTO> facetTranslationDTO = facetTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facetTranslationDTO);
    }

    /**
     * {@code DELETE  /facet-translations/:id} : delete the "id" facetTranslation.
     *
     * @param id the id of the facetTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facet-translations/{id}")
    public ResponseEntity<Void> deleteFacetTranslation(@PathVariable Long id) {
        log.debug("REST request to delete FacetTranslation : {}", id);
        facetTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
