package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Product;
import com.venjure.domain.ProductTranslation;
import com.venjure.repository.ProductTranslationRepository;
import com.venjure.service.criteria.ProductTranslationCriteria;
import com.venjure.service.dto.ProductTranslationDTO;
import com.venjure.service.mapper.ProductTranslationMapper;
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
 * Integration tests for the {@link ProductTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductTranslationRepository productTranslationRepository;

    @Autowired
    private ProductTranslationMapper productTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTranslationMockMvc;

    private ProductTranslation productTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTranslation createEntity(EntityManager em) {
        ProductTranslation productTranslation = new ProductTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION);
        return productTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTranslation createUpdatedEntity(EntityManager em) {
        ProductTranslation productTranslation = new ProductTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);
        return productTranslation;
    }

    @BeforeEach
    public void initTest() {
        productTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createProductTranslation() throws Exception {
        int databaseSizeBeforeCreate = productTranslationRepository.findAll().size();
        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);
        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        ProductTranslation testProductTranslation = productTranslationList.get(productTranslationList.size() - 1);
        assertThat(testProductTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testProductTranslation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductTranslation.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testProductTranslation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProductTranslationWithExistingId() throws Exception {
        // Create the ProductTranslation with an existing ID
        productTranslation.setId(1L);
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        int databaseSizeBeforeCreate = productTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setCreatedat(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setUpdatedat(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setLanguagecode(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setName(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setSlug(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTranslationRepository.findAll().size();
        // set the field null
        productTranslation.setDescription(null);

        // Create the ProductTranslation, which fails.
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        restProductTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductTranslations() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductTranslation() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get the productTranslation
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, productTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        Long id = productTranslation.getId();

        defaultProductTranslationShouldBeFound("id.equals=" + id);
        defaultProductTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultProductTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultProductTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultProductTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultProductTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultProductTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where createdat is not null
        defaultProductTranslationShouldBeFound("createdat.specified=true");

        // Get all the productTranslationList where createdat is null
        defaultProductTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where updatedat is not null
        defaultProductTranslationShouldBeFound("updatedat.specified=true");

        // Get all the productTranslationList where updatedat is null
        defaultProductTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultProductTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the productTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultProductTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the productTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultProductTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultProductTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the productTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultProductTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode is not null
        defaultProductTranslationShouldBeFound("languagecode.specified=true");

        // Get all the productTranslationList where languagecode is null
        defaultProductTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultProductTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the productTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultProductTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultProductTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the productTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultProductTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name equals to DEFAULT_NAME
        defaultProductTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productTranslationList where name equals to UPDATED_NAME
        defaultProductTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name not equals to DEFAULT_NAME
        defaultProductTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productTranslationList where name not equals to UPDATED_NAME
        defaultProductTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productTranslationList where name equals to UPDATED_NAME
        defaultProductTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name is not null
        defaultProductTranslationShouldBeFound("name.specified=true");

        // Get all the productTranslationList where name is null
        defaultProductTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name contains DEFAULT_NAME
        defaultProductTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productTranslationList where name contains UPDATED_NAME
        defaultProductTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where name does not contain DEFAULT_NAME
        defaultProductTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productTranslationList where name does not contain UPDATED_NAME
        defaultProductTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug equals to DEFAULT_SLUG
        defaultProductTranslationShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the productTranslationList where slug equals to UPDATED_SLUG
        defaultProductTranslationShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug not equals to DEFAULT_SLUG
        defaultProductTranslationShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the productTranslationList where slug not equals to UPDATED_SLUG
        defaultProductTranslationShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultProductTranslationShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the productTranslationList where slug equals to UPDATED_SLUG
        defaultProductTranslationShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug is not null
        defaultProductTranslationShouldBeFound("slug.specified=true");

        // Get all the productTranslationList where slug is null
        defaultProductTranslationShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug contains DEFAULT_SLUG
        defaultProductTranslationShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the productTranslationList where slug contains UPDATED_SLUG
        defaultProductTranslationShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductTranslationsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where slug does not contain DEFAULT_SLUG
        defaultProductTranslationShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the productTranslationList where slug does not contain UPDATED_SLUG
        defaultProductTranslationShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description equals to DEFAULT_DESCRIPTION
        defaultProductTranslationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productTranslationList where description equals to UPDATED_DESCRIPTION
        defaultProductTranslationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description not equals to DEFAULT_DESCRIPTION
        defaultProductTranslationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productTranslationList where description not equals to UPDATED_DESCRIPTION
        defaultProductTranslationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductTranslationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productTranslationList where description equals to UPDATED_DESCRIPTION
        defaultProductTranslationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description is not null
        defaultProductTranslationShouldBeFound("description.specified=true");

        // Get all the productTranslationList where description is null
        defaultProductTranslationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description contains DEFAULT_DESCRIPTION
        defaultProductTranslationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productTranslationList where description contains UPDATED_DESCRIPTION
        defaultProductTranslationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        // Get all the productTranslationList where description does not contain DEFAULT_DESCRIPTION
        defaultProductTranslationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productTranslationList where description does not contain UPDATED_DESCRIPTION
        defaultProductTranslationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);
        Product base = ProductResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        productTranslation.setBase(base);
        productTranslationRepository.saveAndFlush(productTranslation);
        Long baseId = base.getId();

        // Get all the productTranslationList where base equals to baseId
        defaultProductTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the productTranslationList where base equals to (baseId + 1)
        defaultProductTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductTranslationShouldBeFound(String filter) throws Exception {
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductTranslationShouldNotBeFound(String filter) throws Exception {
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductTranslation() throws Exception {
        // Get the productTranslation
        restProductTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductTranslation() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();

        // Update the productTranslation
        ProductTranslation updatedProductTranslation = productTranslationRepository.findById(productTranslation.getId()).get();
        // Disconnect from session so that the updates on updatedProductTranslation are not directly saved in db
        em.detach(updatedProductTranslation);
        updatedProductTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(updatedProductTranslation);

        restProductTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductTranslation testProductTranslation = productTranslationList.get(productTranslationList.size() - 1);
        assertThat(testProductTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductTranslation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testProductTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductTranslationWithPatch() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();

        // Update the productTranslation using partial update
        ProductTranslation partialUpdatedProductTranslation = new ProductTranslation();
        partialUpdatedProductTranslation.setId(productTranslation.getId());

        partialUpdatedProductTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);

        restProductTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductTranslation testProductTranslation = productTranslationList.get(productTranslationList.size() - 1);
        assertThat(testProductTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductTranslation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testProductTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProductTranslationWithPatch() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();

        // Update the productTranslation using partial update
        ProductTranslation partialUpdatedProductTranslation = new ProductTranslation();
        partialUpdatedProductTranslation.setId(productTranslation.getId());

        partialUpdatedProductTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);

        restProductTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
        ProductTranslation testProductTranslation = productTranslationList.get(productTranslationList.size() - 1);
        assertThat(testProductTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testProductTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductTranslation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testProductTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductTranslation() throws Exception {
        int databaseSizeBeforeUpdate = productTranslationRepository.findAll().size();
        productTranslation.setId(count.incrementAndGet());

        // Create the ProductTranslation
        ProductTranslationDTO productTranslationDTO = productTranslationMapper.toDto(productTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTranslation in the database
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductTranslation() throws Exception {
        // Initialize the database
        productTranslationRepository.saveAndFlush(productTranslation);

        int databaseSizeBeforeDelete = productTranslationRepository.findAll().size();

        // Delete the productTranslation
        restProductTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductTranslation> productTranslationList = productTranslationRepository.findAll();
        assertThat(productTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
