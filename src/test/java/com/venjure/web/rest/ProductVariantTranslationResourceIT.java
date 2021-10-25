package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.ProductVariantTranslation;
import com.venjure.repository.ProductVariantTranslationRepository;
import com.venjure.service.criteria.ProductVariantTranslationCriteria;
import com.venjure.service.dto.ProductVariantTranslationDTO;
import com.venjure.service.mapper.ProductVariantTranslationMapper;
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
 * Integration tests for the {@link ProductVariantTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductVariantTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-variant-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductVariantTranslationRepository productVariantTranslationRepository;

    @Autowired
    private ProductVariantTranslationMapper productVariantTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductVariantTranslationMockMvc;

    private ProductVariantTranslation productVariantTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantTranslation createEntity(EntityManager em) {
        ProductVariantTranslation productVariantTranslation = new ProductVariantTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return productVariantTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantTranslation createUpdatedEntity(EntityManager em) {
        ProductVariantTranslation productVariantTranslation = new ProductVariantTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return productVariantTranslation;
    }

    @BeforeEach
    public void initTest() {
        productVariantTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createProductVariantTranslation() throws Exception {
        int databaseSizeBeforeCreate = productVariantTranslationRepository.findAll().size();
        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);
        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariantTranslation testProductVariantTranslation = productVariantTranslationList.get(
            productVariantTranslationList.size() - 1
        );
        assertThat(testProductVariantTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductVariantTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariantTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testProductVariantTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createProductVariantTranslationWithExistingId() throws Exception {
        // Create the ProductVariantTranslation with an existing ID
        productVariantTranslation.setId(1L);
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        int databaseSizeBeforeCreate = productVariantTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantTranslationRepository.findAll().size();
        // set the field null
        productVariantTranslation.setCreatedat(null);

        // Create the ProductVariantTranslation, which fails.
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantTranslationRepository.findAll().size();
        // set the field null
        productVariantTranslation.setUpdatedat(null);

        // Create the ProductVariantTranslation, which fails.
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantTranslationRepository.findAll().size();
        // set the field null
        productVariantTranslation.setLanguagecode(null);

        // Create the ProductVariantTranslation, which fails.
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantTranslationRepository.findAll().size();
        // set the field null
        productVariantTranslation.setName(null);

        // Create the ProductVariantTranslation, which fails.
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        restProductVariantTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslations() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProductVariantTranslation() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get the productVariantTranslation
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, productVariantTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVariantTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getProductVariantTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        Long id = productVariantTranslation.getId();

        defaultProductVariantTranslationShouldBeFound("id.equals=" + id);
        defaultProductVariantTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultProductVariantTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductVariantTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultProductVariantTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductVariantTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultProductVariantTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductVariantTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultProductVariantTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductVariantTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productVariantTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where createdat is not null
        defaultProductVariantTranslationShouldBeFound("createdat.specified=true");

        // Get all the productVariantTranslationList where createdat is null
        defaultProductVariantTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductVariantTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductVariantTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductVariantTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductVariantTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productVariantTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where updatedat is not null
        defaultProductVariantTranslationShouldBeFound("updatedat.specified=true");

        // Get all the productVariantTranslationList where updatedat is null
        defaultProductVariantTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultProductVariantTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the productVariantTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultProductVariantTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the productVariantTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the productVariantTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode is not null
        defaultProductVariantTranslationShouldBeFound("languagecode.specified=true");

        // Get all the productVariantTranslationList where languagecode is null
        defaultProductVariantTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultProductVariantTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the productVariantTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultProductVariantTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the productVariantTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultProductVariantTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name equals to DEFAULT_NAME
        defaultProductVariantTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productVariantTranslationList where name equals to UPDATED_NAME
        defaultProductVariantTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name not equals to DEFAULT_NAME
        defaultProductVariantTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productVariantTranslationList where name not equals to UPDATED_NAME
        defaultProductVariantTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductVariantTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productVariantTranslationList where name equals to UPDATED_NAME
        defaultProductVariantTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name is not null
        defaultProductVariantTranslationShouldBeFound("name.specified=true");

        // Get all the productVariantTranslationList where name is null
        defaultProductVariantTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name contains DEFAULT_NAME
        defaultProductVariantTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productVariantTranslationList where name contains UPDATED_NAME
        defaultProductVariantTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        // Get all the productVariantTranslationList where name does not contain DEFAULT_NAME
        defaultProductVariantTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productVariantTranslationList where name does not contain UPDATED_NAME
        defaultProductVariantTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductVariantTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);
        ProductVariant base = ProductVariantResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        productVariantTranslation.setBase(base);
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);
        Long baseId = base.getId();

        // Get all the productVariantTranslationList where base equals to baseId
        defaultProductVariantTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the productVariantTranslationList where base equals to (baseId + 1)
        defaultProductVariantTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductVariantTranslationShouldBeFound(String filter) throws Exception {
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductVariantTranslationShouldNotBeFound(String filter) throws Exception {
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductVariantTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductVariantTranslation() throws Exception {
        // Get the productVariantTranslation
        restProductVariantTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductVariantTranslation() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();

        // Update the productVariantTranslation
        ProductVariantTranslation updatedProductVariantTranslation = productVariantTranslationRepository
            .findById(productVariantTranslation.getId())
            .get();
        // Disconnect from session so that the updates on updatedProductVariantTranslation are not directly saved in db
        em.detach(updatedProductVariantTranslation);
        updatedProductVariantTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(updatedProductVariantTranslation);

        restProductVariantTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantTranslation testProductVariantTranslation = productVariantTranslationList.get(
            productVariantTranslationList.size() - 1
        );
        assertThat(testProductVariantTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductVariantTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductVariantTranslationWithPatch() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();

        // Update the productVariantTranslation using partial update
        ProductVariantTranslation partialUpdatedProductVariantTranslation = new ProductVariantTranslation();
        partialUpdatedProductVariantTranslation.setId(productVariantTranslation.getId());

        partialUpdatedProductVariantTranslation.createdat(UPDATED_CREATEDAT).languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME);

        restProductVariantTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantTranslation testProductVariantTranslation = productVariantTranslationList.get(
            productVariantTranslationList.size() - 1
        );
        assertThat(testProductVariantTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariantTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductVariantTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProductVariantTranslationWithPatch() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();

        // Update the productVariantTranslation using partial update
        ProductVariantTranslation partialUpdatedProductVariantTranslation = new ProductVariantTranslation();
        partialUpdatedProductVariantTranslation.setId(productVariantTranslation.getId());

        partialUpdatedProductVariantTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);

        restProductVariantTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantTranslation testProductVariantTranslation = productVariantTranslationList.get(
            productVariantTranslationList.size() - 1
        );
        assertThat(testProductVariantTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductVariantTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVariantTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVariantTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productVariantTranslationRepository.findAll().size();
        productVariantTranslation.setId(count.incrementAndGet());

        // Create the ProductVariantTranslation
        ProductVariantTranslationDTO productVariantTranslationDTO = productVariantTranslationMapper.toDto(productVariantTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantTranslation in the database
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVariantTranslation() throws Exception {
        // Initialize the database
        productVariantTranslationRepository.saveAndFlush(productVariantTranslation);

        int databaseSizeBeforeDelete = productVariantTranslationRepository.findAll().size();

        // Delete the productVariantTranslation
        restProductVariantTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVariantTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductVariantTranslation> productVariantTranslationList = productVariantTranslationRepository.findAll();
        assertThat(productVariantTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
