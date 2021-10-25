package com.venjure.web.rest;

import com.venjure.repository.ProductVariantRepository;
import com.venjure.service.ProductVariantQueryService;
import com.venjure.service.ProductVariantService;
import com.venjure.service.criteria.ProductVariantCriteria;
import com.venjure.service.dto.ProductVariantDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductVariant}.
 */
@RestController
@RequestMapping("/api")
public class ProductVariantResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantResource.class);

    private static final String ENTITY_NAME = "productVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantService productVariantService;

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantQueryService productVariantQueryService;

    public ProductVariantResource(
        ProductVariantService productVariantService,
        ProductVariantRepository productVariantRepository,
        ProductVariantQueryService productVariantQueryService
    ) {
        this.productVariantService = productVariantService;
        this.productVariantRepository = productVariantRepository;
        this.productVariantQueryService = productVariantQueryService;
    }

    /**
     * {@code POST  /product-variants} : Create a new productVariant.
     *
     * @param productVariantDTO the productVariantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariantDTO, or with status {@code 400 (Bad Request)} if the productVariant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-variants")
    public ResponseEntity<ProductVariantDTO> createProductVariant(@Valid @RequestBody ProductVariantDTO productVariantDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductVariant : {}", productVariantDTO);
        if (productVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariantDTO result = productVariantService.save(productVariantDTO);
        return ResponseEntity
            .created(new URI("/api/product-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-variants/:id} : Updates an existing productVariant.
     *
     * @param id the id of the productVariantDTO to save.
     * @param productVariantDTO the productVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-variants/{id}")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariantDTO productVariantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductVariant : {}, {}", id, productVariantDTO);
        if (productVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductVariantDTO result = productVariantService.save(productVariantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-variants/:id} : Partial updates given fields of an existing productVariant, field will ignore if it is null
     *
     * @param id the id of the productVariantDTO to save.
     * @param productVariantDTO the productVariantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productVariantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-variants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductVariantDTO> partialUpdateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariantDTO productVariantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductVariant partially : {}, {}", id, productVariantDTO);
        if (productVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariantDTO> result = productVariantService.partialUpdate(productVariantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variants} : get all the productVariants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariants in body.
     */
    @GetMapping("/product-variants")
    public ResponseEntity<List<ProductVariantDTO>> getAllProductVariants(ProductVariantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductVariants by criteria: {}", criteria);
        Page<ProductVariantDTO> page = productVariantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-variants/count} : count all the productVariants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-variants/count")
    public ResponseEntity<Long> countProductVariants(ProductVariantCriteria criteria) {
        log.debug("REST request to count ProductVariants by criteria: {}", criteria);
        return ResponseEntity.ok().body(productVariantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-variants/:id} : get the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-variants/{id}")
    public ResponseEntity<ProductVariantDTO> getProductVariant(@PathVariable Long id) {
        log.debug("REST request to get ProductVariant : {}", id);
        Optional<ProductVariantDTO> productVariantDTO = productVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantDTO);
    }

    /**
     * {@code DELETE  /product-variants/:id} : delete the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-variants/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariant : {}", id);
        productVariantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
