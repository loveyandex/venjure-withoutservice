package com.venjure.web.rest;

import static com.venjure.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.CustomerGroup;
import com.venjure.domain.TaxCategory;
import com.venjure.domain.TaxRate;
import com.venjure.domain.Zone;
import com.venjure.repository.TaxRateRepository;
import com.venjure.service.criteria.TaxRateCriteria;
import com.venjure.service.dto.TaxRateDTO;
import com.venjure.service.mapper.TaxRateMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TaxRateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxRateResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/tax-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Autowired
    private TaxRateMapper taxRateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxRateMockMvc;

    private TaxRate taxRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxRate createEntity(EntityManager em) {
        TaxRate taxRate = new TaxRate()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED)
            .value(DEFAULT_VALUE);
        return taxRate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxRate createUpdatedEntity(EntityManager em) {
        TaxRate taxRate = new TaxRate()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .value(UPDATED_VALUE);
        return taxRate;
    }

    @BeforeEach
    public void initTest() {
        taxRate = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxRate() throws Exception {
        int databaseSizeBeforeCreate = taxRateRepository.findAll().size();
        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);
        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeCreate + 1);
        TaxRate testTaxRate = taxRateList.get(taxRateList.size() - 1);
        assertThat(testTaxRate.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testTaxRate.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testTaxRate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxRate.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testTaxRate.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTaxRateWithExistingId() throws Exception {
        // Create the TaxRate with an existing ID
        taxRate.setId(1L);
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        int databaseSizeBeforeCreate = taxRateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRateRepository.findAll().size();
        // set the field null
        taxRate.setCreatedat(null);

        // Create the TaxRate, which fails.
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRateRepository.findAll().size();
        // set the field null
        taxRate.setUpdatedat(null);

        // Create the TaxRate, which fails.
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRateRepository.findAll().size();
        // set the field null
        taxRate.setName(null);

        // Create the TaxRate, which fails.
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRateRepository.findAll().size();
        // set the field null
        taxRate.setEnabled(null);

        // Create the TaxRate, which fails.
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRateRepository.findAll().size();
        // set the field null
        taxRate.setValue(null);

        // Create the TaxRate, which fails.
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        restTaxRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isBadRequest());

        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxRates() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));
    }

    @Test
    @Transactional
    void getTaxRate() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get the taxRate
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL_ID, taxRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxRate.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTaxRatesByIdFiltering() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        Long id = taxRate.getId();

        defaultTaxRateShouldBeFound("id.equals=" + id);
        defaultTaxRateShouldNotBeFound("id.notEquals=" + id);

        defaultTaxRateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaxRateShouldNotBeFound("id.greaterThan=" + id);

        defaultTaxRateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaxRateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxRatesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where createdat equals to DEFAULT_CREATEDAT
        defaultTaxRateShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the taxRateList where createdat equals to UPDATED_CREATEDAT
        defaultTaxRateShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where createdat not equals to DEFAULT_CREATEDAT
        defaultTaxRateShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the taxRateList where createdat not equals to UPDATED_CREATEDAT
        defaultTaxRateShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultTaxRateShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the taxRateList where createdat equals to UPDATED_CREATEDAT
        defaultTaxRateShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where createdat is not null
        defaultTaxRateShouldBeFound("createdat.specified=true");

        // Get all the taxRateList where createdat is null
        defaultTaxRateShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRatesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where updatedat equals to DEFAULT_UPDATEDAT
        defaultTaxRateShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the taxRateList where updatedat equals to UPDATED_UPDATEDAT
        defaultTaxRateShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultTaxRateShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the taxRateList where updatedat not equals to UPDATED_UPDATEDAT
        defaultTaxRateShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultTaxRateShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the taxRateList where updatedat equals to UPDATED_UPDATEDAT
        defaultTaxRateShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxRatesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where updatedat is not null
        defaultTaxRateShouldBeFound("updatedat.specified=true");

        // Get all the taxRateList where updatedat is null
        defaultTaxRateShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name equals to DEFAULT_NAME
        defaultTaxRateShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the taxRateList where name equals to UPDATED_NAME
        defaultTaxRateShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name not equals to DEFAULT_NAME
        defaultTaxRateShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the taxRateList where name not equals to UPDATED_NAME
        defaultTaxRateShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTaxRateShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the taxRateList where name equals to UPDATED_NAME
        defaultTaxRateShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name is not null
        defaultTaxRateShouldBeFound("name.specified=true");

        // Get all the taxRateList where name is null
        defaultTaxRateShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameContainsSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name contains DEFAULT_NAME
        defaultTaxRateShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the taxRateList where name contains UPDATED_NAME
        defaultTaxRateShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxRatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where name does not contain DEFAULT_NAME
        defaultTaxRateShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the taxRateList where name does not contain UPDATED_NAME
        defaultTaxRateShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxRatesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where enabled equals to DEFAULT_ENABLED
        defaultTaxRateShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the taxRateList where enabled equals to UPDATED_ENABLED
        defaultTaxRateShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllTaxRatesByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where enabled not equals to DEFAULT_ENABLED
        defaultTaxRateShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the taxRateList where enabled not equals to UPDATED_ENABLED
        defaultTaxRateShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllTaxRatesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultTaxRateShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the taxRateList where enabled equals to UPDATED_ENABLED
        defaultTaxRateShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllTaxRatesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where enabled is not null
        defaultTaxRateShouldBeFound("enabled.specified=true");

        // Get all the taxRateList where enabled is null
        defaultTaxRateShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value equals to DEFAULT_VALUE
        defaultTaxRateShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the taxRateList where value equals to UPDATED_VALUE
        defaultTaxRateShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value not equals to DEFAULT_VALUE
        defaultTaxRateShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the taxRateList where value not equals to UPDATED_VALUE
        defaultTaxRateShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTaxRateShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the taxRateList where value equals to UPDATED_VALUE
        defaultTaxRateShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value is not null
        defaultTaxRateShouldBeFound("value.specified=true");

        // Get all the taxRateList where value is null
        defaultTaxRateShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value is greater than or equal to DEFAULT_VALUE
        defaultTaxRateShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the taxRateList where value is greater than or equal to UPDATED_VALUE
        defaultTaxRateShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value is less than or equal to DEFAULT_VALUE
        defaultTaxRateShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the taxRateList where value is less than or equal to SMALLER_VALUE
        defaultTaxRateShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value is less than DEFAULT_VALUE
        defaultTaxRateShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the taxRateList where value is less than UPDATED_VALUE
        defaultTaxRateShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        // Get all the taxRateList where value is greater than DEFAULT_VALUE
        defaultTaxRateShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the taxRateList where value is greater than SMALLER_VALUE
        defaultTaxRateShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllTaxRatesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);
        TaxCategory category = TaxCategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        taxRate.setCategory(category);
        taxRateRepository.saveAndFlush(taxRate);
        Long categoryId = category.getId();

        // Get all the taxRateList where category equals to categoryId
        defaultTaxRateShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the taxRateList where category equals to (categoryId + 1)
        defaultTaxRateShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllTaxRatesByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);
        Zone zone = ZoneResourceIT.createEntity(em);
        em.persist(zone);
        em.flush();
        taxRate.setZone(zone);
        taxRateRepository.saveAndFlush(taxRate);
        Long zoneId = zone.getId();

        // Get all the taxRateList where zone equals to zoneId
        defaultTaxRateShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the taxRateList where zone equals to (zoneId + 1)
        defaultTaxRateShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    @Test
    @Transactional
    void getAllTaxRatesByCustomergroupIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);
        CustomerGroup customergroup = CustomerGroupResourceIT.createEntity(em);
        em.persist(customergroup);
        em.flush();
        taxRate.setCustomergroup(customergroup);
        taxRateRepository.saveAndFlush(taxRate);
        Long customergroupId = customergroup.getId();

        // Get all the taxRateList where customergroup equals to customergroupId
        defaultTaxRateShouldBeFound("customergroupId.equals=" + customergroupId);

        // Get all the taxRateList where customergroup equals to (customergroupId + 1)
        defaultTaxRateShouldNotBeFound("customergroupId.equals=" + (customergroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxRateShouldBeFound(String filter) throws Exception {
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));

        // Check, that the count call also returns 1
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxRateShouldNotBeFound(String filter) throws Exception {
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxRateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxRate() throws Exception {
        // Get the taxRate
        restTaxRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaxRate() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();

        // Update the taxRate
        TaxRate updatedTaxRate = taxRateRepository.findById(taxRate.getId()).get();
        // Disconnect from session so that the updates on updatedTaxRate are not directly saved in db
        em.detach(updatedTaxRate);
        updatedTaxRate
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .value(UPDATED_VALUE);
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(updatedTaxRate);

        restTaxRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
        TaxRate testTaxRate = taxRateList.get(taxRateList.size() - 1);
        assertThat(testTaxRate.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxRate.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTaxRate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxRate.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testTaxRate.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxRateWithPatch() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();

        // Update the taxRate using partial update
        TaxRate partialUpdatedTaxRate = new TaxRate();
        partialUpdatedTaxRate.setId(taxRate.getId());

        partialUpdatedTaxRate.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).enabled(UPDATED_ENABLED);

        restTaxRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxRate))
            )
            .andExpect(status().isOk());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
        TaxRate testTaxRate = taxRateList.get(taxRateList.size() - 1);
        assertThat(testTaxRate.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxRate.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTaxRate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxRate.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testTaxRate.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTaxRateWithPatch() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();

        // Update the taxRate using partial update
        TaxRate partialUpdatedTaxRate = new TaxRate();
        partialUpdatedTaxRate.setId(taxRate.getId());

        partialUpdatedTaxRate
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .value(UPDATED_VALUE);

        restTaxRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxRate))
            )
            .andExpect(status().isOk());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
        TaxRate testTaxRate = taxRateList.get(taxRateList.size() - 1);
        assertThat(testTaxRate.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxRate.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTaxRate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxRate.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testTaxRate.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxRateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxRate() throws Exception {
        int databaseSizeBeforeUpdate = taxRateRepository.findAll().size();
        taxRate.setId(count.incrementAndGet());

        // Create the TaxRate
        TaxRateDTO taxRateDTO = taxRateMapper.toDto(taxRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxRateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxRate in the database
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxRate() throws Exception {
        // Initialize the database
        taxRateRepository.saveAndFlush(taxRate);

        int databaseSizeBeforeDelete = taxRateRepository.findAll().size();

        // Delete the taxRate
        restTaxRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxRate> taxRateList = taxRateRepository.findAll();
        assertThat(taxRateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
