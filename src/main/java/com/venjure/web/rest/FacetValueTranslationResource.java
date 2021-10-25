package com.venjure.web.rest;

import com.venjure.repository.FacetValueTranslationRepository;
import com.venjure.service.FacetValueTranslationQueryService;
import com.venjure.service.FacetValueTranslationService;
import com.venjure.service.criteria.FacetValueTranslationCriteria;
import com.venjure.service.dto.FacetValueTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.FacetValueTranslation}.
 */
@RestController
@RequestMapping("/api")
public class FacetValueTranslationResource {

    private final Logger log = LoggerFactory.getLogger(FacetValueTranslationResource.class);

    private static final String ENTITY_NAME = "facetValueTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacetValueTranslationService facetValueTranslationService;

    private final FacetValueTranslationRepository facetValueTranslationRepository;

    private final FacetValueTranslationQueryService facetValueTranslationQueryService;

    public FacetValueTranslationResource(
        FacetValueTranslationService facetValueTranslationService,
        FacetValueTranslationRepository facetValueTranslationRepository,
        FacetValueTranslationQueryService facetValueTranslationQueryService
    ) {
        this.facetValueTranslationService = facetValueTranslationService;
        this.facetValueTranslationRepository = facetValueTranslationRepository;
        this.facetValueTranslationQueryService = facetValueTranslationQueryService;
    }

    /**
     * {@code POST  /facet-value-translations} : Create a new facetValueTranslation.
     *
     * @param facetValueTranslationDTO the facetValueTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facetValueTranslationDTO, or with status {@code 400 (Bad Request)} if the facetValueTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facet-value-translations")
    public ResponseEntity<FacetValueTranslationDTO> createFacetValueTranslation(
        @Valid @RequestBody FacetValueTranslationDTO facetValueTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FacetValueTranslation : {}", facetValueTranslationDTO);
        if (facetValueTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new facetValueTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacetValueTranslationDTO result = facetValueTranslationService.save(facetValueTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/facet-value-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facet-value-translations/:id} : Updates an existing facetValueTranslation.
     *
     * @param id the id of the facetValueTranslationDTO to save.
     * @param facetValueTranslationDTO the facetValueTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetValueTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the facetValueTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facetValueTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facet-value-translations/{id}")
    public ResponseEntity<FacetValueTranslationDTO> updateFacetValueTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacetValueTranslationDTO facetValueTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FacetValueTranslation : {}, {}", id, facetValueTranslationDTO);
        if (facetValueTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetValueTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetValueTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacetValueTranslationDTO result = facetValueTranslationService.save(facetValueTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetValueTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facet-value-translations/:id} : Partial updates given fields of an existing facetValueTranslation, field will ignore if it is null
     *
     * @param id the id of the facetValueTranslationDTO to save.
     * @param facetValueTranslationDTO the facetValueTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facetValueTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the facetValueTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facetValueTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facetValueTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facet-value-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FacetValueTranslationDTO> partialUpdateFacetValueTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacetValueTranslationDTO facetValueTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacetValueTranslation partially : {}, {}", id, facetValueTranslationDTO);
        if (facetValueTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facetValueTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facetValueTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacetValueTranslationDTO> result = facetValueTranslationService.partialUpdate(facetValueTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facetValueTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facet-value-translations} : get all the facetValueTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facetValueTranslations in body.
     */
    @GetMapping("/facet-value-translations")
    public ResponseEntity<List<FacetValueTranslationDTO>> getAllFacetValueTranslations(
        FacetValueTranslationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FacetValueTranslations by criteria: {}", criteria);
        Page<FacetValueTranslationDTO> page = facetValueTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facet-value-translations/count} : count all the facetValueTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/facet-value-translations/count")
    public ResponseEntity<Long> countFacetValueTranslations(FacetValueTranslationCriteria criteria) {
        log.debug("REST request to count FacetValueTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(facetValueTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facet-value-translations/:id} : get the "id" facetValueTranslation.
     *
     * @param id the id of the facetValueTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facetValueTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facet-value-translations/{id}")
    public ResponseEntity<FacetValueTranslationDTO> getFacetValueTranslation(@PathVariable Long id) {
        log.debug("REST request to get FacetValueTranslation : {}", id);
        Optional<FacetValueTranslationDTO> facetValueTranslationDTO = facetValueTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facetValueTranslationDTO);
    }

    /**
     * {@code DELETE  /facet-value-translations/:id} : delete the "id" facetValueTranslation.
     *
     * @param id the id of the facetValueTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facet-value-translations/{id}")
    public ResponseEntity<Void> deleteFacetValueTranslation(@PathVariable Long id) {
        log.debug("REST request to delete FacetValueTranslation : {}", id);
        facetValueTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
