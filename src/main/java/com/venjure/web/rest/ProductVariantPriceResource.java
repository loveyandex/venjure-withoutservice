package com.venjure.web.rest;

import com.venjure.repository.ProductVariantPriceRepository;
import com.venjure.service.ProductVariantPriceQueryService;
import com.venjure.service.ProductVariantPriceService;
import com.venjure.service.criteria.ProductVariantPriceCriteria;
import com.venjure.service.dto.ProductVariantPriceDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductVariantPrice}.
 */
@RestController
@RequestMapping("/api")
public class ProductVariantPriceResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantPriceResource.class);

    private static final String ENTITY_NAME = "productVariantPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantPriceService productVariantPriceService;

    private final ProductVariantPriceRepository productVariantPriceRepository;

    private final ProductVariantPriceQueryService productVariantPriceQueryService;

    public ProductVariantPriceResource(
        ProductVariantPriceService productVariantPriceService,
        ProductVariantPriceRepository productVariantPriceRepository,
        ProductVariantPriceQueryService productVariantPriceQueryService
    ) {
        this.productVariantPriceService = productVariantPriceService;
        this.productVariantPriceRepository = productVariantPriceRepository;
        this.productVariantPriceQueryService = productVariantPriceQueryService;
    }

    /**
     * {@code POST  /product-variant-prices} : Create a new productVariantPrice.
     *
     * @param productVariantPriceDTO the productVariantPriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariantPriceDTO, or with status {@code 400 (Bad Request)} if the productVariantPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-variant-prices")
    public ResponseEntity<ProductVariantPriceDTO> createProductVariantPrice(
        @Valid @RequestBody ProductVariantPriceDTO productVariantPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProductVariantPrice : {}", productVariantPriceDTO);
        if (productVariantPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariantPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariantPriceDTO result = productVariantPriceService.save(productVariantPriceDTO);
        return ResponseEntity
            .created(new URI("/api/product-variant-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-variant-prices/:id} : Updates an existing productVariantPrice.
     *
     * @param id the id of the productVariantPriceDTO to save.
     * @param productVariantPriceDTO the productVariantPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantPriceDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantPriceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariantPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-variant-prices/{id}")
    public ResponseEntity<ProductVariantPriceDTO> updateProductVariantPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariantPriceDTO productVariantPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductVariantPrice : {}, {}", id, productVariantPriceDTO);
        if (productVariantPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductVariantPriceDTO result = productVariantPriceService.save(productVariantPriceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantPriceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-variant-prices/:id} : Partial updates given fields of an existing productVariantPrice, field will ignore if it is null
     *
     * @param id the id of the productVariantPriceDTO to save.
     * @param productVariantPriceDTO the productVariantPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantPriceDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantPriceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productVariantPriceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariantPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-variant-prices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductVariantPriceDTO> partialUpdateProductVariantPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariantPriceDTO productVariantPriceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductVariantPrice partially : {}, {}", id, productVariantPriceDTO);
        if (productVariantPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariantPriceDTO> result = productVariantPriceService.partialUpdate(productVariantPriceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantPriceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variant-prices} : get all the productVariantPrices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariantPrices in body.
     */
    @GetMapping("/product-variant-prices")
    public ResponseEntity<List<ProductVariantPriceDTO>> getAllProductVariantPrices(
        ProductVariantPriceCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProductVariantPrices by criteria: {}", criteria);
        Page<ProductVariantPriceDTO> page = productVariantPriceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-variant-prices/count} : count all the productVariantPrices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-variant-prices/count")
    public ResponseEntity<Long> countProductVariantPrices(ProductVariantPriceCriteria criteria) {
        log.debug("REST request to count ProductVariantPrices by criteria: {}", criteria);
        return ResponseEntity.ok().body(productVariantPriceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-variant-prices/:id} : get the "id" productVariantPrice.
     *
     * @param id the id of the productVariantPriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariantPriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-variant-prices/{id}")
    public ResponseEntity<ProductVariantPriceDTO> getProductVariantPrice(@PathVariable Long id) {
        log.debug("REST request to get ProductVariantPrice : {}", id);
        Optional<ProductVariantPriceDTO> productVariantPriceDTO = productVariantPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantPriceDTO);
    }

    /**
     * {@code DELETE  /product-variant-prices/:id} : delete the "id" productVariantPrice.
     *
     * @param id the id of the productVariantPriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-variant-prices/{id}")
    public ResponseEntity<Void> deleteProductVariantPrice(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariantPrice : {}", id);
        productVariantPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
