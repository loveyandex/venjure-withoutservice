package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.GlobalSettings;
import com.venjure.repository.GlobalSettingsRepository;
import com.venjure.service.criteria.GlobalSettingsCriteria;
import com.venjure.service.dto.GlobalSettingsDTO;
import com.venjure.service.mapper.GlobalSettingsMapper;
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
 * Integration tests for the {@link GlobalSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GlobalSettingsResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AVAILABLELANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_AVAILABLELANGUAGES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TRACKINVENTORY = false;
    private static final Boolean UPDATED_TRACKINVENTORY = true;

    private static final Integer DEFAULT_OUTOFSTOCKTHRESHOLD = 1;
    private static final Integer UPDATED_OUTOFSTOCKTHRESHOLD = 2;
    private static final Integer SMALLER_OUTOFSTOCKTHRESHOLD = 1 - 1;

    private static final String ENTITY_API_URL = "/api/global-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    private GlobalSettingsMapper globalSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlobalSettingsMockMvc;

    private GlobalSettings globalSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalSettings createEntity(EntityManager em) {
        GlobalSettings globalSettings = new GlobalSettings()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .availablelanguages(DEFAULT_AVAILABLELANGUAGES)
            .trackinventory(DEFAULT_TRACKINVENTORY)
            .outofstockthreshold(DEFAULT_OUTOFSTOCKTHRESHOLD);
        return globalSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalSettings createUpdatedEntity(EntityManager em) {
        GlobalSettings globalSettings = new GlobalSettings()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .availablelanguages(UPDATED_AVAILABLELANGUAGES)
            .trackinventory(UPDATED_TRACKINVENTORY)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD);
        return globalSettings;
    }

    @BeforeEach
    public void initTest() {
        globalSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createGlobalSettings() throws Exception {
        int databaseSizeBeforeCreate = globalSettingsRepository.findAll().size();
        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);
        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        GlobalSettings testGlobalSettings = globalSettingsList.get(globalSettingsList.size() - 1);
        assertThat(testGlobalSettings.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testGlobalSettings.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testGlobalSettings.getAvailablelanguages()).isEqualTo(DEFAULT_AVAILABLELANGUAGES);
        assertThat(testGlobalSettings.getTrackinventory()).isEqualTo(DEFAULT_TRACKINVENTORY);
        assertThat(testGlobalSettings.getOutofstockthreshold()).isEqualTo(DEFAULT_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void createGlobalSettingsWithExistingId() throws Exception {
        // Create the GlobalSettings with an existing ID
        globalSettings.setId(1L);
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        int databaseSizeBeforeCreate = globalSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalSettingsRepository.findAll().size();
        // set the field null
        globalSettings.setCreatedat(null);

        // Create the GlobalSettings, which fails.
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalSettingsRepository.findAll().size();
        // set the field null
        globalSettings.setUpdatedat(null);

        // Create the GlobalSettings, which fails.
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailablelanguagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalSettingsRepository.findAll().size();
        // set the field null
        globalSettings.setAvailablelanguages(null);

        // Create the GlobalSettings, which fails.
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrackinventoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalSettingsRepository.findAll().size();
        // set the field null
        globalSettings.setTrackinventory(null);

        // Create the GlobalSettings, which fails.
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutofstockthresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalSettingsRepository.findAll().size();
        // set the field null
        globalSettings.setOutofstockthreshold(null);

        // Create the GlobalSettings, which fails.
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        restGlobalSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGlobalSettings() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].availablelanguages").value(hasItem(DEFAULT_AVAILABLELANGUAGES)))
            .andExpect(jsonPath("$.[*].trackinventory").value(hasItem(DEFAULT_TRACKINVENTORY.booleanValue())))
            .andExpect(jsonPath("$.[*].outofstockthreshold").value(hasItem(DEFAULT_OUTOFSTOCKTHRESHOLD)));
    }

    @Test
    @Transactional
    void getGlobalSettings() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get the globalSettings
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, globalSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(globalSettings.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.availablelanguages").value(DEFAULT_AVAILABLELANGUAGES))
            .andExpect(jsonPath("$.trackinventory").value(DEFAULT_TRACKINVENTORY.booleanValue()))
            .andExpect(jsonPath("$.outofstockthreshold").value(DEFAULT_OUTOFSTOCKTHRESHOLD));
    }

    @Test
    @Transactional
    void getGlobalSettingsByIdFiltering() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        Long id = globalSettings.getId();

        defaultGlobalSettingsShouldBeFound("id.equals=" + id);
        defaultGlobalSettingsShouldNotBeFound("id.notEquals=" + id);

        defaultGlobalSettingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGlobalSettingsShouldNotBeFound("id.greaterThan=" + id);

        defaultGlobalSettingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGlobalSettingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where createdat equals to DEFAULT_CREATEDAT
        defaultGlobalSettingsShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the globalSettingsList where createdat equals to UPDATED_CREATEDAT
        defaultGlobalSettingsShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where createdat not equals to DEFAULT_CREATEDAT
        defaultGlobalSettingsShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the globalSettingsList where createdat not equals to UPDATED_CREATEDAT
        defaultGlobalSettingsShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultGlobalSettingsShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the globalSettingsList where createdat equals to UPDATED_CREATEDAT
        defaultGlobalSettingsShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where createdat is not null
        defaultGlobalSettingsShouldBeFound("createdat.specified=true");

        // Get all the globalSettingsList where createdat is null
        defaultGlobalSettingsShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where updatedat equals to DEFAULT_UPDATEDAT
        defaultGlobalSettingsShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the globalSettingsList where updatedat equals to UPDATED_UPDATEDAT
        defaultGlobalSettingsShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultGlobalSettingsShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the globalSettingsList where updatedat not equals to UPDATED_UPDATEDAT
        defaultGlobalSettingsShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultGlobalSettingsShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the globalSettingsList where updatedat equals to UPDATED_UPDATEDAT
        defaultGlobalSettingsShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where updatedat is not null
        defaultGlobalSettingsShouldBeFound("updatedat.specified=true");

        // Get all the globalSettingsList where updatedat is null
        defaultGlobalSettingsShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesIsEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages equals to DEFAULT_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldBeFound("availablelanguages.equals=" + DEFAULT_AVAILABLELANGUAGES);

        // Get all the globalSettingsList where availablelanguages equals to UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.equals=" + UPDATED_AVAILABLELANGUAGES);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages not equals to DEFAULT_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.notEquals=" + DEFAULT_AVAILABLELANGUAGES);

        // Get all the globalSettingsList where availablelanguages not equals to UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldBeFound("availablelanguages.notEquals=" + UPDATED_AVAILABLELANGUAGES);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesIsInShouldWork() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages in DEFAULT_AVAILABLELANGUAGES or UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldBeFound("availablelanguages.in=" + DEFAULT_AVAILABLELANGUAGES + "," + UPDATED_AVAILABLELANGUAGES);

        // Get all the globalSettingsList where availablelanguages equals to UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.in=" + UPDATED_AVAILABLELANGUAGES);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages is not null
        defaultGlobalSettingsShouldBeFound("availablelanguages.specified=true");

        // Get all the globalSettingsList where availablelanguages is null
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.specified=false");
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesContainsSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages contains DEFAULT_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldBeFound("availablelanguages.contains=" + DEFAULT_AVAILABLELANGUAGES);

        // Get all the globalSettingsList where availablelanguages contains UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.contains=" + UPDATED_AVAILABLELANGUAGES);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByAvailablelanguagesNotContainsSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where availablelanguages does not contain DEFAULT_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldNotBeFound("availablelanguages.doesNotContain=" + DEFAULT_AVAILABLELANGUAGES);

        // Get all the globalSettingsList where availablelanguages does not contain UPDATED_AVAILABLELANGUAGES
        defaultGlobalSettingsShouldBeFound("availablelanguages.doesNotContain=" + UPDATED_AVAILABLELANGUAGES);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByTrackinventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where trackinventory equals to DEFAULT_TRACKINVENTORY
        defaultGlobalSettingsShouldBeFound("trackinventory.equals=" + DEFAULT_TRACKINVENTORY);

        // Get all the globalSettingsList where trackinventory equals to UPDATED_TRACKINVENTORY
        defaultGlobalSettingsShouldNotBeFound("trackinventory.equals=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByTrackinventoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where trackinventory not equals to DEFAULT_TRACKINVENTORY
        defaultGlobalSettingsShouldNotBeFound("trackinventory.notEquals=" + DEFAULT_TRACKINVENTORY);

        // Get all the globalSettingsList where trackinventory not equals to UPDATED_TRACKINVENTORY
        defaultGlobalSettingsShouldBeFound("trackinventory.notEquals=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByTrackinventoryIsInShouldWork() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where trackinventory in DEFAULT_TRACKINVENTORY or UPDATED_TRACKINVENTORY
        defaultGlobalSettingsShouldBeFound("trackinventory.in=" + DEFAULT_TRACKINVENTORY + "," + UPDATED_TRACKINVENTORY);

        // Get all the globalSettingsList where trackinventory equals to UPDATED_TRACKINVENTORY
        defaultGlobalSettingsShouldNotBeFound("trackinventory.in=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByTrackinventoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where trackinventory is not null
        defaultGlobalSettingsShouldBeFound("trackinventory.specified=true");

        // Get all the globalSettingsList where trackinventory is null
        defaultGlobalSettingsShouldNotBeFound("trackinventory.specified=false");
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold equals to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.equals=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.equals=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold not equals to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.notEquals=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold not equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.notEquals=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsInShouldWork() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold in DEFAULT_OUTOFSTOCKTHRESHOLD or UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.in=" + DEFAULT_OUTOFSTOCKTHRESHOLD + "," + UPDATED_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.in=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold is not null
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.specified=true");

        // Get all the globalSettingsList where outofstockthreshold is null
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold is greater than or equal to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.greaterThanOrEqual=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold is greater than or equal to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.greaterThanOrEqual=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold is less than or equal to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.lessThanOrEqual=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold is less than or equal to SMALLER_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.lessThanOrEqual=" + SMALLER_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold is less than DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.lessThan=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold is less than UPDATED_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.lessThan=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllGlobalSettingsByOutofstockthresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        // Get all the globalSettingsList where outofstockthreshold is greater than DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldNotBeFound("outofstockthreshold.greaterThan=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the globalSettingsList where outofstockthreshold is greater than SMALLER_OUTOFSTOCKTHRESHOLD
        defaultGlobalSettingsShouldBeFound("outofstockthreshold.greaterThan=" + SMALLER_OUTOFSTOCKTHRESHOLD);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGlobalSettingsShouldBeFound(String filter) throws Exception {
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].availablelanguages").value(hasItem(DEFAULT_AVAILABLELANGUAGES)))
            .andExpect(jsonPath("$.[*].trackinventory").value(hasItem(DEFAULT_TRACKINVENTORY.booleanValue())))
            .andExpect(jsonPath("$.[*].outofstockthreshold").value(hasItem(DEFAULT_OUTOFSTOCKTHRESHOLD)));

        // Check, that the count call also returns 1
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGlobalSettingsShouldNotBeFound(String filter) throws Exception {
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGlobalSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGlobalSettings() throws Exception {
        // Get the globalSettings
        restGlobalSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGlobalSettings() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();

        // Update the globalSettings
        GlobalSettings updatedGlobalSettings = globalSettingsRepository.findById(globalSettings.getId()).get();
        // Disconnect from session so that the updates on updatedGlobalSettings are not directly saved in db
        em.detach(updatedGlobalSettings);
        updatedGlobalSettings
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .availablelanguages(UPDATED_AVAILABLELANGUAGES)
            .trackinventory(UPDATED_TRACKINVENTORY)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD);
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(updatedGlobalSettings);

        restGlobalSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
        GlobalSettings testGlobalSettings = globalSettingsList.get(globalSettingsList.size() - 1);
        assertThat(testGlobalSettings.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testGlobalSettings.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testGlobalSettings.getAvailablelanguages()).isEqualTo(UPDATED_AVAILABLELANGUAGES);
        assertThat(testGlobalSettings.getTrackinventory()).isEqualTo(UPDATED_TRACKINVENTORY);
        assertThat(testGlobalSettings.getOutofstockthreshold()).isEqualTo(UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void putNonExistingGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGlobalSettingsWithPatch() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();

        // Update the globalSettings using partial update
        GlobalSettings partialUpdatedGlobalSettings = new GlobalSettings();
        partialUpdatedGlobalSettings.setId(globalSettings.getId());

        partialUpdatedGlobalSettings
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .availablelanguages(UPDATED_AVAILABLELANGUAGES)
            .trackinventory(UPDATED_TRACKINVENTORY);

        restGlobalSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalSettings))
            )
            .andExpect(status().isOk());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
        GlobalSettings testGlobalSettings = globalSettingsList.get(globalSettingsList.size() - 1);
        assertThat(testGlobalSettings.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testGlobalSettings.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testGlobalSettings.getAvailablelanguages()).isEqualTo(UPDATED_AVAILABLELANGUAGES);
        assertThat(testGlobalSettings.getTrackinventory()).isEqualTo(UPDATED_TRACKINVENTORY);
        assertThat(testGlobalSettings.getOutofstockthreshold()).isEqualTo(DEFAULT_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void fullUpdateGlobalSettingsWithPatch() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();

        // Update the globalSettings using partial update
        GlobalSettings partialUpdatedGlobalSettings = new GlobalSettings();
        partialUpdatedGlobalSettings.setId(globalSettings.getId());

        partialUpdatedGlobalSettings
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .availablelanguages(UPDATED_AVAILABLELANGUAGES)
            .trackinventory(UPDATED_TRACKINVENTORY)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD);

        restGlobalSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalSettings))
            )
            .andExpect(status().isOk());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
        GlobalSettings testGlobalSettings = globalSettingsList.get(globalSettingsList.size() - 1);
        assertThat(testGlobalSettings.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testGlobalSettings.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testGlobalSettings.getAvailablelanguages()).isEqualTo(UPDATED_AVAILABLELANGUAGES);
        assertThat(testGlobalSettings.getTrackinventory()).isEqualTo(UPDATED_TRACKINVENTORY);
        assertThat(testGlobalSettings.getOutofstockthreshold()).isEqualTo(UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void patchNonExistingGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, globalSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlobalSettings() throws Exception {
        int databaseSizeBeforeUpdate = globalSettingsRepository.findAll().size();
        globalSettings.setId(count.incrementAndGet());

        // Create the GlobalSettings
        GlobalSettingsDTO globalSettingsDTO = globalSettingsMapper.toDto(globalSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalSettings in the database
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGlobalSettings() throws Exception {
        // Initialize the database
        globalSettingsRepository.saveAndFlush(globalSettings);

        int databaseSizeBeforeDelete = globalSettingsRepository.findAll().size();

        // Delete the globalSettings
        restGlobalSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, globalSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlobalSettings> globalSettingsList = globalSettingsRepository.findAll();
        assertThat(globalSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
