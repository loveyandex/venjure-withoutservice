package com.venjure.web.rest;

import com.venjure.repository.ExampleEntityRepository;
import com.venjure.service.ExampleEntityQueryService;
import com.venjure.service.ExampleEntityService;
import com.venjure.service.criteria.ExampleEntityCriteria;
import com.venjure.service.dto.ExampleEntityDTO;
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
 * REST controller for managing {@link com.venjure.domain.ExampleEntity}.
 */
@RestController
@RequestMapping("/api")
public class ExampleEntityResource {

    private final Logger log = LoggerFactory.getLogger(ExampleEntityResource.class);

    private static final String ENTITY_NAME = "exampleEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExampleEntityService exampleEntityService;

    private final ExampleEntityRepository exampleEntityRepository;

    private final ExampleEntityQueryService exampleEntityQueryService;

    public ExampleEntityResource(
        ExampleEntityService exampleEntityService,
        ExampleEntityRepository exampleEntityRepository,
        ExampleEntityQueryService exampleEntityQueryService
    ) {
        this.exampleEntityService = exampleEntityService;
        this.exampleEntityRepository = exampleEntityRepository;
        this.exampleEntityQueryService = exampleEntityQueryService;
    }

    /**
     * {@code POST  /example-entities} : Create a new exampleEntity.
     *
     * @param exampleEntityDTO the exampleEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exampleEntityDTO, or with status {@code 400 (Bad Request)} if the exampleEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/example-entities")
    public ResponseEntity<ExampleEntityDTO> createExampleEntity(@Valid @RequestBody ExampleEntityDTO exampleEntityDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExampleEntity : {}", exampleEntityDTO);
        if (exampleEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new exampleEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExampleEntityDTO result = exampleEntityService.save(exampleEntityDTO);
        return ResponseEntity
            .created(new URI("/api/example-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /example-entities/:id} : Updates an existing exampleEntity.
     *
     * @param id the id of the exampleEntityDTO to save.
     * @param exampleEntityDTO the exampleEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exampleEntityDTO,
     * or with status {@code 400 (Bad Request)} if the exampleEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exampleEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/example-entities/{id}")
    public ResponseEntity<ExampleEntityDTO> updateExampleEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExampleEntityDTO exampleEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExampleEntity : {}, {}", id, exampleEntityDTO);
        if (exampleEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exampleEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exampleEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExampleEntityDTO result = exampleEntityService.save(exampleEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exampleEntityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /example-entities/:id} : Partial updates given fields of an existing exampleEntity, field will ignore if it is null
     *
     * @param id the id of the exampleEntityDTO to save.
     * @param exampleEntityDTO the exampleEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exampleEntityDTO,
     * or with status {@code 400 (Bad Request)} if the exampleEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exampleEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exampleEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/example-entities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExampleEntityDTO> partialUpdateExampleEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExampleEntityDTO exampleEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExampleEntity partially : {}, {}", id, exampleEntityDTO);
        if (exampleEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exampleEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exampleEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExampleEntityDTO> result = exampleEntityService.partialUpdate(exampleEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exampleEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /example-entities} : get all the exampleEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exampleEntities in body.
     */
    @GetMapping("/example-entities")
    public ResponseEntity<List<ExampleEntityDTO>> getAllExampleEntities(ExampleEntityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExampleEntities by criteria: {}", criteria);
        Page<ExampleEntityDTO> page = exampleEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /example-entities/count} : count all the exampleEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/example-entities/count")
    public ResponseEntity<Long> countExampleEntities(ExampleEntityCriteria criteria) {
        log.debug("REST request to count ExampleEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(exampleEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /example-entities/:id} : get the "id" exampleEntity.
     *
     * @param id the id of the exampleEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exampleEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/example-entities/{id}")
    public ResponseEntity<ExampleEntityDTO> getExampleEntity(@PathVariable Long id) {
        log.debug("REST request to get ExampleEntity : {}", id);
        Optional<ExampleEntityDTO> exampleEntityDTO = exampleEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exampleEntityDTO);
    }

    /**
     * {@code DELETE  /example-entities/:id} : delete the "id" exampleEntity.
     *
     * @param id the id of the exampleEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/example-entities/{id}")
    public ResponseEntity<Void> deleteExampleEntity(@PathVariable Long id) {
        log.debug("REST request to delete ExampleEntity : {}", id);
        exampleEntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
