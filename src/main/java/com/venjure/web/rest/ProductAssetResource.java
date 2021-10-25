package com.venjure.web.rest;

import com.venjure.repository.ProductAssetRepository;
import com.venjure.service.ProductAssetQueryService;
import com.venjure.service.ProductAssetService;
import com.venjure.service.criteria.ProductAssetCriteria;
import com.venjure.service.dto.ProductAssetDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductAsset}.
 */
@RestController
@RequestMapping("/api")
public class ProductAssetResource {

    private final Logger log = LoggerFactory.getLogger(ProductAssetResource.class);

    private static final String ENTITY_NAME = "productAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductAssetService productAssetService;

    private final ProductAssetRepository productAssetRepository;

    private final ProductAssetQueryService productAssetQueryService;

    public ProductAssetResource(
        ProductAssetService productAssetService,
        ProductAssetRepository productAssetRepository,
        ProductAssetQueryService productAssetQueryService
    ) {
        this.productAssetService = productAssetService;
        this.productAssetRepository = productAssetRepository;
        this.productAssetQueryService = productAssetQueryService;
    }

    /**
     * {@code POST  /product-assets} : Create a new productAsset.
     *
     * @param productAssetDTO the productAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAssetDTO, or with status {@code 400 (Bad Request)} if the productAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-assets")
    public ResponseEntity<ProductAssetDTO> createProductAsset(@Valid @RequestBody ProductAssetDTO productAssetDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductAsset : {}", productAssetDTO);
        if (productAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new productAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductAssetDTO result = productAssetService.save(productAssetDTO);
        return ResponseEntity
            .created(new URI("/api/product-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-assets/:id} : Updates an existing productAsset.
     *
     * @param id the id of the productAssetDTO to save.
     * @param productAssetDTO the productAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssetDTO,
     * or with status {@code 400 (Bad Request)} if the productAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-assets/{id}")
    public ResponseEntity<ProductAssetDTO> updateProductAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductAssetDTO productAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductAsset : {}, {}", id, productAssetDTO);
        if (productAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductAssetDTO result = productAssetService.save(productAssetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-assets/:id} : Partial updates given fields of an existing productAsset, field will ignore if it is null
     *
     * @param id the id of the productAssetDTO to save.
     * @param productAssetDTO the productAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssetDTO,
     * or with status {@code 400 (Bad Request)} if the productAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-assets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductAssetDTO> partialUpdateProductAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductAssetDTO productAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductAsset partially : {}, {}", id, productAssetDTO);
        if (productAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAssetDTO> result = productAssetService.partialUpdate(productAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-assets} : get all the productAssets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productAssets in body.
     */
    @GetMapping("/product-assets")
    public ResponseEntity<List<ProductAssetDTO>> getAllProductAssets(ProductAssetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductAssets by criteria: {}", criteria);
        Page<ProductAssetDTO> page = productAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-assets/count} : count all the productAssets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-assets/count")
    public ResponseEntity<Long> countProductAssets(ProductAssetCriteria criteria) {
        log.debug("REST request to count ProductAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(productAssetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-assets/:id} : get the "id" productAsset.
     *
     * @param id the id of the productAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-assets/{id}")
    public ResponseEntity<ProductAssetDTO> getProductAsset(@PathVariable Long id) {
        log.debug("REST request to get ProductAsset : {}", id);
        Optional<ProductAssetDTO> productAssetDTO = productAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productAssetDTO);
    }

    /**
     * {@code DELETE  /product-assets/:id} : delete the "id" productAsset.
     *
     * @param id the id of the productAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-assets/{id}")
    public ResponseEntity<Void> deleteProductAsset(@PathVariable Long id) {
        log.debug("REST request to delete ProductAsset : {}", id);
        productAssetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
