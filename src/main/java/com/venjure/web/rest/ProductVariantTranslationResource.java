package com.venjure.web.rest;

import com.venjure.repository.ProductVariantTranslationRepository;
import com.venjure.service.ProductVariantTranslationQueryService;
import com.venjure.service.ProductVariantTranslationService;
import com.venjure.service.criteria.ProductVariantTranslationCriteria;
import com.venjure.service.dto.ProductVariantTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductVariantTranslation}.
 */
@RestController
@RequestMapping("/api")
public class ProductVariantTranslationResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantTranslationResource.class);

    private static final String ENTITY_NAME = "productVariantTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantTranslationService productVariantTranslationService;

    private final ProductVariantTranslationRepository productVariantTranslationRepository;

    private final ProductVariantTranslationQueryService productVariantTranslationQueryService;

    public ProductVariantTranslationResource(
        ProductVariantTranslationService productVariantTranslationService,
        ProductVariantTranslationRepository productVariantTranslationRepository,
        ProductVariantTranslationQueryService productVariantTranslationQueryService
    ) {
        this.productVariantTranslationService = productVariantTranslationService;
        this.productVariantTranslationRepository = productVariantTranslationRepository;
        this.productVariantTranslationQueryService = productVariantTranslationQueryService;
    }

    /**
     * {@code POST  /product-variant-translations} : Create a new productVariantTranslation.
     *
     * @param productVariantTranslationDTO the productVariantTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariantTranslationDTO, or with status {@code 400 (Bad Request)} if the productVariantTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-variant-translations")
    public ResponseEntity<ProductVariantTranslationDTO> createProductVariantTranslation(
        @Valid @RequestBody ProductVariantTranslationDTO productVariantTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProductVariantTranslation : {}", productVariantTranslationDTO);
        if (productVariantTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariantTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariantTranslationDTO result = productVariantTranslationService.save(productVariantTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/product-variant-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-variant-translations/:id} : Updates an existing productVariantTranslation.
     *
     * @param id the id of the productVariantTranslationDTO to save.
     * @param productVariantTranslationDTO the productVariantTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariantTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-variant-translations/{id}")
    public ResponseEntity<ProductVariantTranslationDTO> updateProductVariantTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariantTranslationDTO productVariantTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductVariantTranslation : {}, {}", id, productVariantTranslationDTO);
        if (productVariantTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductVariantTranslationDTO result = productVariantTranslationService.save(productVariantTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantTranslationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /product-variant-translations/:id} : Partial updates given fields of an existing productVariantTranslation, field will ignore if it is null
     *
     * @param id the id of the productVariantTranslationDTO to save.
     * @param productVariantTranslationDTO the productVariantTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariantTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productVariantTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productVariantTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariantTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-variant-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductVariantTranslationDTO> partialUpdateProductVariantTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariantTranslationDTO productVariantTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductVariantTranslation partially : {}, {}", id, productVariantTranslationDTO);
        if (productVariantTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariantTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariantTranslationDTO> result = productVariantTranslationService.partialUpdate(productVariantTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVariantTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variant-translations} : get all the productVariantTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariantTranslations in body.
     */
    @GetMapping("/product-variant-translations")
    public ResponseEntity<List<ProductVariantTranslationDTO>> getAllProductVariantTranslations(
        ProductVariantTranslationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProductVariantTranslations by criteria: {}", criteria);
        Page<ProductVariantTranslationDTO> page = productVariantTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-variant-translations/count} : count all the productVariantTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-variant-translations/count")
    public ResponseEntity<Long> countProductVariantTranslations(ProductVariantTranslationCriteria criteria) {
        log.debug("REST request to count ProductVariantTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(productVariantTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-variant-translations/:id} : get the "id" productVariantTranslation.
     *
     * @param id the id of the productVariantTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariantTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-variant-translations/{id}")
    public ResponseEntity<ProductVariantTranslationDTO> getProductVariantTranslation(@PathVariable Long id) {
        log.debug("REST request to get ProductVariantTranslation : {}", id);
        Optional<ProductVariantTranslationDTO> productVariantTranslationDTO = productVariantTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantTranslationDTO);
    }

    /**
     * {@code DELETE  /product-variant-translations/:id} : delete the "id" productVariantTranslation.
     *
     * @param id the id of the productVariantTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-variant-translations/{id}")
    public ResponseEntity<Void> deleteProductVariantTranslation(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariantTranslation : {}", id);
        productVariantTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
