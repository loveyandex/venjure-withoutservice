package com.venjure.web.rest;

import com.venjure.repository.CustomerGroupRepository;
import com.venjure.service.CustomerGroupQueryService;
import com.venjure.service.CustomerGroupService;
import com.venjure.service.criteria.CustomerGroupCriteria;
import com.venjure.service.dto.CustomerGroupDTO;
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
 * REST controller for managing {@link com.venjure.domain.CustomerGroup}.
 */
@RestController
@RequestMapping("/api")
public class CustomerGroupResource {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupResource.class);

    private static final String ENTITY_NAME = "customerGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerGroupService customerGroupService;

    private final CustomerGroupRepository customerGroupRepository;

    private final CustomerGroupQueryService customerGroupQueryService;

    public CustomerGroupResource(
        CustomerGroupService customerGroupService,
        CustomerGroupRepository customerGroupRepository,
        CustomerGroupQueryService customerGroupQueryService
    ) {
        this.customerGroupService = customerGroupService;
        this.customerGroupRepository = customerGroupRepository;
        this.customerGroupQueryService = customerGroupQueryService;
    }

    /**
     * {@code POST  /customer-groups} : Create a new customerGroup.
     *
     * @param customerGroupDTO the customerGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerGroupDTO, or with status {@code 400 (Bad Request)} if the customerGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-groups")
    public ResponseEntity<CustomerGroupDTO> createCustomerGroup(@Valid @RequestBody CustomerGroupDTO customerGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerGroup : {}", customerGroupDTO);
        if (customerGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerGroupDTO result = customerGroupService.save(customerGroupDTO);
        return ResponseEntity
            .created(new URI("/api/customer-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-groups/:id} : Updates an existing customerGroup.
     *
     * @param id the id of the customerGroupDTO to save.
     * @param customerGroupDTO the customerGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerGroupDTO,
     * or with status {@code 400 (Bad Request)} if the customerGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-groups/{id}")
    public ResponseEntity<CustomerGroupDTO> updateCustomerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerGroupDTO customerGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerGroup : {}, {}", id, customerGroupDTO);
        if (customerGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerGroupDTO result = customerGroupService.save(customerGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-groups/:id} : Partial updates given fields of an existing customerGroup, field will ignore if it is null
     *
     * @param id the id of the customerGroupDTO to save.
     * @param customerGroupDTO the customerGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerGroupDTO,
     * or with status {@code 400 (Bad Request)} if the customerGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CustomerGroupDTO> partialUpdateCustomerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerGroupDTO customerGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerGroup partially : {}, {}", id, customerGroupDTO);
        if (customerGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerGroupDTO> result = customerGroupService.partialUpdate(customerGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-groups} : get all the customerGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerGroups in body.
     */
    @GetMapping("/customer-groups")
    public ResponseEntity<List<CustomerGroupDTO>> getAllCustomerGroups(CustomerGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CustomerGroups by criteria: {}", criteria);
        Page<CustomerGroupDTO> page = customerGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-groups/count} : count all the customerGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-groups/count")
    public ResponseEntity<Long> countCustomerGroups(CustomerGroupCriteria criteria) {
        log.debug("REST request to count CustomerGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-groups/:id} : get the "id" customerGroup.
     *
     * @param id the id of the customerGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-groups/{id}")
    public ResponseEntity<CustomerGroupDTO> getCustomerGroup(@PathVariable Long id) {
        log.debug("REST request to get CustomerGroup : {}", id);
        Optional<CustomerGroupDTO> customerGroupDTO = customerGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerGroupDTO);
    }

    /**
     * {@code DELETE  /customer-groups/:id} : delete the "id" customerGroup.
     *
     * @param id the id of the customerGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-groups/{id}")
    public ResponseEntity<Void> deleteCustomerGroup(@PathVariable Long id) {
        log.debug("REST request to delete CustomerGroup : {}", id);
        customerGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
