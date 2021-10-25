package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Country;
import com.venjure.domain.CountryTranslation;
import com.venjure.domain.Zone;
import com.venjure.repository.CountryRepository;
import com.venjure.service.criteria.CountryCriteria;
import com.venjure.service.dto.CountryDTO;
import com.venjure.service.mapper.CountryMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .code(DEFAULT_CODE)
            .enabled(DEFAULT_ENABLED);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .enabled(UPDATED_ENABLED);
        return country;
    }

    @BeforeEach
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCountry.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCountry.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCountry.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCreatedat(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setUpdatedat(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setEnabled(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        Long id = country.getId();

        defaultCountryShouldBeFound("id.equals=" + id);
        defaultCountryShouldNotBeFound("id.notEquals=" + id);

        defaultCountryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.greaterThan=" + id);

        defaultCountryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where createdat equals to DEFAULT_CREATEDAT
        defaultCountryShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the countryList where createdat equals to UPDATED_CREATEDAT
        defaultCountryShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where createdat not equals to DEFAULT_CREATEDAT
        defaultCountryShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the countryList where createdat not equals to UPDATED_CREATEDAT
        defaultCountryShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCountryShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the countryList where createdat equals to UPDATED_CREATEDAT
        defaultCountryShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where createdat is not null
        defaultCountryShouldBeFound("createdat.specified=true");

        // Get all the countryList where createdat is null
        defaultCountryShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCountryShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the countryList where updatedat equals to UPDATED_UPDATEDAT
        defaultCountryShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCountryShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the countryList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCountryShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCountryShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the countryList where updatedat equals to UPDATED_UPDATEDAT
        defaultCountryShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountriesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where updatedat is not null
        defaultCountryShouldBeFound("updatedat.specified=true");

        // Get all the countryList where updatedat is null
        defaultCountryShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code equals to DEFAULT_CODE
        defaultCountryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the countryList where code equals to UPDATED_CODE
        defaultCountryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code not equals to DEFAULT_CODE
        defaultCountryShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the countryList where code not equals to UPDATED_CODE
        defaultCountryShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCountryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the countryList where code equals to UPDATED_CODE
        defaultCountryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code is not null
        defaultCountryShouldBeFound("code.specified=true");

        // Get all the countryList where code is null
        defaultCountryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code contains DEFAULT_CODE
        defaultCountryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the countryList where code contains UPDATED_CODE
        defaultCountryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where code does not contain DEFAULT_CODE
        defaultCountryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the countryList where code does not contain UPDATED_CODE
        defaultCountryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where enabled equals to DEFAULT_ENABLED
        defaultCountryShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the countryList where enabled equals to UPDATED_ENABLED
        defaultCountryShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllCountriesByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where enabled not equals to DEFAULT_ENABLED
        defaultCountryShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the countryList where enabled not equals to UPDATED_ENABLED
        defaultCountryShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllCountriesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultCountryShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the countryList where enabled equals to UPDATED_ENABLED
        defaultCountryShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllCountriesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where enabled is not null
        defaultCountryShouldBeFound("enabled.specified=true");

        // Get all the countryList where enabled is null
        defaultCountryShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        CountryTranslation countryTranslation = CountryTranslationResourceIT.createEntity(em);
        em.persist(countryTranslation);
        em.flush();
        country.addCountryTranslation(countryTranslation);
        countryRepository.saveAndFlush(country);
        Long countryTranslationId = countryTranslation.getId();

        // Get all the countryList where countryTranslation equals to countryTranslationId
        defaultCountryShouldBeFound("countryTranslationId.equals=" + countryTranslationId);

        // Get all the countryList where countryTranslation equals to (countryTranslationId + 1)
        defaultCountryShouldNotBeFound("countryTranslationId.equals=" + (countryTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllCountriesByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        Zone zone = ZoneResourceIT.createEntity(em);
        em.persist(zone);
        em.flush();
        country.addZone(zone);
        countryRepository.saveAndFlush(country);
        Long zoneId = zone.getId();

        // Get all the countryList where zone equals to zoneId
        defaultCountryShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the countryList where zone equals to (zoneId + 1)
        defaultCountryShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).code(UPDATED_CODE).enabled(UPDATED_ENABLED);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountry.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCountry.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCountry.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.createdat(UPDATED_CREATEDAT).enabled(UPDATED_ENABLED);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountry.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCountry.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCountry.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).code(UPDATED_CODE).enabled(UPDATED_ENABLED);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountry.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCountry.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCountry.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
