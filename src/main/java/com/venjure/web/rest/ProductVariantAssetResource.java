package com.venjure.web.rest;

import com.venjure.repository.ProductVariantAssetRepository;
import com.venjure.service.ProductVariantAssetQueryService;
import com.venjure.service.ProductVariantAssetService;
import com.venjure.service.criteria.ProductVariantAssetCriteria;
import com.venjure.service.dto.ProductVariantAssetDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductVariantAsset}.
 */
@RestController
@RequestMapping("/api")
public class ProductVariantAssetResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantAssetResource.class);

    private static final String ENTITY_NAME = "productVariantAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantAssetService productVariantAssetService;

    private final ProductVariantAssetRepository productVariantAssetRepository;

    private final ProductVariantAssetQueryService productVariantAssetQueryService;

    public ProductVariantAssetResource(
        ProductVariantAssetService productVariantAssetService,
        ProductVariantAssetRepository productVariantAssetRepository,
        ProductVariantAssetQueryService productVariantAssetQueryService
    ) {
        this.productVariantAssetService = productVariantAssetService;
        this.productVariantAssetRepository = productVariantAssetRepository;
        this.productVariantAssetQueryService = productVariantAssetQueryService;
    }

    /**
     * {@code POST  /product-variant-assets} : Create a new productVariantAsset.
     *
     * @param productVariantAssetDTO the productVariantAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariantAssetDTO, or with status {@code 400 (Bad Request)} if the productVariantAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-variant-assets")
    public ResponseEntity<ProductVariantAssetDTO> createProductVariantAsset(
        @Valid @RequestBody ProductVariantAssetDTO productVariantAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProductVariantAsset : {}", productVariantAssetDTO);
        if (productVariantAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariantAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariantAssetDTO result = productVariantAssetService.save(productVariantAssetDTO);
        return ResponseEntity
            .created(new URI("/api/product-variant-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-variant-assets/:id} : Updates an existing productVariantAsset.
     *
     * @param id the id of the productVariantAssetDTO to save.
     * @param productVariantAssetDTO the productVariantAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantAssetDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariantAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-variant-assets/{id}")
    public ResponseEntity<ProductVariantAssetDTO> updateProductVariantAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariantAssetDTO productVariantAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductVariantAsset : {}, {}", id, productVariantAssetDTO);
        if (productVariantAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductVariantAssetDTO result = productVariantAssetService.save(productVariantAssetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantAssetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-variant-assets/:id} : Partial updates given fields of an existing productVariantAsset, field will ignore if it is null
     *
     * @param id the id of the productVariantAssetDTO to save.
     * @param productVariantAssetDTO the productVariantAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantAssetDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productVariantAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariantAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-variant-assets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductVariantAssetDTO> partialUpdateProductVariantAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariantAssetDTO productVariantAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductVariantAsset partially : {}, {}", id, productVariantAssetDTO);
        if (productVariantAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariantAssetDTO> result = productVariantAssetService.partialUpdate(productVariantAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variant-assets} : get all the productVariantAssets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariantAssets in body.
     */
    @GetMapping("/product-variant-assets")
    public ResponseEntity<List<ProductVariantAssetDTO>> getAllProductVariantAssets(
        ProductVariantAssetCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProductVariantAssets by criteria: {}", criteria);
        Page<ProductVariantAssetDTO> page = productVariantAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-variant-assets/count} : count all the productVariantAssets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-variant-assets/count")
    public ResponseEntity<Long> countProductVariantAssets(ProductVariantAssetCriteria criteria) {
        log.debug("REST request to count ProductVariantAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(productVariantAssetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-variant-assets/:id} : get the "id" productVariantAsset.
     *
     * @param id the id of the productVariantAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariantAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-variant-assets/{id}")
    public ResponseEntity<ProductVariantAssetDTO> getProductVariantAsset(@PathVariable Long id) {
        log.debug("REST request to get ProductVariantAsset : {}", id);
        Optional<ProductVariantAssetDTO> productVariantAssetDTO = productVariantAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantAssetDTO);
    }

    /**
     * {@code DELETE  /product-variant-assets/:id} : delete the "id" productVariantAsset.
     *
     * @param id the id of the productVariantAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-variant-assets/{id}")
    public ResponseEntity<Void> deleteProductVariantAsset(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariantAsset : {}", id);
        productVariantAssetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
