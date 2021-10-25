package com.venjure.web.rest;

import com.venjure.repository.TaxCategoryRepository;
import com.venjure.service.TaxCategoryQueryService;
import com.venjure.service.TaxCategoryService;
import com.venjure.service.criteria.TaxCategoryCriteria;
import com.venjure.service.dto.TaxCategoryDTO;
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
 * REST controller for managing {@link com.venjure.domain.TaxCategory}.
 */
@RestController
@RequestMapping("/api")
public class TaxCategoryResource {

    private final Logger log = LoggerFactory.getLogger(TaxCategoryResource.class);

    private static final String ENTITY_NAME = "taxCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxCategoryService taxCategoryService;

    private final TaxCategoryRepository taxCategoryRepository;

    private final TaxCategoryQueryService taxCategoryQueryService;

    public TaxCategoryResource(
        TaxCategoryService taxCategoryService,
        TaxCategoryRepository taxCategoryRepository,
        TaxCategoryQueryService taxCategoryQueryService
    ) {
        this.taxCategoryService = taxCategoryService;
        this.taxCategoryRepository = taxCategoryRepository;
        this.taxCategoryQueryService = taxCategoryQueryService;
    }

    /**
     * {@code POST  /tax-categories} : Create a new taxCategory.
     *
     * @param taxCategoryDTO the taxCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxCategoryDTO, or with status {@code 400 (Bad Request)} if the taxCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-categories")
    public ResponseEntity<TaxCategoryDTO> createTaxCategory(@Valid @RequestBody TaxCategoryDTO taxCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save TaxCategory : {}", taxCategoryDTO);
        if (taxCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxCategoryDTO result = taxCategoryService.save(taxCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/tax-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-categories/:id} : Updates an existing taxCategory.
     *
     * @param id the id of the taxCategoryDTO to save.
     * @param taxCategoryDTO the taxCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the taxCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-categories/{id}")
    public ResponseEntity<TaxCategoryDTO> updateTaxCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxCategoryDTO taxCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxCategory : {}, {}", id, taxCategoryDTO);
        if (taxCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxCategoryDTO result = taxCategoryService.save(taxCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tax-categories/:id} : Partial updates given fields of an existing taxCategory, field will ignore if it is null
     *
     * @param id the id of the taxCategoryDTO to save.
     * @param taxCategoryDTO the taxCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the taxCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaxCategoryDTO> partialUpdateTaxCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxCategoryDTO taxCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxCategory partially : {}, {}", id, taxCategoryDTO);
        if (taxCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxCategoryDTO> result = taxCategoryService.partialUpdate(taxCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-categories} : get all the taxCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxCategories in body.
     */
    @GetMapping("/tax-categories")
    public ResponseEntity<List<TaxCategoryDTO>> getAllTaxCategories(TaxCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaxCategories by criteria: {}", criteria);
        Page<TaxCategoryDTO> page = taxCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-categories/count} : count all the taxCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tax-categories/count")
    public ResponseEntity<Long> countTaxCategories(TaxCategoryCriteria criteria) {
        log.debug("REST request to count TaxCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-categories/:id} : get the "id" taxCategory.
     *
     * @param id the id of the taxCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-categories/{id}")
    public ResponseEntity<TaxCategoryDTO> getTaxCategory(@PathVariable Long id) {
        log.debug("REST request to get TaxCategory : {}", id);
        Optional<TaxCategoryDTO> taxCategoryDTO = taxCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxCategoryDTO);
    }

    /**
     * {@code DELETE  /tax-categories/:id} : delete the "id" taxCategory.
     *
     * @param id the id of the taxCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-categories/{id}")
    public ResponseEntity<Void> deleteTaxCategory(@PathVariable Long id) {
        log.debug("REST request to delete TaxCategory : {}", id);
        taxCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
