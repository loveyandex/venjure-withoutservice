package com.venjure.web.rest;

import com.venjure.repository.FulfillmentRepository;
import com.venjure.service.FulfillmentQueryService;
import com.venjure.service.FulfillmentService;
import com.venjure.service.criteria.FulfillmentCriteria;
import com.venjure.service.dto.FulfillmentDTO;
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
 * REST controller for managing {@link com.venjure.domain.Fulfillment}.
 */
@RestController
@RequestMapping("/api")
public class FulfillmentResource {

    private final Logger log = LoggerFactory.getLogger(FulfillmentResource.class);

    private static final String ENTITY_NAME = "fulfillment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FulfillmentService fulfillmentService;

    private final FulfillmentRepository fulfillmentRepository;

    private final FulfillmentQueryService fulfillmentQueryService;

    public FulfillmentResource(
        FulfillmentService fulfillmentService,
        FulfillmentRepository fulfillmentRepository,
        FulfillmentQueryService fulfillmentQueryService
    ) {
        this.fulfillmentService = fulfillmentService;
        this.fulfillmentRepository = fulfillmentRepository;
        this.fulfillmentQueryService = fulfillmentQueryService;
    }

    /**
     * {@code POST  /fulfillments} : Create a new fulfillment.
     *
     * @param fulfillmentDTO the fulfillmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fulfillmentDTO, or with status {@code 400 (Bad Request)} if the fulfillment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fulfillments")
    public ResponseEntity<FulfillmentDTO> createFulfillment(@Valid @RequestBody FulfillmentDTO fulfillmentDTO) throws URISyntaxException {
        log.debug("REST request to save Fulfillment : {}", fulfillmentDTO);
        if (fulfillmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new fulfillment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FulfillmentDTO result = fulfillmentService.save(fulfillmentDTO);
        return ResponseEntity
            .created(new URI("/api/fulfillments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fulfillments/:id} : Updates an existing fulfillment.
     *
     * @param id the id of the fulfillmentDTO to save.
     * @param fulfillmentDTO the fulfillmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fulfillmentDTO,
     * or with status {@code 400 (Bad Request)} if the fulfillmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fulfillmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fulfillments/{id}")
    public ResponseEntity<FulfillmentDTO> updateFulfillment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FulfillmentDTO fulfillmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fulfillment : {}, {}", id, fulfillmentDTO);
        if (fulfillmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fulfillmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fulfillmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FulfillmentDTO result = fulfillmentService.save(fulfillmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fulfillmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fulfillments/:id} : Partial updates given fields of an existing fulfillment, field will ignore if it is null
     *
     * @param id the id of the fulfillmentDTO to save.
     * @param fulfillmentDTO the fulfillmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fulfillmentDTO,
     * or with status {@code 400 (Bad Request)} if the fulfillmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fulfillmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fulfillmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fulfillments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FulfillmentDTO> partialUpdateFulfillment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FulfillmentDTO fulfillmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fulfillment partially : {}, {}", id, fulfillmentDTO);
        if (fulfillmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fulfillmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fulfillmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FulfillmentDTO> result = fulfillmentService.partialUpdate(fulfillmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fulfillmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fulfillments} : get all the fulfillments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fulfillments in body.
     */
    @GetMapping("/fulfillments")
    public ResponseEntity<List<FulfillmentDTO>> getAllFulfillments(FulfillmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Fulfillments by criteria: {}", criteria);
        Page<FulfillmentDTO> page = fulfillmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fulfillments/count} : count all the fulfillments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fulfillments/count")
    public ResponseEntity<Long> countFulfillments(FulfillmentCriteria criteria) {
        log.debug("REST request to count Fulfillments by criteria: {}", criteria);
        return ResponseEntity.ok().body(fulfillmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fulfillments/:id} : get the "id" fulfillment.
     *
     * @param id the id of the fulfillmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fulfillmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fulfillments/{id}")
    public ResponseEntity<FulfillmentDTO> getFulfillment(@PathVariable Long id) {
        log.debug("REST request to get Fulfillment : {}", id);
        Optional<FulfillmentDTO> fulfillmentDTO = fulfillmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fulfillmentDTO);
    }

    /**
     * {@code DELETE  /fulfillments/:id} : delete the "id" fulfillment.
     *
     * @param id the id of the fulfillmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fulfillments/{id}")
    public ResponseEntity<Void> deleteFulfillment(@PathVariable Long id) {
        log.debug("REST request to delete Fulfillment : {}", id);
        fulfillmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
