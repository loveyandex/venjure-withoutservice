package com.venjure.web.rest;

import com.venjure.repository.ShippingLineRepository;
import com.venjure.service.ShippingLineQueryService;
import com.venjure.service.ShippingLineService;
import com.venjure.service.criteria.ShippingLineCriteria;
import com.venjure.service.dto.ShippingLineDTO;
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
 * REST controller for managing {@link com.venjure.domain.ShippingLine}.
 */
@RestController
@RequestMapping("/api")
public class ShippingLineResource {

    private final Logger log = LoggerFactory.getLogger(ShippingLineResource.class);

    private static final String ENTITY_NAME = "shippingLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShippingLineService shippingLineService;

    private final ShippingLineRepository shippingLineRepository;

    private final ShippingLineQueryService shippingLineQueryService;

    public ShippingLineResource(
        ShippingLineService shippingLineService,
        ShippingLineRepository shippingLineRepository,
        ShippingLineQueryService shippingLineQueryService
    ) {
        this.shippingLineService = shippingLineService;
        this.shippingLineRepository = shippingLineRepository;
        this.shippingLineQueryService = shippingLineQueryService;
    }

    /**
     * {@code POST  /shipping-lines} : Create a new shippingLine.
     *
     * @param shippingLineDTO the shippingLineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shippingLineDTO, or with status {@code 400 (Bad Request)} if the shippingLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipping-lines")
    public ResponseEntity<ShippingLineDTO> createShippingLine(@Valid @RequestBody ShippingLineDTO shippingLineDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShippingLine : {}", shippingLineDTO);
        if (shippingLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new shippingLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShippingLineDTO result = shippingLineService.save(shippingLineDTO);
        return ResponseEntity
            .created(new URI("/api/shipping-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipping-lines/:id} : Updates an existing shippingLine.
     *
     * @param id the id of the shippingLineDTO to save.
     * @param shippingLineDTO the shippingLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingLineDTO,
     * or with status {@code 400 (Bad Request)} if the shippingLineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shippingLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipping-lines/{id}")
    public ResponseEntity<ShippingLineDTO> updateShippingLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShippingLineDTO shippingLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShippingLine : {}, {}", id, shippingLineDTO);
        if (shippingLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShippingLineDTO result = shippingLineService.save(shippingLineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingLineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shipping-lines/:id} : Partial updates given fields of an existing shippingLine, field will ignore if it is null
     *
     * @param id the id of the shippingLineDTO to save.
     * @param shippingLineDTO the shippingLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingLineDTO,
     * or with status {@code 400 (Bad Request)} if the shippingLineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shippingLineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shippingLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shipping-lines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ShippingLineDTO> partialUpdateShippingLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShippingLineDTO shippingLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShippingLine partially : {}, {}", id, shippingLineDTO);
        if (shippingLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShippingLineDTO> result = shippingLineService.partialUpdate(shippingLineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingLineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipping-lines} : get all the shippingLines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shippingLines in body.
     */
    @GetMapping("/shipping-lines")
    public ResponseEntity<List<ShippingLineDTO>> getAllShippingLines(ShippingLineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShippingLines by criteria: {}", criteria);
        Page<ShippingLineDTO> page = shippingLineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipping-lines/count} : count all the shippingLines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shipping-lines/count")
    public ResponseEntity<Long> countShippingLines(ShippingLineCriteria criteria) {
        log.debug("REST request to count ShippingLines by criteria: {}", criteria);
        return ResponseEntity.ok().body(shippingLineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipping-lines/:id} : get the "id" shippingLine.
     *
     * @param id the id of the shippingLineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shippingLineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipping-lines/{id}")
    public ResponseEntity<ShippingLineDTO> getShippingLine(@PathVariable Long id) {
        log.debug("REST request to get ShippingLine : {}", id);
        Optional<ShippingLineDTO> shippingLineDTO = shippingLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shippingLineDTO);
    }

    /**
     * {@code DELETE  /shipping-lines/:id} : delete the "id" shippingLine.
     *
     * @param id the id of the shippingLineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipping-lines/{id}")
    public ResponseEntity<Void> deleteShippingLine(@PathVariable Long id) {
        log.debug("REST request to delete ShippingLine : {}", id);
        shippingLineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
