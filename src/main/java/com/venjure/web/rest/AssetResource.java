package com.venjure.web.rest;

import com.venjure.repository.AssetRepository;
import com.venjure.service.AssetQueryService;
import com.venjure.service.AssetService;
import com.venjure.service.criteria.AssetCriteria;
import com.venjure.service.dto.AssetDTO;
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
 * REST controller for managing {@link com.venjure.domain.Asset}.
 */
@RestController
@RequestMapping("/api")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetService assetService;

    private final AssetRepository assetRepository;

    private final AssetQueryService assetQueryService;

    public AssetResource(AssetService assetService, AssetRepository assetRepository, AssetQueryService assetQueryService) {
        this.assetService = assetService;
        this.assetRepository = assetRepository;
        this.assetQueryService = assetQueryService;
    }

    /**
     * {@code POST  /assets} : Create a new asset.
     *
     * @param assetDTO the assetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDTO, or with status {@code 400 (Bad Request)} if the asset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assets")
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody AssetDTO assetDTO) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", assetDTO);
        if (assetDTO.getId() != null) {
            throw new BadRequestAlertException("A new asset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetDTO result = assetService.save(assetDTO);
        return ResponseEntity
            .created(new URI("/api/assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assets/:id} : Updates an existing asset.
     *
     * @param id the id of the assetDTO to save.
     * @param assetDTO the assetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDTO,
     * or with status {@code 400 (Bad Request)} if the assetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assets/{id}")
    public ResponseEntity<AssetDTO> updateAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetDTO assetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Asset : {}, {}", id, assetDTO);
        if (assetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetDTO result = assetService.save(assetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assets/:id} : Partial updates given fields of an existing asset, field will ignore if it is null
     *
     * @param id the id of the assetDTO to save.
     * @param assetDTO the assetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDTO,
     * or with status {@code 400 (Bad Request)} if the assetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AssetDTO> partialUpdateAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetDTO assetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Asset partially : {}, {}", id, assetDTO);
        if (assetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetDTO> result = assetService.partialUpdate(assetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assets} : get all the assets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GetMapping("/assets")
    public ResponseEntity<List<AssetDTO>> getAllAssets(AssetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Assets by criteria: {}", criteria);
        Page<AssetDTO> page = assetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assets/count} : count all the assets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assets/count")
    public ResponseEntity<Long> countAssets(AssetCriteria criteria) {
        log.debug("REST request to count Assets by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the assetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assets/{id}")
    public ResponseEntity<AssetDTO> getAsset(@PathVariable Long id) {
        log.debug("REST request to get Asset : {}", id);
        Optional<AssetDTO> assetDTO = assetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDTO);
    }

    /**
     * {@code DELETE  /assets/:id} : delete the "id" asset.
     *
     * @param id the id of the assetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.debug("REST request to delete Asset : {}", id);
        assetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
