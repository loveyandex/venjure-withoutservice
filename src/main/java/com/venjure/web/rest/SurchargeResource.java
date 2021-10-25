package com.venjure.web.rest;

import com.venjure.repository.SurchargeRepository;
import com.venjure.service.SurchargeQueryService;
import com.venjure.service.SurchargeService;
import com.venjure.service.criteria.SurchargeCriteria;
import com.venjure.service.dto.SurchargeDTO;
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
 * REST controller for managing {@link com.venjure.domain.Surcharge}.
 */
@RestController
@RequestMapping("/api")
public class SurchargeResource {

    private final Logger log = LoggerFactory.getLogger(SurchargeResource.class);

    private static final String ENTITY_NAME = "surcharge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurchargeService surchargeService;

    private final SurchargeRepository surchargeRepository;

    private final SurchargeQueryService surchargeQueryService;

    public SurchargeResource(
        SurchargeService surchargeService,
        SurchargeRepository surchargeRepository,
        SurchargeQueryService surchargeQueryService
    ) {
        this.surchargeService = surchargeService;
        this.surchargeRepository = surchargeRepository;
        this.surchargeQueryService = surchargeQueryService;
    }

    /**
     * {@code POST  /surcharges} : Create a new surcharge.
     *
     * @param surchargeDTO the surchargeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surchargeDTO, or with status {@code 400 (Bad Request)} if the surcharge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/surcharges")
    public ResponseEntity<SurchargeDTO> createSurcharge(@Valid @RequestBody SurchargeDTO surchargeDTO) throws URISyntaxException {
        log.debug("REST request to save Surcharge : {}", surchargeDTO);
        if (surchargeDTO.getId() != null) {
            throw new BadRequestAlertException("A new surcharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurchargeDTO result = surchargeService.save(surchargeDTO);
        return ResponseEntity
            .created(new URI("/api/surcharges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /surcharges/:id} : Updates an existing surcharge.
     *
     * @param id the id of the surchargeDTO to save.
     * @param surchargeDTO the surchargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surchargeDTO,
     * or with status {@code 400 (Bad Request)} if the surchargeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surchargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/surcharges/{id}")
    public ResponseEntity<SurchargeDTO> updateSurcharge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SurchargeDTO surchargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Surcharge : {}, {}", id, surchargeDTO);
        if (surchargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surchargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surchargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SurchargeDTO result = surchargeService.save(surchargeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surchargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /surcharges/:id} : Partial updates given fields of an existing surcharge, field will ignore if it is null
     *
     * @param id the id of the surchargeDTO to save.
     * @param surchargeDTO the surchargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surchargeDTO,
     * or with status {@code 400 (Bad Request)} if the surchargeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the surchargeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the surchargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/surcharges/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SurchargeDTO> partialUpdateSurcharge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SurchargeDTO surchargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Surcharge partially : {}, {}", id, surchargeDTO);
        if (surchargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surchargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surchargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurchargeDTO> result = surchargeService.partialUpdate(surchargeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surchargeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /surcharges} : get all the surcharges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surcharges in body.
     */
    @GetMapping("/surcharges")
    public ResponseEntity<List<SurchargeDTO>> getAllSurcharges(SurchargeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Surcharges by criteria: {}", criteria);
        Page<SurchargeDTO> page = surchargeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surcharges/count} : count all the surcharges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/surcharges/count")
    public ResponseEntity<Long> countSurcharges(SurchargeCriteria criteria) {
        log.debug("REST request to count Surcharges by criteria: {}", criteria);
        return ResponseEntity.ok().body(surchargeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /surcharges/:id} : get the "id" surcharge.
     *
     * @param id the id of the surchargeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surchargeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/surcharges/{id}")
    public ResponseEntity<SurchargeDTO> getSurcharge(@PathVariable Long id) {
        log.debug("REST request to get Surcharge : {}", id);
        Optional<SurchargeDTO> surchargeDTO = surchargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surchargeDTO);
    }

    /**
     * {@code DELETE  /surcharges/:id} : delete the "id" surcharge.
     *
     * @param id the id of the surchargeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/surcharges/{id}")
    public ResponseEntity<Void> deleteSurcharge(@PathVariable Long id) {
        log.debug("REST request to delete Surcharge : {}", id);
        surchargeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
