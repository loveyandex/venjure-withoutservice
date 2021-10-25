package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Collection;
import com.venjure.domain.CollectionTranslation;
import com.venjure.repository.CollectionTranslationRepository;
import com.venjure.service.criteria.CollectionTranslationCriteria;
import com.venjure.service.dto.CollectionTranslationDTO;
import com.venjure.service.mapper.CollectionTranslationMapper;
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
 * Integration tests for the {@link CollectionTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CollectionTranslationResourceIT {

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

    private static final String ENTITY_API_URL = "/api/collection-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollectionTranslationRepository collectionTranslationRepository;

    @Autowired
    private CollectionTranslationMapper collectionTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectionTranslationMockMvc;

    private CollectionTranslation collectionTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionTranslation createEntity(EntityManager em) {
        CollectionTranslation collectionTranslation = new CollectionTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION);
        return collectionTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionTranslation createUpdatedEntity(EntityManager em) {
        CollectionTranslation collectionTranslation = new CollectionTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);
        return collectionTranslation;
    }

    @BeforeEach
    public void initTest() {
        collectionTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createCollectionTranslation() throws Exception {
        int databaseSizeBeforeCreate = collectionTranslationRepository.findAll().size();
        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);
        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        CollectionTranslation testCollectionTranslation = collectionTranslationList.get(collectionTranslationList.size() - 1);
        assertThat(testCollectionTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCollectionTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCollectionTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testCollectionTranslation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollectionTranslation.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testCollectionTranslation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCollectionTranslationWithExistingId() throws Exception {
        // Create the CollectionTranslation with an existing ID
        collectionTranslation.setId(1L);
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        int databaseSizeBeforeCreate = collectionTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setCreatedat(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setUpdatedat(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setLanguagecode(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setName(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setSlug(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionTranslationRepository.findAll().size();
        // set the field null
        collectionTranslation.setDescription(null);

        // Create the CollectionTranslation, which fails.
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollectionTranslations() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectionTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCollectionTranslation() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get the collectionTranslation
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, collectionTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collectionTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCollectionTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        Long id = collectionTranslation.getId();

        defaultCollectionTranslationShouldBeFound("id.equals=" + id);
        defaultCollectionTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultCollectionTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollectionTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultCollectionTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollectionTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultCollectionTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the collectionTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultCollectionTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the collectionTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultCollectionTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCollectionTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the collectionTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where createdat is not null
        defaultCollectionTranslationShouldBeFound("createdat.specified=true");

        // Get all the collectionTranslationList where createdat is null
        defaultCollectionTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCollectionTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCollectionTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCollectionTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCollectionTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the collectionTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where updatedat is not null
        defaultCollectionTranslationShouldBeFound("updatedat.specified=true");

        // Get all the collectionTranslationList where updatedat is null
        defaultCollectionTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultCollectionTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the collectionTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultCollectionTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the collectionTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the collectionTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode is not null
        defaultCollectionTranslationShouldBeFound("languagecode.specified=true");

        // Get all the collectionTranslationList where languagecode is null
        defaultCollectionTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultCollectionTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the collectionTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultCollectionTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the collectionTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultCollectionTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name equals to DEFAULT_NAME
        defaultCollectionTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the collectionTranslationList where name equals to UPDATED_NAME
        defaultCollectionTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name not equals to DEFAULT_NAME
        defaultCollectionTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the collectionTranslationList where name not equals to UPDATED_NAME
        defaultCollectionTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCollectionTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the collectionTranslationList where name equals to UPDATED_NAME
        defaultCollectionTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name is not null
        defaultCollectionTranslationShouldBeFound("name.specified=true");

        // Get all the collectionTranslationList where name is null
        defaultCollectionTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name contains DEFAULT_NAME
        defaultCollectionTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the collectionTranslationList where name contains UPDATED_NAME
        defaultCollectionTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where name does not contain DEFAULT_NAME
        defaultCollectionTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the collectionTranslationList where name does not contain UPDATED_NAME
        defaultCollectionTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug equals to DEFAULT_SLUG
        defaultCollectionTranslationShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the collectionTranslationList where slug equals to UPDATED_SLUG
        defaultCollectionTranslationShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug not equals to DEFAULT_SLUG
        defaultCollectionTranslationShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the collectionTranslationList where slug not equals to UPDATED_SLUG
        defaultCollectionTranslationShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultCollectionTranslationShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the collectionTranslationList where slug equals to UPDATED_SLUG
        defaultCollectionTranslationShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug is not null
        defaultCollectionTranslationShouldBeFound("slug.specified=true");

        // Get all the collectionTranslationList where slug is null
        defaultCollectionTranslationShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug contains DEFAULT_SLUG
        defaultCollectionTranslationShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the collectionTranslationList where slug contains UPDATED_SLUG
        defaultCollectionTranslationShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where slug does not contain DEFAULT_SLUG
        defaultCollectionTranslationShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the collectionTranslationList where slug does not contain UPDATED_SLUG
        defaultCollectionTranslationShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description equals to DEFAULT_DESCRIPTION
        defaultCollectionTranslationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the collectionTranslationList where description equals to UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description not equals to DEFAULT_DESCRIPTION
        defaultCollectionTranslationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the collectionTranslationList where description not equals to UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the collectionTranslationList where description equals to UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description is not null
        defaultCollectionTranslationShouldBeFound("description.specified=true");

        // Get all the collectionTranslationList where description is null
        defaultCollectionTranslationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description contains DEFAULT_DESCRIPTION
        defaultCollectionTranslationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the collectionTranslationList where description contains UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        // Get all the collectionTranslationList where description does not contain DEFAULT_DESCRIPTION
        defaultCollectionTranslationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the collectionTranslationList where description does not contain UPDATED_DESCRIPTION
        defaultCollectionTranslationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCollectionTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);
        Collection base = CollectionResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        collectionTranslation.setBase(base);
        collectionTranslationRepository.saveAndFlush(collectionTranslation);
        Long baseId = base.getId();

        // Get all the collectionTranslationList where base equals to baseId
        defaultCollectionTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the collectionTranslationList where base equals to (baseId + 1)
        defaultCollectionTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollectionTranslationShouldBeFound(String filter) throws Exception {
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectionTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollectionTranslationShouldNotBeFound(String filter) throws Exception {
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollectionTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollectionTranslation() throws Exception {
        // Get the collectionTranslation
        restCollectionTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollectionTranslation() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();

        // Update the collectionTranslation
        CollectionTranslation updatedCollectionTranslation = collectionTranslationRepository.findById(collectionTranslation.getId()).get();
        // Disconnect from session so that the updates on updatedCollectionTranslation are not directly saved in db
        em.detach(updatedCollectionTranslation);
        updatedCollectionTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(updatedCollectionTranslation);

        restCollectionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
        CollectionTranslation testCollectionTranslation = collectionTranslationList.get(collectionTranslationList.size() - 1);
        assertThat(testCollectionTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollectionTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollectionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCollectionTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollectionTranslation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testCollectionTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollectionTranslationWithPatch() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();

        // Update the collectionTranslation using partial update
        CollectionTranslation partialUpdatedCollectionTranslation = new CollectionTranslation();
        partialUpdatedCollectionTranslation.setId(collectionTranslation.getId());

        partialUpdatedCollectionTranslation.languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCollectionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionTranslation))
            )
            .andExpect(status().isOk());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
        CollectionTranslation testCollectionTranslation = collectionTranslationList.get(collectionTranslationList.size() - 1);
        assertThat(testCollectionTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCollectionTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCollectionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCollectionTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollectionTranslation.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testCollectionTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCollectionTranslationWithPatch() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();

        // Update the collectionTranslation using partial update
        CollectionTranslation partialUpdatedCollectionTranslation = new CollectionTranslation();
        partialUpdatedCollectionTranslation.setId(collectionTranslation.getId());

        partialUpdatedCollectionTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION);

        restCollectionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionTranslation))
            )
            .andExpect(status().isOk());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
        CollectionTranslation testCollectionTranslation = collectionTranslationList.get(collectionTranslationList.size() - 1);
        assertThat(testCollectionTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollectionTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollectionTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testCollectionTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollectionTranslation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testCollectionTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collectionTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollectionTranslation() throws Exception {
        int databaseSizeBeforeUpdate = collectionTranslationRepository.findAll().size();
        collectionTranslation.setId(count.incrementAndGet());

        // Create the CollectionTranslation
        CollectionTranslationDTO collectionTranslationDTO = collectionTranslationMapper.toDto(collectionTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionTranslation in the database
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollectionTranslation() throws Exception {
        // Initialize the database
        collectionTranslationRepository.saveAndFlush(collectionTranslation);

        int databaseSizeBeforeDelete = collectionTranslationRepository.findAll().size();

        // Delete the collectionTranslation
        restCollectionTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, collectionTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollectionTranslation> collectionTranslationList = collectionTranslationRepository.findAll();
        assertThat(collectionTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
