package com.venjure.web.rest;

import com.venjure.repository.CollectionTranslationRepository;
import com.venjure.service.CollectionTranslationQueryService;
import com.venjure.service.CollectionTranslationService;
import com.venjure.service.criteria.CollectionTranslationCriteria;
import com.venjure.service.dto.CollectionTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.CollectionTranslation}.
 */
@RestController
@RequestMapping("/api")
public class CollectionTranslationResource {

    private final Logger log = LoggerFactory.getLogger(CollectionTranslationResource.class);

    private static final String ENTITY_NAME = "collectionTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionTranslationService collectionTranslationService;

    private final CollectionTranslationRepository collectionTranslationRepository;

    private final CollectionTranslationQueryService collectionTranslationQueryService;

    public CollectionTranslationResource(
        CollectionTranslationService collectionTranslationService,
        CollectionTranslationRepository collectionTranslationRepository,
        CollectionTranslationQueryService collectionTranslationQueryService
    ) {
        this.collectionTranslationService = collectionTranslationService;
        this.collectionTranslationRepository = collectionTranslationRepository;
        this.collectionTranslationQueryService = collectionTranslationQueryService;
    }

    /**
     * {@code POST  /collection-translations} : Create a new collectionTranslation.
     *
     * @param collectionTranslationDTO the collectionTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectionTranslationDTO, or with status {@code 400 (Bad Request)} if the collectionTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collection-translations")
    public ResponseEntity<CollectionTranslationDTO> createCollectionTranslation(
        @Valid @RequestBody CollectionTranslationDTO collectionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CollectionTranslation : {}", collectionTranslationDTO);
        if (collectionTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectionTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectionTranslationDTO result = collectionTranslationService.save(collectionTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/collection-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collection-translations/:id} : Updates an existing collectionTranslation.
     *
     * @param id the id of the collectionTranslationDTO to save.
     * @param collectionTranslationDTO the collectionTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the collectionTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectionTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collection-translations/{id}")
    public ResponseEntity<CollectionTranslationDTO> updateCollectionTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollectionTranslationDTO collectionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollectionTranslation : {}, {}", id, collectionTranslationDTO);
        if (collectionTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollectionTranslationDTO result = collectionTranslationService.save(collectionTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collection-translations/:id} : Partial updates given fields of an existing collectionTranslation, field will ignore if it is null
     *
     * @param id the id of the collectionTranslationDTO to save.
     * @param collectionTranslationDTO the collectionTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the collectionTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collectionTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collectionTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collection-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CollectionTranslationDTO> partialUpdateCollectionTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollectionTranslationDTO collectionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollectionTranslation partially : {}, {}", id, collectionTranslationDTO);
        if (collectionTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollectionTranslationDTO> result = collectionTranslationService.partialUpdate(collectionTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collection-translations} : get all the collectionTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectionTranslations in body.
     */
    @GetMapping("/collection-translations")
    public ResponseEntity<List<CollectionTranslationDTO>> getAllCollectionTranslations(
        CollectionTranslationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CollectionTranslations by criteria: {}", criteria);
        Page<CollectionTranslationDTO> page = collectionTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collection-translations/count} : count all the collectionTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collection-translations/count")
    public ResponseEntity<Long> countCollectionTranslations(CollectionTranslationCriteria criteria) {
        log.debug("REST request to count CollectionTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(collectionTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collection-translations/:id} : get the "id" collectionTranslation.
     *
     * @param id the id of the collectionTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectionTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collection-translations/{id}")
    public ResponseEntity<CollectionTranslationDTO> getCollectionTranslation(@PathVariable Long id) {
        log.debug("REST request to get CollectionTranslation : {}", id);
        Optional<CollectionTranslationDTO> collectionTranslationDTO = collectionTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectionTranslationDTO);
    }

    /**
     * {@code DELETE  /collection-translations/:id} : delete the "id" collectionTranslation.
     *
     * @param id the id of the collectionTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collection-translations/{id}")
    public ResponseEntity<Void> deleteCollectionTranslation(@PathVariable Long id) {
        log.debug("REST request to delete CollectionTranslation : {}", id);
        collectionTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
