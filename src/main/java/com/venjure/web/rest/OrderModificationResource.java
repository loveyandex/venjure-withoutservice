package com.venjure.web.rest;

import com.venjure.repository.OrderModificationRepository;
import com.venjure.service.OrderModificationQueryService;
import com.venjure.service.OrderModificationService;
import com.venjure.service.criteria.OrderModificationCriteria;
import com.venjure.service.dto.OrderModificationDTO;
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
 * REST controller for managing {@link com.venjure.domain.OrderModification}.
 */
@RestController
@RequestMapping("/api")
public class OrderModificationResource {

    private final Logger log = LoggerFactory.getLogger(OrderModificationResource.class);

    private static final String ENTITY_NAME = "orderModification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderModificationService orderModificationService;

    private final OrderModificationRepository orderModificationRepository;

    private final OrderModificationQueryService orderModificationQueryService;

    public OrderModificationResource(
        OrderModificationService orderModificationService,
        OrderModificationRepository orderModificationRepository,
        OrderModificationQueryService orderModificationQueryService
    ) {
        this.orderModificationService = orderModificationService;
        this.orderModificationRepository = orderModificationRepository;
        this.orderModificationQueryService = orderModificationQueryService;
    }

    /**
     * {@code POST  /order-modifications} : Create a new orderModification.
     *
     * @param orderModificationDTO the orderModificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderModificationDTO, or with status {@code 400 (Bad Request)} if the orderModification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-modifications")
    public ResponseEntity<OrderModificationDTO> createOrderModification(@Valid @RequestBody OrderModificationDTO orderModificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrderModification : {}", orderModificationDTO);
        if (orderModificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderModification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderModificationDTO result = orderModificationService.save(orderModificationDTO);
        return ResponseEntity
            .created(new URI("/api/order-modifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-modifications/:id} : Updates an existing orderModification.
     *
     * @param id the id of the orderModificationDTO to save.
     * @param orderModificationDTO the orderModificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderModificationDTO,
     * or with status {@code 400 (Bad Request)} if the orderModificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderModificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-modifications/{id}")
    public ResponseEntity<OrderModificationDTO> updateOrderModification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderModificationDTO orderModificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderModification : {}, {}", id, orderModificationDTO);
        if (orderModificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderModificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderModificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderModificationDTO result = orderModificationService.save(orderModificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderModificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-modifications/:id} : Partial updates given fields of an existing orderModification, field will ignore if it is null
     *
     * @param id the id of the orderModificationDTO to save.
     * @param orderModificationDTO the orderModificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderModificationDTO,
     * or with status {@code 400 (Bad Request)} if the orderModificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderModificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderModificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-modifications/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderModificationDTO> partialUpdateOrderModification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderModificationDTO orderModificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderModification partially : {}, {}", id, orderModificationDTO);
        if (orderModificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderModificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderModificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderModificationDTO> result = orderModificationService.partialUpdate(orderModificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderModificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-modifications} : get all the orderModifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderModifications in body.
     */
    @GetMapping("/order-modifications")
    public ResponseEntity<List<OrderModificationDTO>> getAllOrderModifications(OrderModificationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderModifications by criteria: {}", criteria);
        Page<OrderModificationDTO> page = orderModificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-modifications/count} : count all the orderModifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/order-modifications/count")
    public ResponseEntity<Long> countOrderModifications(OrderModificationCriteria criteria) {
        log.debug("REST request to count OrderModifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderModificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-modifications/:id} : get the "id" orderModification.
     *
     * @param id the id of the orderModificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderModificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-modifications/{id}")
    public ResponseEntity<OrderModificationDTO> getOrderModification(@PathVariable Long id) {
        log.debug("REST request to get OrderModification : {}", id);
        Optional<OrderModificationDTO> orderModificationDTO = orderModificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderModificationDTO);
    }

    /**
     * {@code DELETE  /order-modifications/:id} : delete the "id" orderModification.
     *
     * @param id the id of the orderModificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-modifications/{id}")
    public ResponseEntity<Void> deleteOrderModification(@PathVariable Long id) {
        log.debug("REST request to delete OrderModification : {}", id);
        orderModificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
