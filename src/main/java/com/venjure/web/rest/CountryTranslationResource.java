package com.venjure.web.rest;

import com.venjure.repository.CountryTranslationRepository;
import com.venjure.service.CountryTranslationQueryService;
import com.venjure.service.CountryTranslationService;
import com.venjure.service.criteria.CountryTranslationCriteria;
import com.venjure.service.dto.CountryTranslationDTO;
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
 * REST controller for managing {@link com.venjure.domain.CountryTranslation}.
 */
@RestController
@RequestMapping("/api")
public class CountryTranslationResource {

    private final Logger log = LoggerFactory.getLogger(CountryTranslationResource.class);

    private static final String ENTITY_NAME = "countryTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryTranslationService countryTranslationService;

    private final CountryTranslationRepository countryTranslationRepository;

    private final CountryTranslationQueryService countryTranslationQueryService;

    public CountryTranslationResource(
        CountryTranslationService countryTranslationService,
        CountryTranslationRepository countryTranslationRepository,
        CountryTranslationQueryService countryTranslationQueryService
    ) {
        this.countryTranslationService = countryTranslationService;
        this.countryTranslationRepository = countryTranslationRepository;
        this.countryTranslationQueryService = countryTranslationQueryService;
    }

    /**
     * {@code POST  /country-translations} : Create a new countryTranslation.
     *
     * @param countryTranslationDTO the countryTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countryTranslationDTO, or with status {@code 400 (Bad Request)} if the countryTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/country-translations")
    public ResponseEntity<CountryTranslationDTO> createCountryTranslation(@Valid @RequestBody CountryTranslationDTO countryTranslationDTO)
        throws URISyntaxException {
        log.debug("REST request to save CountryTranslation : {}", countryTranslationDTO);
        if (countryTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new countryTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountryTranslationDTO result = countryTranslationService.save(countryTranslationDTO);
        return ResponseEntity
            .created(new URI("/api/country-translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /country-translations/:id} : Updates an existing countryTranslation.
     *
     * @param id the id of the countryTranslationDTO to save.
     * @param countryTranslationDTO the countryTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the countryTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countryTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/country-translations/{id}")
    public ResponseEntity<CountryTranslationDTO> updateCountryTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountryTranslationDTO countryTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CountryTranslation : {}, {}", id, countryTranslationDTO);
        if (countryTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountryTranslationDTO result = countryTranslationService.save(countryTranslationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryTranslationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /country-translations/:id} : Partial updates given fields of an existing countryTranslation, field will ignore if it is null
     *
     * @param id the id of the countryTranslationDTO to save.
     * @param countryTranslationDTO the countryTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the countryTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countryTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countryTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/country-translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CountryTranslationDTO> partialUpdateCountryTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountryTranslationDTO countryTranslationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountryTranslation partially : {}, {}", id, countryTranslationDTO);
        if (countryTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountryTranslationDTO> result = countryTranslationService.partialUpdate(countryTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /country-translations} : get all the countryTranslations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countryTranslations in body.
     */
    @GetMapping("/country-translations")
    public ResponseEntity<List<CountryTranslationDTO>> getAllCountryTranslations(CountryTranslationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountryTranslations by criteria: {}", criteria);
        Page<CountryTranslationDTO> page = countryTranslationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /country-translations/count} : count all the countryTranslations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/country-translations/count")
    public ResponseEntity<Long> countCountryTranslations(CountryTranslationCriteria criteria) {
        log.debug("REST request to count CountryTranslations by criteria: {}", criteria);
        return ResponseEntity.ok().body(countryTranslationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /country-translations/:id} : get the "id" countryTranslation.
     *
     * @param id the id of the countryTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countryTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/country-translations/{id}")
    public ResponseEntity<CountryTranslationDTO> getCountryTranslation(@PathVariable Long id) {
        log.debug("REST request to get CountryTranslation : {}", id);
        Optional<CountryTranslationDTO> countryTranslationDTO = countryTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countryTranslationDTO);
    }

    /**
     * {@code DELETE  /country-translations/:id} : delete the "id" countryTranslation.
     *
     * @param id the id of the countryTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/country-translations/{id}")
    public ResponseEntity<Void> deleteCountryTranslation(@PathVariable Long id) {
        log.debug("REST request to delete CountryTranslation : {}", id);
        countryTranslationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
