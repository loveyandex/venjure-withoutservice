package com.venjure.web.rest;

import com.venjure.repository.ProductOptionTranslationRepository;
import com.venjure.service.ProductOptionTranslationQueryService;
import com.venjure.service.ProductOptionTranslationService;
import com.venjure.service.criteria.ProductOptionTranslationCriteria;
import com.venjure.service.dto.ProductOptionTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductOptionTranslation}.
 */
@RestController
@RequestMapping("/api")
public class ProductOptionTranslationResource {

    private final Logger log = LoggerFactory.getLogger(ProductOptionTranslationResource.class);

    private static final String ENTITY_NAME = "productOptionTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOptionTranslationService productOptionTranslationService;

    private final ProductOptionTranslationRepository productOptionTranslationRepository;

    private final ProductOptionTranslationQueryService productOptionTranslationQueryService;

    public ProductOptionTranslationResource(
        ProductOptionTranslationService productOptionTranslationService,
        ProductOptionTranslationRepository productOptionTranslationRepository,
        ProductOptionTranslationQueryService productOptionTranslationQueryService
    ) {
        this.productOptionTranslationService = productOptionTranslationService;
        this.productOptionTranslationRepository = productOptionTranslationRepository;
        this.productOptionTranslationQueryService = productOptionTranslationQueryService;
    }

    /**
     * {@code POST  /product-option-translations} : Create a new productOptionTranslation.
     *
     * @param productOptionTranslationDTO the productOptionTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOptionTranslationDTO, or with status {@code 400 (Bad Request)} if the productOptionTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-option-translations")
    public ResponseEntity<ProductOptionTranslationDTO> createProductOptionTranslation(
        @Valid @RequestBody ProductOptionTranslationDTO productOptionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProductOptionTranslation : {}", productOptionTranslationDTO);
        if (productOptionTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOptionTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOptionTranslationDTO result = productOptionTranslationService.save(productOptionTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/product-option-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-option-translations/:id} : Updates an existing productOptionTranslation.
     *
     * @param id the id of the productOptionTranslationDTO to save.
     * @param productOptionTranslationDTO the productOptionTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOptionTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-option-translations/{id}")
    public ResponseEntity<ProductOptionTranslationDTO> updateProductOptionTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductOptionTranslationDTO productOptionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductOptionTranslation : {}, {}", id, productOptionTranslationDTO);
        if (productOptionTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductOptionTranslationDTO result = productOptionTranslationService.save(productOptionTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-option-translations/:id} : Partial updates given fields of an existing productOptionTranslation, field will ignore if it is null
     *
     * @param id the id of the productOptionTranslationDTO to save.
     * @param productOptionTranslationDTO the productOptionTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productOptionTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOptionTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-option-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductOptionTranslationDTO> partialUpdateProductOptionTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductOptionTranslationDTO productOptionTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductOptionTranslation partially : {}, {}", id, productOptionTranslationDTO);
        if (productOptionTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductOptionTranslationDTO> result = productOptionTranslationService.partialUpdate(productOptionTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-option-translations} : get all the productOptionTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOptionTranslations in body.
     */
    @GetMapping("/product-option-translations")
    public ResponseEntity<List<ProductOptionTranslationDTO>> getAllProductOptionTranslations(
        ProductOptionTranslationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProductOptionTranslations by criteria: {}", criteria);
        Page<ProductOptionTranslationDTO> page = productOptionTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-option-translations/count} : count all the productOptionTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-option-translations/count")
    public ResponseEntity<Long> countProductOptionTranslations(ProductOptionTranslationCriteria criteria) {
        log.debug("REST request to count ProductOptionTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(productOptionTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-option-translations/:id} : get the "id" productOptionTranslation.
     *
     * @param id the id of the productOptionTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOptionTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-option-translations/{id}")
    public ResponseEntity<ProductOptionTranslationDTO> getProductOptionTranslation(@PathVariable Long id) {
        log.debug("REST request to get ProductOptionTranslation : {}", id);
        Optional<ProductOptionTranslationDTO> productOptionTranslationDTO = productOptionTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOptionTranslationDTO);
    }

    /**
     * {@code DELETE  /product-option-translations/:id} : delete the "id" productOptionTranslation.
     *
     * @param id the id of the productOptionTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-option-translations/{id}")
    public ResponseEntity<Void> deleteProductOptionTranslation(@PathVariable Long id) {
        log.debug("REST request to delete ProductOptionTranslation : {}", id);
        productOptionTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
