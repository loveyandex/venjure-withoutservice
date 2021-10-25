package com.venjure.web.rest;

import com.venjure.repository.HistoryEntryRepository;
import com.venjure.service.HistoryEntryQueryService;
import com.venjure.service.HistoryEntryService;
import com.venjure.service.criteria.HistoryEntryCriteria;
import com.venjure.service.dto.HistoryEntryDTO;
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
 * REST controller for managing {@link com.venjure.domain.HistoryEntry}.
 */
@RestController
@RequestMapping("/api")
public class HistoryEntryResource {

    private final Logger log = LoggerFactory.getLogger(HistoryEntryResource.class);

    private static final String ENTITY_NAME = "historyEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoryEntryService historyEntryService;

    private final HistoryEntryRepository historyEntryRepository;

    private final HistoryEntryQueryService historyEntryQueryService;

    public HistoryEntryResource(
        HistoryEntryService historyEntryService,
        HistoryEntryRepository historyEntryRepository,
        HistoryEntryQueryService historyEntryQueryService
    ) {
        this.historyEntryService = historyEntryService;
        this.historyEntryRepository = historyEntryRepository;
        this.historyEntryQueryService = historyEntryQueryService;
    }

    /**
     * {@code POST  /history-entries} : Create a new historyEntry.
     *
     * @param historyEntryDTO the historyEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historyEntryDTO, or with status {@code 400 (Bad Request)} if the historyEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/history-entries")
    public ResponseEntity<HistoryEntryDTO> createHistoryEntry(@Valid @RequestBody HistoryEntryDTO historyEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save HistoryEntry : {}", historyEntryDTO);
        if (historyEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new historyEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoryEntryDTO result = historyEntryService.save(historyEntryDTO);
        return ResponseEntity
            .created(new URI("/api/history-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /history-entries/:id} : Updates an existing historyEntry.
     *
     * @param id the id of the historyEntryDTO to save.
     * @param historyEntryDTO the historyEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyEntryDTO,
     * or with status {@code 400 (Bad Request)} if the historyEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historyEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/history-entries/{id}")
    public ResponseEntity<HistoryEntryDTO> updateHistoryEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoryEntryDTO historyEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoryEntry : {}, {}", id, historyEntryDTO);
        if (historyEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoryEntryDTO result = historyEntryService.save(historyEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /history-entries/:id} : Partial updates given fields of an existing historyEntry, field will ignore if it is null
     *
     * @param id the id of the historyEntryDTO to save.
     * @param historyEntryDTO the historyEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyEntryDTO,
     * or with status {@code 400 (Bad Request)} if the historyEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historyEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historyEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/history-entries/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HistoryEntryDTO> partialUpdateHistoryEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoryEntryDTO historyEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoryEntry partially : {}, {}", id, historyEntryDTO);
        if (historyEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoryEntryDTO> result = historyEntryService.partialUpdate(historyEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /history-entries} : get all the historyEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historyEntries in body.
     */
    @GetMapping("/history-entries")
    public ResponseEntity<List<HistoryEntryDTO>> getAllHistoryEntries(HistoryEntryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HistoryEntries by criteria: {}", criteria);
        Page<HistoryEntryDTO> page = historyEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /history-entries/count} : count all the historyEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/history-entries/count")
    public ResponseEntity<Long> countHistoryEntries(HistoryEntryCriteria criteria) {
        log.debug("REST request to count HistoryEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(historyEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /history-entries/:id} : get the "id" historyEntry.
     *
     * @param id the id of the historyEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historyEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/history-entries/{id}")
    public ResponseEntity<HistoryEntryDTO> getHistoryEntry(@PathVariable Long id) {
        log.debug("REST request to get HistoryEntry : {}", id);
        Optional<HistoryEntryDTO> historyEntryDTO = historyEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyEntryDTO);
    }

    /**
     * {@code DELETE  /history-entries/:id} : delete the "id" historyEntry.
     *
     * @param id the id of the historyEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/history-entries/{id}")
    public ResponseEntity<Void> deleteHistoryEntry(@PathVariable Long id) {
        log.debug("REST request to delete HistoryEntry : {}", id);
        historyEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
