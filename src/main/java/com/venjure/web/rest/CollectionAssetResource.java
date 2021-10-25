package com.venjure.web.rest;

import com.venjure.repository.CollectionAssetRepository;
import com.venjure.service.CollectionAssetQueryService;
import com.venjure.service.CollectionAssetService;
import com.venjure.service.criteria.CollectionAssetCriteria;
import com.venjure.service.dto.CollectionAssetDTO;
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
 * REST controller for managing {@link com.venjure.domain.CollectionAsset}.
 */
@RestController
@RequestMapping("/api")
public class CollectionAssetResource {

    private final Logger log = LoggerFactory.getLogger(CollectionAssetResource.class);

    private static final String ENTITY_NAME = "collectionAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionAssetService collectionAssetService;

    private final CollectionAssetRepository collectionAssetRepository;

    private final CollectionAssetQueryService collectionAssetQueryService;

    public CollectionAssetResource(
        CollectionAssetService collectionAssetService,
        CollectionAssetRepository collectionAssetRepository,
        CollectionAssetQueryService collectionAssetQueryService
    ) {
        this.collectionAssetService = collectionAssetService;
        this.collectionAssetRepository = collectionAssetRepository;
        this.collectionAssetQueryService = collectionAssetQueryService;
    }

    /**
     * {@code POST  /collection-assets} : Create a new collectionAsset.
     *
     * @param collectionAssetDTO the collectionAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectionAssetDTO, or with status {@code 400 (Bad Request)} if the collectionAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collection-assets")
    public ResponseEntity<CollectionAssetDTO> createCollectionAsset(@Valid @RequestBody CollectionAssetDTO collectionAssetDTO)
        throws URISyntaxException {
        log.debug("REST request to save CollectionAsset : {}", collectionAssetDTO);
        if (collectionAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectionAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectionAssetDTO result = collectionAssetService.save(collectionAssetDTO);
        return ResponseEntity
            .created(new URI("/api/collection-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collection-assets/:id} : Updates an existing collectionAsset.
     *
     * @param id the id of the collectionAssetDTO to save.
     * @param collectionAssetDTO the collectionAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionAssetDTO,
     * or with status {@code 400 (Bad Request)} if the collectionAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectionAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collection-assets/{id}")
    public ResponseEntity<CollectionAssetDTO> updateCollectionAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollectionAssetDTO collectionAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollectionAsset : {}, {}", id, collectionAssetDTO);
        if (collectionAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollectionAssetDTO result = collectionAssetService.save(collectionAssetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionAssetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collection-assets/:id} : Partial updates given fields of an existing collectionAsset, field will ignore if it is null
     *
     * @param id the id of the collectionAssetDTO to save.
     * @param collectionAssetDTO the collectionAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionAssetDTO,
     * or with status {@code 400 (Bad Request)} if the collectionAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collectionAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collectionAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collection-assets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CollectionAssetDTO> partialUpdateCollectionAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollectionAssetDTO collectionAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollectionAsset partially : {}, {}", id, collectionAssetDTO);
        if (collectionAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollectionAssetDTO> result = collectionAssetService.partialUpdate(collectionAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collection-assets} : get all the collectionAssets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectionAssets in body.
     */
    @GetMapping("/collection-assets")
    public ResponseEntity<List<CollectionAssetDTO>> getAllCollectionAssets(CollectionAssetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CollectionAssets by criteria: {}", criteria);
        Page<CollectionAssetDTO> page = collectionAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collection-assets/count} : count all the collectionAssets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collection-assets/count")
    public ResponseEntity<Long> countCollectionAssets(CollectionAssetCriteria criteria) {
        log.debug("REST request to count CollectionAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(collectionAssetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collection-assets/:id} : get the "id" collectionAsset.
     *
     * @param id the id of the collectionAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectionAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collection-assets/{id}")
    public ResponseEntity<CollectionAssetDTO> getCollectionAsset(@PathVariable Long id) {
        log.debug("REST request to get CollectionAsset : {}", id);
        Optional<CollectionAssetDTO> collectionAssetDTO = collectionAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectionAssetDTO);
    }

    /**
     * {@code DELETE  /collection-assets/:id} : delete the "id" collectionAsset.
     *
     * @param id the id of the collectionAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collection-assets/{id}")
    public ResponseEntity<Void> deleteCollectionAsset(@PathVariable Long id) {
        log.debug("REST request to delete CollectionAsset : {}", id);
        collectionAssetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
