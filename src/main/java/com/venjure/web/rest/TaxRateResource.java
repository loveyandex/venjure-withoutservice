package com.venjure.web.rest;

import com.venjure.repository.TaxRateRepository;
import com.venjure.service.TaxRateQueryService;
import com.venjure.service.TaxRateService;
import com.venjure.service.criteria.TaxRateCriteria;
import com.venjure.service.dto.TaxRateDTO;
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
 * REST controller for managing {@link com.venjure.domain.TaxRate}.
 */
@RestController
@RequestMapping("/api")
public class TaxRateResource {

    private final Logger log = LoggerFactory.getLogger(TaxRateResource.class);

    private static final String ENTITY_NAME = "taxRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxRateService taxRateService;

    private final TaxRateRepository taxRateRepository;

    private final TaxRateQueryService taxRateQueryService;

    public TaxRateResource(TaxRateService taxRateService, TaxRateRepository taxRateRepository, TaxRateQueryService taxRateQueryService) {
        this.taxRateService = taxRateService;
        this.taxRateRepository = taxRateRepository;
        this.taxRateQueryService = taxRateQueryService;
    }

    /**
     * {@code POST  /tax-rates} : Create a new taxRate.
     *
     * @param taxRateDTO the taxRateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxRateDTO, or with status {@code 400 (Bad Request)} if the taxRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-rates")
    public ResponseEntity<TaxRateDTO> createTaxRate(@Valid @RequestBody TaxRateDTO taxRateDTO) throws URISyntaxException {
        log.debug("REST request to save TaxRate : {}", taxRateDTO);
        if (taxRateDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxRateDTO result = taxRateService.save(taxRateDTO);
        return ResponseEntity
            .created(new URI("/api/tax-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-rates/:id} : Updates an existing taxRate.
     *
     * @param id the id of the taxRateDTO to save.
     * @param taxRateDTO the taxRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxRateDTO,
     * or with status {@code 400 (Bad Request)} if the taxRateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-rates/{id}")
    public ResponseEntity<TaxRateDTO> updateTaxRate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxRateDTO taxRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxRate : {}, {}", id, taxRateDTO);
        if (taxRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxRateDTO result = taxRateService.save(taxRateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxRateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tax-rates/:id} : Partial updates given fields of an existing taxRate, field will ignore if it is null
     *
     * @param id the id of the taxRateDTO to save.
     * @param taxRateDTO the taxRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxRateDTO,
     * or with status {@code 400 (Bad Request)} if the taxRateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxRateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-rates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaxRateDTO> partialUpdateTaxRate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxRateDTO taxRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxRate partially : {}, {}", id, taxRateDTO);
        if (taxRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxRateDTO> result = taxRateService.partialUpdate(taxRateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxRateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-rates} : get all the taxRates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxRates in body.
     */
    @GetMapping("/tax-rates")
    public ResponseEntity<List<TaxRateDTO>> getAllTaxRates(TaxRateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaxRates by criteria: {}", criteria);
        Page<TaxRateDTO> page = taxRateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-rates/count} : count all the taxRates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tax-rates/count")
    public ResponseEntity<Long> countTaxRates(TaxRateCriteria criteria) {
        log.debug("REST request to count TaxRates by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxRateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-rates/:id} : get the "id" taxRate.
     *
     * @param id the id of the taxRateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxRateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-rates/{id}")
    public ResponseEntity<TaxRateDTO> getTaxRate(@PathVariable Long id) {
        log.debug("REST request to get TaxRate : {}", id);
        Optional<TaxRateDTO> taxRateDTO = taxRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxRateDTO);
    }

    /**
     * {@code DELETE  /tax-rates/:id} : delete the "id" taxRate.
     *
     * @param id the id of the taxRateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-rates/{id}")
    public ResponseEntity<Void> deleteTaxRate(@PathVariable Long id) {
        log.debug("REST request to delete TaxRate : {}", id);
        taxRateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
