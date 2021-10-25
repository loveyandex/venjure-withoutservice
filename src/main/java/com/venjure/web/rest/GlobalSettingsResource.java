package com.venjure.web.rest;

import com.venjure.repository.GlobalSettingsRepository;
import com.venjure.service.GlobalSettingsQueryService;
import com.venjure.service.GlobalSettingsService;
import com.venjure.service.criteria.GlobalSettingsCriteria;
import com.venjure.service.dto.GlobalSettingsDTO;
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
 * REST controller for managing {@link com.venjure.domain.GlobalSettings}.
 */
@RestController
@RequestMapping("/api")
public class GlobalSettingsResource {

    private final Logger log = LoggerFactory.getLogger(GlobalSettingsResource.class);

    private static final String ENTITY_NAME = "globalSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalSettingsService globalSettingsService;

    private final GlobalSettingsRepository globalSettingsRepository;

    private final GlobalSettingsQueryService globalSettingsQueryService;

    public GlobalSettingsResource(
        GlobalSettingsService globalSettingsService,
        GlobalSettingsRepository globalSettingsRepository,
        GlobalSettingsQueryService globalSettingsQueryService
    ) {
        this.globalSettingsService = globalSettingsService;
        this.globalSettingsRepository = globalSettingsRepository;
        this.globalSettingsQueryService = globalSettingsQueryService;
    }

    /**
     * {@code POST  /global-settings} : Create a new globalSettings.
     *
     * @param globalSettingsDTO the globalSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalSettingsDTO, or with status {@code 400 (Bad Request)} if the globalSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/global-settings")
    public ResponseEntity<GlobalSettingsDTO> createGlobalSettings(@Valid @RequestBody GlobalSettingsDTO globalSettingsDTO)
        throws URISyntaxException {
        log.debug("REST request to save GlobalSettings : {}", globalSettingsDTO);
        if (globalSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalSettingsDTO result = globalSettingsService.save(globalSettingsDTO);
        return ResponseEntity
            .created(new URI("/api/global-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /global-settings/:id} : Updates an existing globalSettings.
     *
     * @param id the id of the globalSettingsDTO to save.
     * @param globalSettingsDTO the globalSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the globalSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/global-settings/{id}")
    public ResponseEntity<GlobalSettingsDTO> updateGlobalSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GlobalSettingsDTO globalSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GlobalSettings : {}, {}", id, globalSettingsDTO);
        if (globalSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GlobalSettingsDTO result = globalSettingsService.save(globalSettingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /global-settings/:id} : Partial updates given fields of an existing globalSettings, field will ignore if it is null
     *
     * @param id the id of the globalSettingsDTO to save.
     * @param globalSettingsDTO the globalSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the globalSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the globalSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the globalSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/global-settings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GlobalSettingsDTO> partialUpdateGlobalSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GlobalSettingsDTO globalSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlobalSettings partially : {}, {}", id, globalSettingsDTO);
        if (globalSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlobalSettingsDTO> result = globalSettingsService.partialUpdate(globalSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /global-settings} : get all the globalSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalSettings in body.
     */
    @GetMapping("/global-settings")
    public ResponseEntity<List<GlobalSettingsDTO>> getAllGlobalSettings(GlobalSettingsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GlobalSettings by criteria: {}", criteria);
        Page<GlobalSettingsDTO> page = globalSettingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /global-settings/count} : count all the globalSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/global-settings/count")
    public ResponseEntity<Long> countGlobalSettings(GlobalSettingsCriteria criteria) {
        log.debug("REST request to count GlobalSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(globalSettingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /global-settings/:id} : get the "id" globalSettings.
     *
     * @param id the id of the globalSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/global-settings/{id}")
    public ResponseEntity<GlobalSettingsDTO> getGlobalSettings(@PathVariable Long id) {
        log.debug("REST request to get GlobalSettings : {}", id);
        Optional<GlobalSettingsDTO> globalSettingsDTO = globalSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalSettingsDTO);
    }

    /**
     * {@code DELETE  /global-settings/:id} : delete the "id" globalSettings.
     *
     * @param id the id of the globalSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/global-settings/{id}")
    public ResponseEntity<Void> deleteGlobalSettings(@PathVariable Long id) {
        log.debug("REST request to delete GlobalSettings : {}", id);
        globalSettingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
