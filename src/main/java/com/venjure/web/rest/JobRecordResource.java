package com.venjure.web.rest;

import com.venjure.repository.JobRecordRepository;
import com.venjure.service.JobRecordQueryService;
import com.venjure.service.JobRecordService;
import com.venjure.service.criteria.JobRecordCriteria;
import com.venjure.service.dto.JobRecordDTO;
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
 * REST controller for managing {@link com.venjure.domain.JobRecord}.
 */
@RestController
@RequestMapping("/api")
public class JobRecordResource {

    private final Logger log = LoggerFactory.getLogger(JobRecordResource.class);

    private static final String ENTITY_NAME = "jobRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRecordService jobRecordService;

    private final JobRecordRepository jobRecordRepository;

    private final JobRecordQueryService jobRecordQueryService;

    public JobRecordResource(
        JobRecordService jobRecordService,
        JobRecordRepository jobRecordRepository,
        JobRecordQueryService jobRecordQueryService
    ) {
        this.jobRecordService = jobRecordService;
        this.jobRecordRepository = jobRecordRepository;
        this.jobRecordQueryService = jobRecordQueryService;
    }

    /**
     * {@code POST  /job-records} : Create a new jobRecord.
     *
     * @param jobRecordDTO the jobRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobRecordDTO, or with status {@code 400 (Bad Request)} if the jobRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-records")
    public ResponseEntity<JobRecordDTO> createJobRecord(@Valid @RequestBody JobRecordDTO jobRecordDTO) throws URISyntaxException {
        log.debug("REST request to save JobRecord : {}", jobRecordDTO);
        if (jobRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobRecordDTO result = jobRecordService.save(jobRecordDTO);
        return ResponseEntity
            .created(new URI("/api/job-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-records/:id} : Updates an existing jobRecord.
     *
     * @param id the id of the jobRecordDTO to save.
     * @param jobRecordDTO the jobRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobRecordDTO,
     * or with status {@code 400 (Bad Request)} if the jobRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-records/{id}")
    public ResponseEntity<JobRecordDTO> updateJobRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobRecordDTO jobRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobRecord : {}, {}", id, jobRecordDTO);
        if (jobRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobRecordDTO result = jobRecordService.save(jobRecordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-records/:id} : Partial updates given fields of an existing jobRecord, field will ignore if it is null
     *
     * @param id the id of the jobRecordDTO to save.
     * @param jobRecordDTO the jobRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobRecordDTO,
     * or with status {@code 400 (Bad Request)} if the jobRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-records/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JobRecordDTO> partialUpdateJobRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobRecordDTO jobRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobRecord partially : {}, {}", id, jobRecordDTO);
        if (jobRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobRecordDTO> result = jobRecordService.partialUpdate(jobRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-records} : get all the jobRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobRecords in body.
     */
    @GetMapping("/job-records")
    public ResponseEntity<List<JobRecordDTO>> getAllJobRecords(JobRecordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobRecords by criteria: {}", criteria);
        Page<JobRecordDTO> page = jobRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-records/count} : count all the jobRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-records/count")
    public ResponseEntity<Long> countJobRecords(JobRecordCriteria criteria) {
        log.debug("REST request to count JobRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-records/:id} : get the "id" jobRecord.
     *
     * @param id the id of the jobRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-records/{id}")
    public ResponseEntity<JobRecordDTO> getJobRecord(@PathVariable Long id) {
        log.debug("REST request to get JobRecord : {}", id);
        Optional<JobRecordDTO> jobRecordDTO = jobRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobRecordDTO);
    }

    /**
     * {@code DELETE  /job-records/:id} : delete the "id" jobRecord.
     *
     * @param id the id of the jobRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-records/{id}")
    public ResponseEntity<Void> deleteJobRecord(@PathVariable Long id) {
        log.debug("REST request to delete JobRecord : {}", id);
        jobRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
