package com.venjure.web.rest;

import com.venjure.repository.ShippingMethodRepository;
import com.venjure.service.ShippingMethodQueryService;
import com.venjure.service.ShippingMethodService;
import com.venjure.service.criteria.ShippingMethodCriteria;
import com.venjure.service.dto.ShippingMethodDTO;
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
 * REST controller for managing {@link com.venjure.domain.ShippingMethod}.
 */
@RestController
@RequestMapping("/api")
public class ShippingMethodResource {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodResource.class);

    private static final String ENTITY_NAME = "shippingMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShippingMethodService shippingMethodService;

    private final ShippingMethodRepository shippingMethodRepository;

    private final ShippingMethodQueryService shippingMethodQueryService;

    public ShippingMethodResource(
        ShippingMethodService shippingMethodService,
        ShippingMethodRepository shippingMethodRepository,
        ShippingMethodQueryService shippingMethodQueryService
    ) {
        this.shippingMethodService = shippingMethodService;
        this.shippingMethodRepository = shippingMethodRepository;
        this.shippingMethodQueryService = shippingMethodQueryService;
    }

    /**
     * {@code POST  /shipping-methods} : Create a new shippingMethod.
     *
     * @param shippingMethodDTO the shippingMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shippingMethodDTO, or with status {@code 400 (Bad Request)} if the shippingMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipping-methods")
    public ResponseEntity<ShippingMethodDTO> createShippingMethod(@Valid @RequestBody ShippingMethodDTO shippingMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShippingMethod : {}", shippingMethodDTO);
        if (shippingMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new shippingMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShippingMethodDTO result = shippingMethodService.save(shippingMethodDTO);
        return ResponseEntity
            .created(new URI("/api/shipping-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipping-methods/:id} : Updates an existing shippingMethod.
     *
     * @param id the id of the shippingMethodDTO to save.
     * @param shippingMethodDTO the shippingMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingMethodDTO,
     * or with status {@code 400 (Bad Request)} if the shippingMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shippingMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipping-methods/{id}")
    public ResponseEntity<ShippingMethodDTO> updateShippingMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShippingMethodDTO shippingMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShippingMethod : {}, {}", id, shippingMethodDTO);
        if (shippingMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShippingMethodDTO result = shippingMethodService.save(shippingMethodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shipping-methods/:id} : Partial updates given fields of an existing shippingMethod, field will ignore if it is null
     *
     * @param id the id of the shippingMethodDTO to save.
     * @param shippingMethodDTO the shippingMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingMethodDTO,
     * or with status {@code 400 (Bad Request)} if the shippingMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shippingMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shippingMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shipping-methods/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ShippingMethodDTO> partialUpdateShippingMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShippingMethodDTO shippingMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShippingMethod partially : {}, {}", id, shippingMethodDTO);
        if (shippingMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShippingMethodDTO> result = shippingMethodService.partialUpdate(shippingMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipping-methods} : get all the shippingMethods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shippingMethods in body.
     */
    @GetMapping("/shipping-methods")
    public ResponseEntity<List<ShippingMethodDTO>> getAllShippingMethods(ShippingMethodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShippingMethods by criteria: {}", criteria);
        Page<ShippingMethodDTO> page = shippingMethodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipping-methods/count} : count all the shippingMethods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shipping-methods/count")
    public ResponseEntity<Long> countShippingMethods(ShippingMethodCriteria criteria) {
        log.debug("REST request to count ShippingMethods by criteria: {}", criteria);
        return ResponseEntity.ok().body(shippingMethodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipping-methods/:id} : get the "id" shippingMethod.
     *
     * @param id the id of the shippingMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shippingMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipping-methods/{id}")
    public ResponseEntity<ShippingMethodDTO> getShippingMethod(@PathVariable Long id) {
        log.debug("REST request to get ShippingMethod : {}", id);
        Optional<ShippingMethodDTO> shippingMethodDTO = shippingMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shippingMethodDTO);
    }

    /**
     * {@code DELETE  /shipping-methods/:id} : delete the "id" shippingMethod.
     *
     * @param id the id of the shippingMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipping-methods/{id}")
    public ResponseEntity<Void> deleteShippingMethod(@PathVariable Long id) {
        log.debug("REST request to delete ShippingMethod : {}", id);
        shippingMethodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
