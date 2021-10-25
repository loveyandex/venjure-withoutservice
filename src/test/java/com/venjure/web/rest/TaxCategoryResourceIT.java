package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.TaxCategory;
import com.venjure.domain.TaxRate;
import com.venjure.repository.TaxCategoryRepository;
import com.venjure.service.criteria.TaxCategoryCriteria;
import com.venjure.service.dto.TaxCategoryDTO;
import com.venjure.service.mapper.TaxCategoryMapper;
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
 * Integration tests for the {@link TaxCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxCategoryResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISDEFAULT = false;
    private static final Boolean UPDATED_ISDEFAULT = true;

    private static final String ENTITY_API_URL = "/api/tax-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxCategoryRepository taxCategoryRepository;

    @Autowired
    private TaxCategoryMapper taxCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxCategoryMockMvc;

    private TaxCategory taxCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxCategory createEntity(EntityManager em) {
        TaxCategory taxCategory = new TaxCategory()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .name(DEFAULT_NAME)
            .isdefault(DEFAULT_ISDEFAULT);
        return taxCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxCategory createUpdatedEntity(EntityManager em) {
        TaxCategory taxCategory = new TaxCategory()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .isdefault(UPDATED_ISDEFAULT);
        return taxCategory;
    }

    @BeforeEach
    public void initTest() {
        taxCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxCategory() throws Exception {
        int databaseSizeBeforeCreate = taxCategoryRepository.findAll().size();
        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);
        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testTaxCategory.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testTaxCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxCategory.getIsdefault()).isEqualTo(DEFAULT_ISDEFAULT);
    }

    @Test
    @Transactional
    void createTaxCategoryWithExistingId() throws Exception {
        // Create the TaxCategory with an existing ID
        taxCategory.setId(1L);
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        int databaseSizeBeforeCreate = taxCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxCategoryRepository.findAll().size();
        // set the field null
        taxCategory.setCreatedat(null);

        // Create the TaxCategory, which fails.
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxCategoryRepository.findAll().size();
        // set the field null
        taxCategory.setUpdatedat(null);

        // Create the TaxCategory, which fails.
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxCategoryRepository.findAll().size();
        // set the field null
        taxCategory.setName(null);

        // Create the TaxCategory, which fails.
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsdefaultIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxCategoryRepository.findAll().size();
        // set the field null
        taxCategory.setIsdefault(null);

        // Create the TaxCategory, which fails.
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        restTaxCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxCategories() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isdefault").value(hasItem(DEFAULT_ISDEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get the taxCategory
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, taxCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxCategory.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isdefault").value(DEFAULT_ISDEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getTaxCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        Long id = taxCategory.getId();

        defaultTaxCategoryShouldBeFound("id.equals=" + id);
        defaultTaxCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultTaxCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaxCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTaxCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaxCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where createdat equals to DEFAULT_CREATEDAT
        defaultTaxCategoryShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the taxCategoryList where createdat equals to UPDATED_CREATEDAT
        defaultTaxCategoryShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where createdat not equals to DEFAULT_CREATEDAT
        defaultTaxCategoryShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the taxCategoryList where createdat not equals to UPDATED_CREATEDAT
        defaultTaxCategoryShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultTaxCategoryShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the taxCategoryList where createdat equals to UPDATED_CREATEDAT
        defaultTaxCategoryShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where createdat is not null
        defaultTaxCategoryShouldBeFound("createdat.specified=true");

        // Get all the taxCategoryList where createdat is null
        defaultTaxCategoryShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where updatedat equals to DEFAULT_UPDATEDAT
        defaultTaxCategoryShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the taxCategoryList where updatedat equals to UPDATED_UPDATEDAT
        defaultTaxCategoryShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultTaxCategoryShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the taxCategoryList where updatedat not equals to UPDATED_UPDATEDAT
        defaultTaxCategoryShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultTaxCategoryShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the taxCategoryList where updatedat equals to UPDATED_UPDATEDAT
        defaultTaxCategoryShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where updatedat is not null
        defaultTaxCategoryShouldBeFound("updatedat.specified=true");

        // Get all the taxCategoryList where updatedat is null
        defaultTaxCategoryShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name equals to DEFAULT_NAME
        defaultTaxCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the taxCategoryList where name equals to UPDATED_NAME
        defaultTaxCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name not equals to DEFAULT_NAME
        defaultTaxCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the taxCategoryList where name not equals to UPDATED_NAME
        defaultTaxCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTaxCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the taxCategoryList where name equals to UPDATED_NAME
        defaultTaxCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name is not null
        defaultTaxCategoryShouldBeFound("name.specified=true");

        // Get all the taxCategoryList where name is null
        defaultTaxCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name contains DEFAULT_NAME
        defaultTaxCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the taxCategoryList where name contains UPDATED_NAME
        defaultTaxCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where name does not contain DEFAULT_NAME
        defaultTaxCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the taxCategoryList where name does not contain UPDATED_NAME
        defaultTaxCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByIsdefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where isdefault equals to DEFAULT_ISDEFAULT
        defaultTaxCategoryShouldBeFound("isdefault.equals=" + DEFAULT_ISDEFAULT);

        // Get all the taxCategoryList where isdefault equals to UPDATED_ISDEFAULT
        defaultTaxCategoryShouldNotBeFound("isdefault.equals=" + UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByIsdefaultIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where isdefault not equals to DEFAULT_ISDEFAULT
        defaultTaxCategoryShouldNotBeFound("isdefault.notEquals=" + DEFAULT_ISDEFAULT);

        // Get all the taxCategoryList where isdefault not equals to UPDATED_ISDEFAULT
        defaultTaxCategoryShouldBeFound("isdefault.notEquals=" + UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByIsdefaultIsInShouldWork() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where isdefault in DEFAULT_ISDEFAULT or UPDATED_ISDEFAULT
        defaultTaxCategoryShouldBeFound("isdefault.in=" + DEFAULT_ISDEFAULT + "," + UPDATED_ISDEFAULT);

        // Get all the taxCategoryList where isdefault equals to UPDATED_ISDEFAULT
        defaultTaxCategoryShouldNotBeFound("isdefault.in=" + UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByIsdefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList where isdefault is not null
        defaultTaxCategoryShouldBeFound("isdefault.specified=true");

        // Get all the taxCategoryList where isdefault is null
        defaultTaxCategoryShouldNotBeFound("isdefault.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxCategoriesByTaxRateIsEqualToSomething() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);
        TaxRate taxRate = TaxRateResourceIT.createEntity(em);
        em.persist(taxRate);
        em.flush();
        taxCategory.addTaxRate(taxRate);
        taxCategoryRepository.saveAndFlush(taxCategory);
        Long taxRateId = taxRate.getId();

        // Get all the taxCategoryList where taxRate equals to taxRateId
        defaultTaxCategoryShouldBeFound("taxRateId.equals=" + taxRateId);

        // Get all the taxCategoryList where taxRate equals to (taxRateId + 1)
        defaultTaxCategoryShouldNotBeFound("taxRateId.equals=" + (taxRateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxCategoryShouldBeFound(String filter) throws Exception {
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isdefault").value(hasItem(DEFAULT_ISDEFAULT.booleanValue())));

        // Check, that the count call also returns 1
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxCategoryShouldNotBeFound(String filter) throws Exception {
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxCategory() throws Exception {
        // Get the taxCategory
        restTaxCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();

        // Update the taxCategory
        TaxCategory updatedTaxCategory = taxCategoryRepository.findById(taxCategory.getId()).get();
        // Disconnect from session so that the updates on updatedTaxCategory are not directly saved in db
        em.detach(updatedTaxCategory);
        updatedTaxCategory.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME).isdefault(UPDATED_ISDEFAULT);
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(updatedTaxCategory);

        restTaxCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxCategory.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTaxCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxCategory.getIsdefault()).isEqualTo(UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void putNonExistingTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxCategoryWithPatch() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();

        // Update the taxCategory using partial update
        TaxCategory partialUpdatedTaxCategory = new TaxCategory();
        partialUpdatedTaxCategory.setId(taxCategory.getId());

        partialUpdatedTaxCategory.createdat(UPDATED_CREATEDAT);

        restTaxCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxCategory))
            )
            .andExpect(status().isOk());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxCategory.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testTaxCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxCategory.getIsdefault()).isEqualTo(DEFAULT_ISDEFAULT);
    }

    @Test
    @Transactional
    void fullUpdateTaxCategoryWithPatch() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();

        // Update the taxCategory using partial update
        TaxCategory partialUpdatedTaxCategory = new TaxCategory();
        partialUpdatedTaxCategory.setId(taxCategory.getId());

        partialUpdatedTaxCategory.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME).isdefault(UPDATED_ISDEFAULT);

        restTaxCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxCategory))
            )
            .andExpect(status().isOk());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTaxCategory.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTaxCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxCategory.getIsdefault()).isEqualTo(UPDATED_ISDEFAULT);
    }

    @Test
    @Transactional
    void patchNonExistingTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();
        taxCategory.setId(count.incrementAndGet());

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeDelete = taxCategoryRepository.findAll().size();

        // Delete the taxCategory
        restTaxCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
