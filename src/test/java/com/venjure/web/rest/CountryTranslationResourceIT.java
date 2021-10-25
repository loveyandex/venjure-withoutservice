package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Country;
import com.venjure.domain.CountryTranslation;
import com.venjure.repository.CountryTranslationRepository;
import com.venjure.service.criteria.CountryTranslationCriteria;
import com.venjure.service.dto.CountryTranslationDTO;
import com.venjure.service.mapper.CountryTranslationMapper;
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
 * Integration tests for the {@link CountryTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/country-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryTranslationRepository countryTranslationRepository;

    @Autowired
    private CountryTranslationMapper countryTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryTranslationMockMvc;

    private CountryTranslation countryTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryTranslation createEntity(EntityManager em) {
        CountryTranslation countryTranslation = new CountryTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return countryTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryTranslation createUpdatedEntity(EntityManager em) {
        CountryTranslation countryTranslation = new CountryTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return countryTranslation;
    }

    @BeforeEach
    public void initTest() {
        countryTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createCountryTranslation() throws Exception {
        int databaseSizeBeforeCreate = countryTranslationRepository.findAll().size();
        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);
        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        CountryTranslation testCountryTranslation = countryTranslationList.get(countryTranslationList.size() - 1);
        assertThat(testCountryTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCountryTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCountryTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testCountryTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCountryTranslationWithExistingId() throws Exception {
        // Create the CountryTranslation with an existing ID
        countryTranslation.setId(1L);
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        int databaseSizeBeforeCreate = countryTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryTranslationRepository.findAll().size();
        // set the field null
        countryTranslation.setCreatedat(null);

        // Create the CountryTranslation, which fails.
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryTranslationRepository.findAll().size();
        // set the field null
        countryTranslation.setUpdatedat(null);

        // Create the CountryTranslation, which fails.
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryTranslationRepository.findAll().size();
        // set the field null
        countryTranslation.setLanguagecode(null);

        // Create the CountryTranslation, which fails.
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryTranslationRepository.findAll().size();
        // set the field null
        countryTranslation.setName(null);

        // Create the CountryTranslation, which fails.
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        restCountryTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountryTranslations() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countryTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCountryTranslation() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get the countryTranslation
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, countryTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countryTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCountryTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        Long id = countryTranslation.getId();

        defaultCountryTranslationShouldBeFound("id.equals=" + id);
        defaultCountryTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultCountryTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountryTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultCountryTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountryTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultCountryTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the countryTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultCountryTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultCountryTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the countryTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultCountryTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCountryTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the countryTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultCountryTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where createdat is not null
        defaultCountryTranslationShouldBeFound("createdat.specified=true");

        // Get all the countryTranslationList where createdat is null
        defaultCountryTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCountryTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the countryTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultCountryTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCountryTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the countryTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCountryTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCountryTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the countryTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultCountryTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where updatedat is not null
        defaultCountryTranslationShouldBeFound("updatedat.specified=true");

        // Get all the countryTranslationList where updatedat is null
        defaultCountryTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultCountryTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the countryTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultCountryTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the countryTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the countryTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode is not null
        defaultCountryTranslationShouldBeFound("languagecode.specified=true");

        // Get all the countryTranslationList where languagecode is null
        defaultCountryTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultCountryTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the countryTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultCountryTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the countryTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultCountryTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name equals to DEFAULT_NAME
        defaultCountryTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the countryTranslationList where name equals to UPDATED_NAME
        defaultCountryTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name not equals to DEFAULT_NAME
        defaultCountryTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the countryTranslationList where name not equals to UPDATED_NAME
        defaultCountryTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCountryTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the countryTranslationList where name equals to UPDATED_NAME
        defaultCountryTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name is not null
        defaultCountryTranslationShouldBeFound("name.specified=true");

        // Get all the countryTranslationList where name is null
        defaultCountryTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name contains DEFAULT_NAME
        defaultCountryTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the countryTranslationList where name contains UPDATED_NAME
        defaultCountryTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        // Get all the countryTranslationList where name does not contain DEFAULT_NAME
        defaultCountryTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the countryTranslationList where name does not contain UPDATED_NAME
        defaultCountryTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCountryTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);
        Country base = CountryResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        countryTranslation.setBase(base);
        countryTranslationRepository.saveAndFlush(countryTranslation);
        Long baseId = base.getId();

        // Get all the countryTranslationList where base equals to baseId
        defaultCountryTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the countryTranslationList where base equals to (baseId + 1)
        defaultCountryTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryTranslationShouldBeFound(String filter) throws Exception {
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countryTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryTranslationShouldNotBeFound(String filter) throws Exception {
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountryTranslation() throws Exception {
        // Get the countryTranslation
        restCountryTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountryTranslation() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();

        // Update the countryTranslation
        CountryTranslation updatedCountryTranslation = countryTranslationRepository.findById(countryTranslation.getId()).get();
        // Disconnect from session so that the updates on updatedCountryTranslation are not directly saved in db
        em.detach(updatedCountryTranslation);
        updatedCountryTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(updatedCountryTranslation);

        restCountryTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
        CountryTranslation testCountryTranslation = countryTranslationList.get(countryTranslationList.size() - 1);
        assertThat(testCountryTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountryTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCountryTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCountryTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryTranslationWithPatch() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();

        // Update the countryTranslation using partial update
        CountryTranslation partialUpdatedCountryTranslation = new CountryTranslation();
        partialUpdatedCountryTranslation.setId(countryTranslation.getId());

        partialUpdatedCountryTranslation.createdat(UPDATED_CREATEDAT).languagecode(UPDATED_LANGUAGECODE);

        restCountryTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountryTranslation))
            )
            .andExpect(status().isOk());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
        CountryTranslation testCountryTranslation = countryTranslationList.get(countryTranslationList.size() - 1);
        assertThat(testCountryTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountryTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCountryTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCountryTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountryTranslationWithPatch() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();

        // Update the countryTranslation using partial update
        CountryTranslation partialUpdatedCountryTranslation = new CountryTranslation();
        partialUpdatedCountryTranslation.setId(countryTranslation.getId());

        partialUpdatedCountryTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);

        restCountryTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountryTranslation))
            )
            .andExpect(status().isOk());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
        CountryTranslation testCountryTranslation = countryTranslationList.get(countryTranslationList.size() - 1);
        assertThat(testCountryTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCountryTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCountryTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCountryTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountryTranslation() throws Exception {
        int databaseSizeBeforeUpdate = countryTranslationRepository.findAll().size();
        countryTranslation.setId(count.incrementAndGet());

        // Create the CountryTranslation
        CountryTranslationDTO countryTranslationDTO = countryTranslationMapper.toDto(countryTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryTranslation in the database
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountryTranslation() throws Exception {
        // Initialize the database
        countryTranslationRepository.saveAndFlush(countryTranslation);

        int databaseSizeBeforeDelete = countryTranslationRepository.findAll().size();

        // Delete the countryTranslation
        restCountryTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, countryTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountryTranslation> countryTranslationList = countryTranslationRepository.findAll();
        assertThat(countryTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
