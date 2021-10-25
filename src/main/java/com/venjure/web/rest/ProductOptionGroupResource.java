package com.venjure.web.rest;

import com.venjure.repository.ProductOptionGroupRepository;
import com.venjure.service.ProductOptionGroupQueryService;
import com.venjure.service.ProductOptionGroupService;
import com.venjure.service.criteria.ProductOptionGroupCriteria;
import com.venjure.service.dto.ProductOptionGroupDTO;
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
 * REST controller for managing {@link com.venjure.domain.ProductOptionGroup}.
 */
@RestController
@RequestMapping("/api")
public class ProductOptionGroupResource {

    private final Logger log = LoggerFactory.getLogger(ProductOptionGroupResource.class);

    private static final String ENTITY_NAME = "productOptionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOptionGroupService productOptionGroupService;

    private final ProductOptionGroupRepository productOptionGroupRepository;

    private final ProductOptionGroupQueryService productOptionGroupQueryService;

    public ProductOptionGroupResource(
        ProductOptionGroupService productOptionGroupService,
        ProductOptionGroupRepository productOptionGroupRepository,
        ProductOptionGroupQueryService productOptionGroupQueryService
    ) {
        this.productOptionGroupService = productOptionGroupService;
        this.productOptionGroupRepository = productOptionGroupRepository;
        this.productOptionGroupQueryService = productOptionGroupQueryService;
    }

    /**
     * {@code POST  /product-option-groups} : Create a new productOptionGroup.
     *
     * @param productOptionGroupDTO the productOptionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOptionGroupDTO, or with status {@code 400 (Bad Request)} if the productOptionGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-option-groups")
    public ResponseEntity<ProductOptionGroupDTO> createProductOptionGroup(@Valid @RequestBody ProductOptionGroupDTO productOptionGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductOptionGroup : {}", productOptionGroupDTO);
        if (productOptionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOptionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOptionGroupDTO result = productOptionGroupService.save(productOptionGroupDTO);
        return ResponseEntity
            .created(new URI("/api/product-option-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-option-groups/:id} : Updates an existing productOptionGroup.
     *
     * @param id the id of the productOptionGroupDTO to save.
     * @param productOptionGroupDTO the productOptionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOptionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-option-groups/{id}")
    public ResponseEntity<ProductOptionGroupDTO> updateProductOptionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductOptionGroupDTO productOptionGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductOptionGroup : {}, {}", id, productOptionGroupDTO);
        if (productOptionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductOptionGroupDTO result = productOptionGroupService.save(productOptionGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-option-groups/:id} : Partial updates given fields of an existing productOptionGroup, field will ignore if it is null
     *
     * @param id the id of the productOptionGroupDTO to save.
     * @param productOptionGroupDTO the productOptionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOptionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the productOptionGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productOptionGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOptionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-option-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductOptionGroupDTO> partialUpdateProductOptionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductOptionGroupDTO productOptionGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductOptionGroup partially : {}, {}", id, productOptionGroupDTO);
        if (productOptionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOptionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOptionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductOptionGroupDTO> result = productOptionGroupService.partialUpdate(productOptionGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOptionGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-option-groups} : get all the productOptionGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOptionGroups in body.
     */
    @GetMapping("/product-option-groups")
    public ResponseEntity<List<ProductOptionGroupDTO>> getAllProductOptionGroups(ProductOptionGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductOptionGroups by criteria: {}", criteria);
        Page<ProductOptionGroupDTO> page = productOptionGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-option-groups/count} : count all the productOptionGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-option-groups/count")
    public ResponseEntity<Long> countProductOptionGroups(ProductOptionGroupCriteria criteria) {
        log.debug("REST request to count ProductOptionGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(productOptionGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-option-groups/:id} : get the "id" productOptionGroup.
     *
     * @param id the id of the productOptionGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOptionGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-option-groups/{id}")
    public ResponseEntity<ProductOptionGroupDTO> getProductOptionGroup(@PathVariable Long id) {
        log.debug("REST request to get ProductOptionGroup : {}", id);
        Optional<ProductOptionGroupDTO> productOptionGroupDTO = productOptionGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOptionGroupDTO);
    }

    /**
     * {@code DELETE  /product-option-groups/:id} : delete the "id" productOptionGroup.
     *
     * @param id the id of the productOptionGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-option-groups/{id}")
    public ResponseEntity<Void> deleteProductOptionGroup(@PathVariable Long id) {
        log.debug("REST request to delete ProductOptionGroup : {}", id);
        productOptionGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
