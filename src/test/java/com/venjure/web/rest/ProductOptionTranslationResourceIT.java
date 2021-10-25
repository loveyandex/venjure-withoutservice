package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ProductOption;
import com.venjure.domain.ProductOptionTranslation;
import com.venjure.repository.ProductOptionTranslationRepository;
import com.venjure.service.criteria.ProductOptionTranslationCriteria;
import com.venjure.service.dto.ProductOptionTranslationDTO;
import com.venjure.service.mapper.ProductOptionTranslationMapper;
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
 * Integration tests for the {@link ProductOptionTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOptionTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-option-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductOptionTranslationRepository productOptionTranslationRepository;

    @Autowired
    private ProductOptionTranslationMapper productOptionTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOptionTranslationMockMvc;

    private ProductOptionTranslation productOptionTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOptionTranslation createEntity(EntityManager em) {
        ProductOptionTranslation productOptionTranslation = new ProductOptionTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return productOptionTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOptionTranslation createUpdatedEntity(EntityManager em) {
        ProductOptionTranslation productOptionTranslation = new ProductOptionTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return productOptionTranslation;
    }

    @BeforeEach
    public void initTest() {
        productOptionTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createProductOptionTranslation() throws Exception {
        int databaseSizeBeforeCreate = productOptionTranslationRepository.findAll().size();
        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);
        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOptionTranslation testProductOptionTranslation = productOptionTranslationList.get(productOptionTranslationList.size() - 1);
        assertThat(testProductOptionTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductOptionTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductOptionTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testProductOptionTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createProductOptionTranslationWithExistingId() throws Exception {
        // Create the ProductOptionTranslation with an existing ID
        productOptionTranslation.setId(1L);
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        int databaseSizeBeforeCreate = productOptionTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionTranslationRepository.findAll().size();
        // set the field null
        productOptionTranslation.setCreatedat(null);

        // Create the ProductOptionTranslation, which fails.
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionTranslationRepository.findAll().size();
        // set the field null
        productOptionTranslation.setUpdatedat(null);

        // Create the ProductOptionTranslation, which fails.
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionTranslationRepository.findAll().size();
        // set the field null
        productOptionTranslation.setLanguagecode(null);

        // Create the ProductOptionTranslation, which fails.
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionTranslationRepository.findAll().size();
        // set the field null
        productOptionTranslation.setName(null);

        // Create the ProductOptionTranslation, which fails.
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        restProductOptionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslations() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOptionTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProductOptionTranslation() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get the productOptionTranslation
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, productOptionTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOptionTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getProductOptionTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        Long id = productOptionTranslation.getId();

        defaultProductOptionTranslationShouldBeFound("id.equals=" + id);
        defaultProductOptionTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultProductOptionTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductOptionTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultProductOptionTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductOptionTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultProductOptionTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductOptionTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultProductOptionTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductOptionTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productOptionTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where createdat is not null
        defaultProductOptionTranslationShouldBeFound("createdat.specified=true");

        // Get all the productOptionTranslationList where createdat is null
        defaultProductOptionTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductOptionTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductOptionTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductOptionTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductOptionTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productOptionTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where updatedat is not null
        defaultProductOptionTranslationShouldBeFound("updatedat.specified=true");

        // Get all the productOptionTranslationList where updatedat is null
        defaultProductOptionTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultProductOptionTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the productOptionTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultProductOptionTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the productOptionTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the productOptionTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode is not null
        defaultProductOptionTranslationShouldBeFound("languagecode.specified=true");

        // Get all the productOptionTranslationList where languagecode is null
        defaultProductOptionTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultProductOptionTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the productOptionTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultProductOptionTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the productOptionTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultProductOptionTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name equals to DEFAULT_NAME
        defaultProductOptionTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productOptionTranslationList where name equals to UPDATED_NAME
        defaultProductOptionTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name not equals to DEFAULT_NAME
        defaultProductOptionTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productOptionTranslationList where name not equals to UPDATED_NAME
        defaultProductOptionTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductOptionTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productOptionTranslationList where name equals to UPDATED_NAME
        defaultProductOptionTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name is not null
        defaultProductOptionTranslationShouldBeFound("name.specified=true");

        // Get all the productOptionTranslationList where name is null
        defaultProductOptionTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name contains DEFAULT_NAME
        defaultProductOptionTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productOptionTranslationList where name contains UPDATED_NAME
        defaultProductOptionTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        // Get all the productOptionTranslationList where name does not contain DEFAULT_NAME
        defaultProductOptionTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productOptionTranslationList where name does not contain UPDATED_NAME
        defaultProductOptionTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductOptionTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);
        ProductOption base = ProductOptionResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        productOptionTranslation.setBase(base);
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);
        Long baseId = base.getId();

        // Get all the productOptionTranslationList where base equals to baseId
        defaultProductOptionTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the productOptionTranslationList where base equals to (baseId + 1)
        defaultProductOptionTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductOptionTranslationShouldBeFound(String filter) throws Exception {
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOptionTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductOptionTranslationShouldNotBeFound(String filter) throws Exception {
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOptionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductOptionTranslation() throws Exception {
        // Get the productOptionTranslation
        restProductOptionTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductOptionTranslation() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();

        // Update the productOptionTranslation
        ProductOptionTranslation updatedProductOptionTranslation = productOptionTranslationRepository
            .findById(productOptionTranslation.getId())
            .get();
        // Disconnect from session so that the updates on updatedProductOptionTranslation are not directly saved in db
        em.detach(updatedProductOptionTranslation);
        updatedProductOptionTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(updatedProductOptionTranslation);

        restProductOptionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionTranslation testProductOptionTranslation = productOptionTranslationList.get(productOptionTranslationList.size() - 1);
        assertThat(testProductOptionTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOptionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductOptionTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOptionTranslationWithPatch() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();

        // Update the productOptionTranslation using partial update
        ProductOptionTranslation partialUpdatedProductOptionTranslation = new ProductOptionTranslation();
        partialUpdatedProductOptionTranslation.setId(productOptionTranslation.getId());

        partialUpdatedProductOptionTranslation.createdat(UPDATED_CREATEDAT).languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME);

        restProductOptionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOptionTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOptionTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionTranslation testProductOptionTranslation = productOptionTranslationList.get(productOptionTranslationList.size() - 1);
        assertThat(testProductOptionTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductOptionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductOptionTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProductOptionTranslationWithPatch() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();

        // Update the productOptionTranslation using partial update
        ProductOptionTranslation partialUpdatedProductOptionTranslation = new ProductOptionTranslation();
        partialUpdatedProductOptionTranslation.setId(productOptionTranslation.getId());

        partialUpdatedProductOptionTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);

        restProductOptionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOptionTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOptionTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionTranslation testProductOptionTranslation = productOptionTranslationList.get(productOptionTranslationList.size() - 1);
        assertThat(testProductOptionTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOptionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductOptionTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOptionTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOptionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productOptionTranslationRepository.findAll().size();
        productOptionTranslation.setId(count.incrementAndGet());

        // Create the ProductOptionTranslation
        ProductOptionTranslationDTO productOptionTranslationDTO = productOptionTranslationMapper.toDto(productOptionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOptionTranslation in the database
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOptionTranslation() throws Exception {
        // Initialize the database
        productOptionTranslationRepository.saveAndFlush(productOptionTranslation);

        int databaseSizeBeforeDelete = productOptionTranslationRepository.findAll().size();

        // Delete the productOptionTranslation
        restProductOptionTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOptionTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOptionTranslation> productOptionTranslationList = productOptionTranslationRepository.findAll();
        assertThat(productOptionTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
