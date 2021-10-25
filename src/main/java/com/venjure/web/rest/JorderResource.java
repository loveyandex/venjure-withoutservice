package com.venjure.web.rest;

import com.venjure.repository.JorderRepository;
import com.venjure.service.JorderQueryService;
import com.venjure.service.JorderService;
import com.venjure.service.criteria.JorderCriteria;
import com.venjure.service.dto.JorderDTO;
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
 * REST controller for managing {@link com.venjure.domain.Jorder}.
 */
@RestController
@RequestMapping("/api")
public class JorderResource {

    private final Logger log = LoggerFactory.getLogger(JorderResource.class);

    private static final String ENTITY_NAME = "jorder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JorderService jorderService;

    private final JorderRepository jorderRepository;

    private final JorderQueryService jorderQueryService;

    public JorderResource(JorderService jorderService, JorderRepository jorderRepository, JorderQueryService jorderQueryService) {
        this.jorderService = jorderService;
        this.jorderRepository = jorderRepository;
        this.jorderQueryService = jorderQueryService;
    }

    /**
     * {@code POST  /jorders} : Create a new jorder.
     *
     * @param jorderDTO the jorderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jorderDTO, or with status {@code 400 (Bad Request)} if the jorder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jorders")
    public ResponseEntity<JorderDTO> createJorder(@Valid @RequestBody JorderDTO jorderDTO) throws URISyntaxException {
        log.debug("REST request to save Jorder : {}", jorderDTO);
        if (jorderDTO.getId() != null) {
            throw new BadRequestAlertException("A new jorder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JorderDTO result = jorderService.save(jorderDTO);
        return ResponseEntity
            .created(new URI("/api/jorders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jorders/:id} : Updates an existing jorder.
     *
     * @param id the id of the jorderDTO to save.
     * @param jorderDTO the jorderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jorderDTO,
     * or with status {@code 400 (Bad Request)} if the jorderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jorderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jorders/{id}")
    public ResponseEntity<JorderDTO> updateJorder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JorderDTO jorderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Jorder : {}, {}", id, jorderDTO);
        if (jorderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jorderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jorderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JorderDTO result = jorderService.save(jorderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jorderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /jorders/:id} : Partial updates given fields of an existing jorder, field will ignore if it is null
     *
     * @param id the id of the jorderDTO to save.
     * @param jorderDTO the jorderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jorderDTO,
     * or with status {@code 400 (Bad Request)} if the jorderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jorderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jorderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/jorders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JorderDTO> partialUpdateJorder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JorderDTO jorderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Jorder partially : {}, {}", id, jorderDTO);
        if (jorderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jorderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jorderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JorderDTO> result = jorderService.partialUpdate(jorderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jorderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /jorders} : get all the jorders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jorders in body.
     */
    @GetMapping("/jorders")
    public ResponseEntity<List<JorderDTO>> getAllJorders(JorderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Jorders by criteria: {}", criteria);
        Page<JorderDTO> page = jorderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jorders/count} : count all the jorders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/jorders/count")
    public ResponseEntity<Long> countJorders(JorderCriteria criteria) {
        log.debug("REST request to count Jorders by criteria: {}", criteria);
        return ResponseEntity.ok().body(jorderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /jorders/:id} : get the "id" jorder.
     *
     * @param id the id of the jorderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jorderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jorders/{id}")
    public ResponseEntity<JorderDTO> getJorder(@PathVariable Long id) {
        log.debug("REST request to get Jorder : {}", id);
        Optional<JorderDTO> jorderDTO = jorderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jorderDTO);
    }

    /**
     * {@code DELETE  /jorders/:id} : delete the "id" jorder.
     *
     * @param id the id of the jorderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jorders/{id}")
    public ResponseEntity<Void> deleteJorder(@PathVariable Long id) {
        log.debug("REST request to delete Jorder : {}", id);
        jorderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
