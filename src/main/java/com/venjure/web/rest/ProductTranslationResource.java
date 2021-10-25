package com.venjure.web.rest;

import com.venjure.repository.ProductTranslationRepository;
import com.venjure.service.ProductTranslationQueryService;
import com.venjure.service.ProductTranslationService;
import com.venjure.service.criteria.ProductTranslationCriteria;
import com.venjure.service.dto.ProductTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductTranslation}.
 */
@RestController
@RequestMapping("/api")
public class ProductTranslationResource {

    private final Logger log = LoggerFactory.getLogger(ProductTranslationResource.class);

    private static final String ENTITY_NAME = "productTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductTranslationService productTranslationService;

    private final ProductTranslationRepository productTranslationRepository;

    private final ProductTranslationQueryService productTranslationQueryService;

    public ProductTranslationResource(
        ProductTranslationService productTranslationService,
        ProductTranslationRepository productTranslationRepository,
        ProductTranslationQueryService productTranslationQueryService
    ) {
        this.productTranslationService = productTranslationService;
        this.productTranslationRepository = productTranslationRepository;
        this.productTranslationQueryService = productTranslationQueryService;
    }

    /**
     * {@code POST  /product-translations} : Create a new productTranslation.
     *
     * @param productTranslationDTO the productTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTranslationDTO, or with status {@code 400 (Bad Request)} if the productTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-translations")
    public ResponseEntity<ProductTranslationDTO> createProductTranslation(@Valid @RequestBody ProductTranslationDTO productTranslationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductTranslation : {}", productTranslationDTO);
        if (productTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new productTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTranslationDTO result = productTranslationService.save(productTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/product-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-translations/:id} : Updates an existing productTranslation.
     *
     * @param id the id of the productTranslationDTO to save.
     * @param productTranslationDTO the productTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-translations/{id}")
    public ResponseEntity<ProductTranslationDTO> updateProductTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductTranslationDTO productTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductTranslation : {}, {}", id, productTranslationDTO);
        if (productTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductTranslationDTO result = productTranslationService.save(productTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-translations/:id} : Partial updates given fields of an existing productTranslation, field will ignore if it is null
     *
     * @param id the id of the productTranslationDTO to save.
     * @param productTranslationDTO the productTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the productTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductTranslationDTO> partialUpdateProductTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductTranslationDTO productTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductTranslation partially : {}, {}", id, productTranslationDTO);
        if (productTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductTranslationDTO> result = productTranslationService.partialUpdate(productTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-translations} : get all the productTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productTranslations in body.
     */
    @GetMapping("/product-translations")
    public ResponseEntity<List<ProductTranslationDTO>> getAllProductTranslations(ProductTranslationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductTranslations by criteria: {}", criteria);
        Page<ProductTranslationDTO> page = productTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-translations/count} : count all the productTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-translations/count")
    public ResponseEntity<Long> countProductTranslations(ProductTranslationCriteria criteria) {
        log.debug("REST request to count ProductTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(productTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-translations/:id} : get the "id" productTranslation.
     *
     * @param id the id of the productTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-translations/{id}")
    public ResponseEntity<ProductTranslationDTO> getProductTranslation(@PathVariable Long id) {
        log.debug("REST request to get ProductTranslation : {}", id);
        Optional<ProductTranslationDTO> productTranslationDTO = productTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTranslationDTO);
    }

    /**
     * {@code DELETE  /product-translations/:id} : delete the "id" productTranslation.
     *
     * @param id the id of the productTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-translations/{id}")
    public ResponseEntity<Void> deleteProductTranslation(@PathVariable Long id) {
        log.debug("REST request to delete ProductTranslation : {}", id);
        productTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
