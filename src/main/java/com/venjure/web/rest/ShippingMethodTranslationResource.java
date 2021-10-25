package com.venjure.web.rest;

import com.venjure.repository.ShippingMethodTranslationRepository;
import com.venjure.service.ShippingMethodTranslationQueryService;
import com.venjure.service.ShippingMethodTranslationService;
import com.venjure.service.criteria.ShippingMethodTranslationCriteria;
import com.venjure.service.dto.ShippingMethodTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.ShippingMethodTranslation}.
 */
@RestController
@RequestMapping("/api")
public class ShippingMethodTranslationResource {

    private final Logger log = LoggerFactory.getLogger(ShippingMethodTranslationResource.class);

    private static final String ENTITY_NAME = "shippingMethodTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShippingMethodTranslationService shippingMethodTranslationService;

    private final ShippingMethodTranslationRepository shippingMethodTranslationRepository;

    private final ShippingMethodTranslationQueryService shippingMethodTranslationQueryService;

    public ShippingMethodTranslationResource(
        ShippingMethodTranslationService shippingMethodTranslationService,
        ShippingMethodTranslationRepository shippingMethodTranslationRepository,
        ShippingMethodTranslationQueryService shippingMethodTranslationQueryService
    ) {
        this.shippingMethodTranslationService = shippingMethodTranslationService;
        this.shippingMethodTranslationRepository = shippingMethodTranslationRepository;
        this.shippingMethodTranslationQueryService = shippingMethodTranslationQueryService;
    }

    /**
     * {@code POST  /shipping-method-translations} : Create a new shippingMethodTranslation.
     *
     * @param shippingMethodTranslationDTO the shippingMethodTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shippingMethodTranslationDTO, or with status {@code 400 (Bad Request)} if the shippingMethodTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipping-method-translations")
    public ResponseEntity<ShippingMethodTranslationDTO> createShippingMethodTranslation(
        @Valid @RequestBody ShippingMethodTranslationDTO shippingMethodTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ShippingMethodTranslation : {}", shippingMethodTranslationDTO);
        if (shippingMethodTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new shippingMethodTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShippingMethodTranslationDTO result = shippingMethodTranslationService.save(shippingMethodTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/shipping-method-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipping-method-translations/:id} : Updates an existing shippingMethodTranslation.
     *
     * @param id the id of the shippingMethodTranslationDTO to save.
     * @param shippingMethodTranslationDTO the shippingMethodTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingMethodTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the shippingMethodTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shippingMethodTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipping-method-translations/{id}")
    public ResponseEntity<ShippingMethodTranslationDTO> updateShippingMethodTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShippingMethodTranslationDTO shippingMethodTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShippingMethodTranslation : {}, {}", id, shippingMethodTranslationDTO);
        if (shippingMethodTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingMethodTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingMethodTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShippingMethodTranslationDTO result = shippingMethodTranslationService.save(shippingMethodTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingMethodTranslationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /shipping-method-translations/:id} : Partial updates given fields of an existing shippingMethodTranslation, field will ignore if it is null
     *
     * @param id the id of the shippingMethodTranslationDTO to save.
     * @param shippingMethodTranslationDTO the shippingMethodTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingMethodTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the shippingMethodTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shippingMethodTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shippingMethodTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shipping-method-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ShippingMethodTranslationDTO> partialUpdateShippingMethodTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShippingMethodTranslationDTO shippingMethodTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShippingMethodTranslation partially : {}, {}", id, shippingMethodTranslationDTO);
        if (shippingMethodTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingMethodTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingMethodTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShippingMethodTranslationDTO> result = shippingMethodTranslationService.partialUpdate(shippingMethodTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingMethodTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipping-method-translations} : get all the shippingMethodTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shippingMethodTranslations in body.
     */
    @GetMapping("/shipping-method-translations")
    public ResponseEntity<List<ShippingMethodTranslationDTO>> getAllShippingMethodTranslations(
        ShippingMethodTranslationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ShippingMethodTranslations by criteria: {}", criteria);
        Page<ShippingMethodTranslationDTO> page = shippingMethodTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipping-method-translations/count} : count all the shippingMethodTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shipping-method-translations/count")
    public ResponseEntity<Long> countShippingMethodTranslations(ShippingMethodTranslationCriteria criteria) {
        log.debug("REST request to count ShippingMethodTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(shippingMethodTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipping-method-translations/:id} : get the "id" shippingMethodTranslation.
     *
     * @param id the id of the shippingMethodTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shippingMethodTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipping-method-translations/{id}")
    public ResponseEntity<ShippingMethodTranslationDTO> getShippingMethodTranslation(@PathVariable Long id) {
        log.debug("REST request to get ShippingMethodTranslation : {}", id);
        Optional<ShippingMethodTranslationDTO> shippingMethodTranslationDTO = shippingMethodTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shippingMethodTranslationDTO);
    }

    /**
     * {@code DELETE  /shipping-method-translations/:id} : delete the "id" shippingMethodTranslation.
     *
     * @param id the id of the shippingMethodTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipping-method-translations/{id}")
    public ResponseEntity<Void> deleteShippingMethodTranslation(@PathVariable Long id) {
        log.debug("REST request to delete ShippingMethodTranslation : {}", id);
        shippingMethodTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
