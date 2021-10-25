package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Jorder;
import com.venjure.domain.OrderModification;
import com.venjure.domain.Surcharge;
import com.venjure.repository.SurchargeRepository;
import com.venjure.service.criteria.SurchargeCriteria;
import com.venjure.service.dto.SurchargeDTO;
import com.venjure.service.mapper.SurchargeMapper;
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
 * Integration tests for the {@link SurchargeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurchargeResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LISTPRICE = 1;
    private static final Integer UPDATED_LISTPRICE = 2;
    private static final Integer SMALLER_LISTPRICE = 1 - 1;

    private static final Boolean DEFAULT_LISTPRICEINCLUDESTAX = false;
    private static final Boolean UPDATED_LISTPRICEINCLUDESTAX = true;

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_TAXLINES = "AAAAAAAAAA";
    private static final String UPDATED_TAXLINES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/surcharges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurchargeRepository surchargeRepository;

    @Autowired
    private SurchargeMapper surchargeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurchargeMockMvc;

    private Surcharge surcharge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surcharge createEntity(EntityManager em) {
        Surcharge surcharge = new Surcharge()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .description(DEFAULT_DESCRIPTION)
            .listprice(DEFAULT_LISTPRICE)
            .listpriceincludestax(DEFAULT_LISTPRICEINCLUDESTAX)
            .sku(DEFAULT_SKU)
            .taxlines(DEFAULT_TAXLINES);
        return surcharge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surcharge createUpdatedEntity(EntityManager em) {
        Surcharge surcharge = new Surcharge()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .description(UPDATED_DESCRIPTION)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .sku(UPDATED_SKU)
            .taxlines(UPDATED_TAXLINES);
        return surcharge;
    }

    @BeforeEach
    public void initTest() {
        surcharge = createEntity(em);
    }

    @Test
    @Transactional
    void createSurcharge() throws Exception {
        int databaseSizeBeforeCreate = surchargeRepository.findAll().size();
        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);
        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isCreated());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeCreate + 1);
        Surcharge testSurcharge = surchargeList.get(surchargeList.size() - 1);
        assertThat(testSurcharge.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testSurcharge.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testSurcharge.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSurcharge.getListprice()).isEqualTo(DEFAULT_LISTPRICE);
        assertThat(testSurcharge.getListpriceincludestax()).isEqualTo(DEFAULT_LISTPRICEINCLUDESTAX);
        assertThat(testSurcharge.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testSurcharge.getTaxlines()).isEqualTo(DEFAULT_TAXLINES);
    }

    @Test
    @Transactional
    void createSurchargeWithExistingId() throws Exception {
        // Create the Surcharge with an existing ID
        surcharge.setId(1L);
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        int databaseSizeBeforeCreate = surchargeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setCreatedat(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setUpdatedat(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setDescription(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setListprice(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceincludestaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setListpriceincludestax(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setSku(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxlinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = surchargeRepository.findAll().size();
        // set the field null
        surcharge.setTaxlines(null);

        // Create the Surcharge, which fails.
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        restSurchargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isBadRequest());

        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSurcharges() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surcharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)));
    }

    @Test
    @Transactional
    void getSurcharge() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get the surcharge
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL_ID, surcharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surcharge.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.listprice").value(DEFAULT_LISTPRICE))
            .andExpect(jsonPath("$.listpriceincludestax").value(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.taxlines").value(DEFAULT_TAXLINES));
    }

    @Test
    @Transactional
    void getSurchargesByIdFiltering() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        Long id = surcharge.getId();

        defaultSurchargeShouldBeFound("id.equals=" + id);
        defaultSurchargeShouldNotBeFound("id.notEquals=" + id);

        defaultSurchargeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSurchargeShouldNotBeFound("id.greaterThan=" + id);

        defaultSurchargeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSurchargeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSurchargesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where createdat equals to DEFAULT_CREATEDAT
        defaultSurchargeShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the surchargeList where createdat equals to UPDATED_CREATEDAT
        defaultSurchargeShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where createdat not equals to DEFAULT_CREATEDAT
        defaultSurchargeShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the surchargeList where createdat not equals to UPDATED_CREATEDAT
        defaultSurchargeShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultSurchargeShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the surchargeList where createdat equals to UPDATED_CREATEDAT
        defaultSurchargeShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where createdat is not null
        defaultSurchargeShouldBeFound("createdat.specified=true");

        // Get all the surchargeList where createdat is null
        defaultSurchargeShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where updatedat equals to DEFAULT_UPDATEDAT
        defaultSurchargeShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the surchargeList where updatedat equals to UPDATED_UPDATEDAT
        defaultSurchargeShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultSurchargeShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the surchargeList where updatedat not equals to UPDATED_UPDATEDAT
        defaultSurchargeShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultSurchargeShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the surchargeList where updatedat equals to UPDATED_UPDATEDAT
        defaultSurchargeShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllSurchargesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where updatedat is not null
        defaultSurchargeShouldBeFound("updatedat.specified=true");

        // Get all the surchargeList where updatedat is null
        defaultSurchargeShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description equals to DEFAULT_DESCRIPTION
        defaultSurchargeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the surchargeList where description equals to UPDATED_DESCRIPTION
        defaultSurchargeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description not equals to DEFAULT_DESCRIPTION
        defaultSurchargeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the surchargeList where description not equals to UPDATED_DESCRIPTION
        defaultSurchargeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSurchargeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the surchargeList where description equals to UPDATED_DESCRIPTION
        defaultSurchargeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description is not null
        defaultSurchargeShouldBeFound("description.specified=true");

        // Get all the surchargeList where description is null
        defaultSurchargeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description contains DEFAULT_DESCRIPTION
        defaultSurchargeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the surchargeList where description contains UPDATED_DESCRIPTION
        defaultSurchargeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSurchargesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where description does not contain DEFAULT_DESCRIPTION
        defaultSurchargeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the surchargeList where description does not contain UPDATED_DESCRIPTION
        defaultSurchargeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice equals to DEFAULT_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.equals=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice equals to UPDATED_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.equals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice not equals to DEFAULT_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.notEquals=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice not equals to UPDATED_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.notEquals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice in DEFAULT_LISTPRICE or UPDATED_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.in=" + DEFAULT_LISTPRICE + "," + UPDATED_LISTPRICE);

        // Get all the surchargeList where listprice equals to UPDATED_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.in=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice is not null
        defaultSurchargeShouldBeFound("listprice.specified=true");

        // Get all the surchargeList where listprice is null
        defaultSurchargeShouldNotBeFound("listprice.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice is greater than or equal to DEFAULT_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.greaterThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice is greater than or equal to UPDATED_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.greaterThanOrEqual=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice is less than or equal to DEFAULT_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.lessThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice is less than or equal to SMALLER_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.lessThanOrEqual=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsLessThanSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice is less than DEFAULT_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.lessThan=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice is less than UPDATED_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.lessThan=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listprice is greater than DEFAULT_LISTPRICE
        defaultSurchargeShouldNotBeFound("listprice.greaterThan=" + DEFAULT_LISTPRICE);

        // Get all the surchargeList where listprice is greater than SMALLER_LISTPRICE
        defaultSurchargeShouldBeFound("listprice.greaterThan=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceincludestaxIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listpriceincludestax equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldBeFound("listpriceincludestax.equals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the surchargeList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldNotBeFound("listpriceincludestax.equals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceincludestaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listpriceincludestax not equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldNotBeFound("listpriceincludestax.notEquals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the surchargeList where listpriceincludestax not equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldBeFound("listpriceincludestax.notEquals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceincludestaxIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listpriceincludestax in DEFAULT_LISTPRICEINCLUDESTAX or UPDATED_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldBeFound("listpriceincludestax.in=" + DEFAULT_LISTPRICEINCLUDESTAX + "," + UPDATED_LISTPRICEINCLUDESTAX);

        // Get all the surchargeList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultSurchargeShouldNotBeFound("listpriceincludestax.in=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllSurchargesByListpriceincludestaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where listpriceincludestax is not null
        defaultSurchargeShouldBeFound("listpriceincludestax.specified=true");

        // Get all the surchargeList where listpriceincludestax is null
        defaultSurchargeShouldNotBeFound("listpriceincludestax.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku equals to DEFAULT_SKU
        defaultSurchargeShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the surchargeList where sku equals to UPDATED_SKU
        defaultSurchargeShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku not equals to DEFAULT_SKU
        defaultSurchargeShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the surchargeList where sku not equals to UPDATED_SKU
        defaultSurchargeShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultSurchargeShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the surchargeList where sku equals to UPDATED_SKU
        defaultSurchargeShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku is not null
        defaultSurchargeShouldBeFound("sku.specified=true");

        // Get all the surchargeList where sku is null
        defaultSurchargeShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku contains DEFAULT_SKU
        defaultSurchargeShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the surchargeList where sku contains UPDATED_SKU
        defaultSurchargeShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllSurchargesBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where sku does not contain DEFAULT_SKU
        defaultSurchargeShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the surchargeList where sku does not contain UPDATED_SKU
        defaultSurchargeShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines equals to DEFAULT_TAXLINES
        defaultSurchargeShouldBeFound("taxlines.equals=" + DEFAULT_TAXLINES);

        // Get all the surchargeList where taxlines equals to UPDATED_TAXLINES
        defaultSurchargeShouldNotBeFound("taxlines.equals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines not equals to DEFAULT_TAXLINES
        defaultSurchargeShouldNotBeFound("taxlines.notEquals=" + DEFAULT_TAXLINES);

        // Get all the surchargeList where taxlines not equals to UPDATED_TAXLINES
        defaultSurchargeShouldBeFound("taxlines.notEquals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesIsInShouldWork() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines in DEFAULT_TAXLINES or UPDATED_TAXLINES
        defaultSurchargeShouldBeFound("taxlines.in=" + DEFAULT_TAXLINES + "," + UPDATED_TAXLINES);

        // Get all the surchargeList where taxlines equals to UPDATED_TAXLINES
        defaultSurchargeShouldNotBeFound("taxlines.in=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines is not null
        defaultSurchargeShouldBeFound("taxlines.specified=true");

        // Get all the surchargeList where taxlines is null
        defaultSurchargeShouldNotBeFound("taxlines.specified=false");
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines contains DEFAULT_TAXLINES
        defaultSurchargeShouldBeFound("taxlines.contains=" + DEFAULT_TAXLINES);

        // Get all the surchargeList where taxlines contains UPDATED_TAXLINES
        defaultSurchargeShouldNotBeFound("taxlines.contains=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllSurchargesByTaxlinesNotContainsSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        // Get all the surchargeList where taxlines does not contain DEFAULT_TAXLINES
        defaultSurchargeShouldNotBeFound("taxlines.doesNotContain=" + DEFAULT_TAXLINES);

        // Get all the surchargeList where taxlines does not contain UPDATED_TAXLINES
        defaultSurchargeShouldBeFound("taxlines.doesNotContain=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllSurchargesByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        surcharge.setJorder(jorder);
        surchargeRepository.saveAndFlush(surcharge);
        Long jorderId = jorder.getId();

        // Get all the surchargeList where jorder equals to jorderId
        defaultSurchargeShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the surchargeList where jorder equals to (jorderId + 1)
        defaultSurchargeShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllSurchargesByOrdermodificationIsEqualToSomething() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);
        OrderModification ordermodification = OrderModificationResourceIT.createEntity(em);
        em.persist(ordermodification);
        em.flush();
        surcharge.setOrdermodification(ordermodification);
        surchargeRepository.saveAndFlush(surcharge);
        Long ordermodificationId = ordermodification.getId();

        // Get all the surchargeList where ordermodification equals to ordermodificationId
        defaultSurchargeShouldBeFound("ordermodificationId.equals=" + ordermodificationId);

        // Get all the surchargeList where ordermodification equals to (ordermodificationId + 1)
        defaultSurchargeShouldNotBeFound("ordermodificationId.equals=" + (ordermodificationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSurchargeShouldBeFound(String filter) throws Exception {
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surcharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)));

        // Check, that the count call also returns 1
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSurchargeShouldNotBeFound(String filter) throws Exception {
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSurchargeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSurcharge() throws Exception {
        // Get the surcharge
        restSurchargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSurcharge() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();

        // Update the surcharge
        Surcharge updatedSurcharge = surchargeRepository.findById(surcharge.getId()).get();
        // Disconnect from session so that the updates on updatedSurcharge are not directly saved in db
        em.detach(updatedSurcharge);
        updatedSurcharge
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .description(UPDATED_DESCRIPTION)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .sku(UPDATED_SKU)
            .taxlines(UPDATED_TAXLINES);
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(updatedSurcharge);

        restSurchargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surchargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
        Surcharge testSurcharge = surchargeList.get(surchargeList.size() - 1);
        assertThat(testSurcharge.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testSurcharge.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testSurcharge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurcharge.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testSurcharge.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testSurcharge.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testSurcharge.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void putNonExistingSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surchargeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surchargeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurchargeWithPatch() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();

        // Update the surcharge using partial update
        Surcharge partialUpdatedSurcharge = new Surcharge();
        partialUpdatedSurcharge.setId(surcharge.getId());

        partialUpdatedSurcharge.description(UPDATED_DESCRIPTION).listprice(UPDATED_LISTPRICE);

        restSurchargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurcharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurcharge))
            )
            .andExpect(status().isOk());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
        Surcharge testSurcharge = surchargeList.get(surchargeList.size() - 1);
        assertThat(testSurcharge.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testSurcharge.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testSurcharge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurcharge.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testSurcharge.getListpriceincludestax()).isEqualTo(DEFAULT_LISTPRICEINCLUDESTAX);
        assertThat(testSurcharge.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testSurcharge.getTaxlines()).isEqualTo(DEFAULT_TAXLINES);
    }

    @Test
    @Transactional
    void fullUpdateSurchargeWithPatch() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();

        // Update the surcharge using partial update
        Surcharge partialUpdatedSurcharge = new Surcharge();
        partialUpdatedSurcharge.setId(surcharge.getId());

        partialUpdatedSurcharge
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .description(UPDATED_DESCRIPTION)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .sku(UPDATED_SKU)
            .taxlines(UPDATED_TAXLINES);

        restSurchargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurcharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurcharge))
            )
            .andExpect(status().isOk());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
        Surcharge testSurcharge = surchargeList.get(surchargeList.size() - 1);
        assertThat(testSurcharge.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testSurcharge.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testSurcharge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSurcharge.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testSurcharge.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testSurcharge.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testSurcharge.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void patchNonExistingSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surchargeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurcharge() throws Exception {
        int databaseSizeBeforeUpdate = surchargeRepository.findAll().size();
        surcharge.setId(count.incrementAndGet());

        // Create the Surcharge
        SurchargeDTO surchargeDTO = surchargeMapper.toDto(surcharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurchargeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(surchargeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surcharge in the database
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurcharge() throws Exception {
        // Initialize the database
        surchargeRepository.saveAndFlush(surcharge);

        int databaseSizeBeforeDelete = surchargeRepository.findAll().size();

        // Delete the surcharge
        restSurchargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, surcharge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Surcharge> surchargeList = surchargeRepository.findAll();
        assertThat(surchargeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
