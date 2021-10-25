package com.venjure.web.rest;

import com.venjure.repository.PogtRepository;
import com.venjure.service.PogtQueryService;
import com.venjure.service.PogtService;
import com.venjure.service.criteria.PogtCriteria;
import com.venjure.service.dto.PogtDTO;
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
 * REST controller for managing {@link com.venjure.domain.Pogt}.
 */
@RestController
@RequestMapping("/api")
public class PogtResource {

    private final Logger log = LoggerFactory.getLogger(PogtResource.class);

    private static final String ENTITY_NAME = "pogt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PogtService pogtService;

    private final PogtRepository pogtRepository;

    private final PogtQueryService pogtQueryService;

    public PogtResource(PogtService pogtService, PogtRepository pogtRepository, PogtQueryService pogtQueryService) {
        this.pogtService = pogtService;
        this.pogtRepository = pogtRepository;
        this.pogtQueryService = pogtQueryService;
    }

    /**
     * {@code POST  /pogts} : Create a new pogt.
     *
     * @param pogtDTO the pogtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pogtDTO, or with status {@code 400 (Bad Request)} if the pogt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pogts")
    public ResponseEntity<PogtDTO> createPogt(@Valid @RequestBody PogtDTO pogtDTO) throws URISyntaxException {
        log.debug("REST request to save Pogt : {}", pogtDTO);
        if (pogtDTO.getId() != null) {
            throw new BadRequestAlertException("A new pogt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PogtDTO result = pogtService.save(pogtDTO);
        return ResponseEntity
            .created(new URI("/api/pogts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pogts/:id} : Updates an existing pogt.
     *
     * @param id the id of the pogtDTO to save.
     * @param pogtDTO the pogtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pogtDTO,
     * or with status {@code 400 (Bad Request)} if the pogtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pogtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pogts/{id}")
    public ResponseEntity<PogtDTO> updatePogt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PogtDTO pogtDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pogt : {}, {}", id, pogtDTO);
        if (pogtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pogtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pogtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PogtDTO result = pogtService.save(pogtDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pogtDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pogts/:id} : Partial updates given fields of an existing pogt, field will ignore if it is null
     *
     * @param id the id of the pogtDTO to save.
     * @param pogtDTO the pogtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pogtDTO,
     * or with status {@code 400 (Bad Request)} if the pogtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pogtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pogtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pogts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PogtDTO> partialUpdatePogt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PogtDTO pogtDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pogt partially : {}, {}", id, pogtDTO);
        if (pogtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pogtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pogtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PogtDTO> result = pogtService.partialUpdate(pogtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pogtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pogts} : get all the pogts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pogts in body.
     */
    @GetMapping("/pogts")
    public ResponseEntity<List<PogtDTO>> getAllPogts(PogtCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pogts by criteria: {}", criteria);
        Page<PogtDTO> page = pogtQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pogts/count} : count all the pogts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pogts/count")
    public ResponseEntity<Long> countPogts(PogtCriteria criteria) {
        log.debug("REST request to count Pogts by criteria: {}", criteria);
        return ResponseEntity.ok().body(pogtQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pogts/:id} : get the "id" pogt.
     *
     * @param id the id of the pogtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pogtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pogts/{id}")
    public ResponseEntity<PogtDTO> getPogt(@PathVariable Long id) {
        log.debug("REST request to get Pogt : {}", id);
        Optional<PogtDTO> pogtDTO = pogtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pogtDTO);
    }

    /**
     * {@code DELETE  /pogts/:id} : delete the "id" pogt.
     *
     * @param id the id of the pogtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pogts/{id}")
    public ResponseEntity<Void> deletePogt(@PathVariable Long id) {
        log.debug("REST request to delete Pogt : {}", id);
        pogtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
